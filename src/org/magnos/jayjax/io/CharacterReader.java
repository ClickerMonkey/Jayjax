/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

package org.magnos.jayjax.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;


/**
 * A simple parser for reading characters. The parser keeps track of character
 * index, line number, and column index.
 * 
 * @author Philip Diffenderfer
 * 
 */
public abstract class CharacterReader
{

    public static final char NEWLINE = '\n';
    public static final char TAB = '\t';
    public static final char RETURN = '\r';
    public static final char FORMFEED = '\f';
    public static final char BACKSPACE = '\b';

    public static final char CHAR_ESCAPE = '\\';
    public static final char CHAR_NEWLINE = 'n';
    public static final char CHAR_TAB = 't';
    public static final char CHAR_RETURN = 'r';
    public static final char CHAR_FORMFEED = 'f';
    public static final char CHAR_BACKSPACE = 'b';
    public static final char CHAR_UNICODE = 'u';

    public static final int NO_DATA = -1;
    public static final int READLIMIT = 6;

    /**
     * Whitespace are all characters <= the space character.
     */
    public static final CharacterSet SET_WHITESPACE = new CharacterSet( new char[][] { { '\0', ' ' } } );

    /**
     * Control characters are all characters < space character.
     */
    public static final CharacterSet SET_CONTROL_CHARACTERS = new CharacterSet( new char[][] { { '\0', ' ' - 1 } } );

    /**
     * Hex characters are any digit and any letter in any case.
     */
    public static final CharacterSet SET_HEX = new CharacterSet( new char[][] { { '0', '9' }, { 'a', 'f' }, { 'A', 'F' } } );

    public StringBuilder read;
    public int data;
    public int line;
    public int character;
    public int column;
    public int lastColumn;

    /**
     * Instantiates a new CharacterReader.
     * 
     * @param in
     *        The reader to take input from.
     */
    public CharacterReader()
    {
        this.read = new StringBuilder();
    }

    /**
     * Reads a single character from input, return -1 when all input has been
     * read.
     * 
     * @return The character read or -1 if there are no more characters.
     * @throws IOException
     *         An error occurred reading from input.
     */
    protected abstract int read() throws IOException;

    /**
     * Marks the current position for later {@link #reset()}.
     * 
     * @param readLimit
     *        The maximum number of characters that might be read before a
     *        {@link #reset()} is called.
     * @throws IOException
     *         An error occurred marking the input.
     */
    protected abstract void mark( int readLimit ) throws IOException;

    /**
     * Resets the reading position to the last {@link #mark(int)}ed reading
     * position.
     * 
     * @throws IOException
     *         An error occurred resetting the input.
     */
    protected abstract void reset() throws IOException;

    /**
     * Parses a single character from the current character (data). If escapable
     * is true and the current character is an escape character, then possible
     * values will be determined. If the next character is a control character
     * and control characters are not allowed an {@link IOException} is thrown.
     * If the next character is a marker for a control character, the actual
     * control character is returned. If the next character is a marker for
     * Unicode then the next 4 hex characters are read to determine the Unicode
     * value.
     * 
     * @param escapable
     *        If escapable characters are permitted to be read.
     * @param rejectControlCharacters
     *        If control characters following an escape are allowed.
     * @param acceptUnicode
     *        If Unicode is acceptable after an escape.
     * @return The character parsed.
     * @throws IOException
     *         An error occurred reading from the reader.
     */
    public char parseCharacter( boolean escapable, boolean rejectControlCharacters, boolean acceptUnicode ) throws IOException
    {
        if (escapable && data == CHAR_ESCAPE)
        {
            readData();

            if (rejectControlCharacters && SET_CONTROL_CHARACTERS.has( data ))
            {
                throw new IOException( "Control characters are not acceptable " + getCursor() );
            }

            switch (data)
            {
            case NO_DATA:
                throw new IOException( "Expected character after escape " + getCursor() );
            case CHAR_TAB:
                data = TAB;
                break;
            case CHAR_NEWLINE:
                data = NEWLINE;
                break;
            case CHAR_RETURN:
                data = RETURN;
                break;
            case CHAR_FORMFEED:
                data = FORMFEED;
                break;
            case CHAR_BACKSPACE:
                data = BACKSPACE;
                break;
            case CHAR_UNICODE:
                if (acceptUnicode)
                {
                    int h0 = readHexDigitIndexed( 0 );
                    int h1 = readHexDigitIndexed( 1 );
                    int h2 = readHexDigitIndexed( 2 );
                    int h3 = readHexDigitIndexed( 3 );
                    data = (h0 << 12) | (h1 << 8) | (h2 << 4) | h3;
                }
                break;
            }
        }

        return (char)data;
    }

    /**
     * Reads a hex digit from the next character in the reader.
     * 
     * @return The hex value, between 0 and 15 inclusive.
     * @throws IOException
     *         An error occurred reading the next error.
     */
    public int readHexDigit() throws IOException
    {
        int hex = CharacterReader.hex( readData() );

        if (hex == NO_DATA)
        {
            throw new IOException( "Expected hex digit " + getCursor() );
        }

        return hex;
    }

    /**
     * Reads a hex digit from the next character in the reader.
     * 
     * @param index
     *        The index of the hex digit in a Unicode character. This is used to
     *        create a more useful exception message.
     * @return The hex value, between 0 and 15 inclusive.
     * @throws IOException
     *         An error occurred reading the next character.
     */
    public int readHexDigitIndexed( int index ) throws IOException
    {
        int hex = CharacterReader.hex( readData() );

        if (hex == NO_DATA)
        {
            throw new IOException( "Expected hex digit " + index + " " + getCursor() );
        }

        return hex;
    }

    /**
     * Reads and returns a string until any one of the characters in a set is
     * reached.
     * 
     * @param stop
     *        The set to use to determine when to stop.
     * @param includeCurrent
     *        If the current character should be included in the output.
     * @param readAsCharacter
     *        If {@link #parseCharacter(boolean, boolean, boolean)} should be
     *        used to build characters in the string, if not the raw character
     *        value is appended to the output.
     * @param backup
     *        If the reader should back up one character so the next character
     *        in the reader is the character that it stopped on.
     * @return The string read from the current character to a stopping
     *         character.
     * @throws IOException
     *         An error occurred reading the next character.
     */
    public String readUntil( CharacterSet stop, boolean includeCurrent, boolean readAsCharacter, boolean backup ) throws IOException
    {
        StringBuilder out = new StringBuilder();

        if (includeCurrent)
        {
            out.append( (char)data );
        }

        if (backup)
        {
            mark( READLIMIT );    
        }

        while (readData() != NO_DATA && !stop.has( data ))
        {
            if (readAsCharacter)
            {
                out.append( parseCharacter( true, true, true ) );
            }
            else
            {
                out.append( (char)data );
            }
            
            if (backup)
            {
                mark( READLIMIT );
            }
        }

        if (backup && stop.has( data ))
        {
            unreadData();

            reset();
        }

        return out.toString();
    }

    /**
     * Reads past all whitespace. This will return the next character in the
     * reader which is not whitespace.
     * 
     * @return The non-whitespace character read or -1 if end of reader.
     * @throws IOException
     *         An error occurred reading the next character.
     */
    public int readPastWhitespace() throws IOException
    {
        while (readData() != NO_DATA && SET_WHITESPACE.has( data ))
            ;

        return data;
    }

    /**
     * Reads the next character.
     * 
     * @return
     *         The next character.
     * @throws IOException
     *         An error occurred reading the next character.
     */
    public int readData() throws IOException
    {
        data = read();

        character++;

        if (data == NEWLINE)
        {
            line++;
            lastColumn = column;
            column = 0;
        }
        else if (data != NO_DATA)
        {
            column++;
        }

        if (data != NO_DATA)
        {
            read.append( (char)data );
        }

        return data;
    }

    /**
     * Unreads the last character read.
     * 
     * @throws IOException
     *         An error occurred unreading the current character.
     */
    public void unreadData() throws IOException
    {
        if (data == NEWLINE)
        {
            line--;
            column = lastColumn;
            character--;
        }
        else if (data != NO_DATA)
        {
            column--;
            character--;
        }

        if (data != NO_DATA)
        {
            read.setLength( read.length() - 1 );
        }
    }

    /**
     * @return A description of the reader consisting of the index of the
     *         current character, it's line, and it's column.
     */
    public String getCursor()
    {
        return String.format( "at character %c at index %d line %d column %d", (char)data, character, line, column );
    }

    /**
     * Converts the character to a hex value. If the character is not a valid
     * hex
     * digit then {@link #NO_DATA} is returned, otherwise a number between 0 and
     * 15 inclusive.
     * 
     * @param x
     *        The character to calculate a hex value from.
     * @return The hex value.
     */
    public static int hex( int x )
    {
        if (x >= '0' && x <= '9')
        {
            return x - '0';
        }
        if (x >= 'a' && x <= 'f')
        {
            return x - 'a' + 10;
        }
        if (x >= 'A' && x <= 'F')
        {
            return x - 'A' + 10;
        }

        return NO_DATA;
    }

    /**
     * Reads the remaining characters from input in a blocking manner and
     * returns all characters read by this reader.
     * 
     * @return The string of the read characters.
     * @throws IOException
     *         An error occurred consuming from the input source.
     */
    public String consume() throws IOException
    {
        while (readData() != NO_DATA)
            ;

        return read.toString();
    }

    /**
     * A CharacterReader implementation where the input is stored entirely in
     * memory and any character in the input can be accessed randomly.
     * 
     * @author pdiffenderfer
     * 
     */
    private static abstract class MemoryReader extends CharacterReader
    {

        /**
         * The length of the input in characters.
         */
        protected abstract int length();

        /**
         * The character at the specified index in the input.
         */
        protected abstract char charAt( int i );

        /**
         * The current position in the input.
         */
        protected int pos;

        /**
         * The position of the last mark.
         */
        protected int mark;

        @Override
        protected int read() throws IOException
        {
            return pos == length() ? -1 : charAt( pos++ );
        }

        @Override
        protected void mark( int readLimit ) throws IOException
        {
            mark = pos;
        }

        @Override
        protected void reset() throws IOException
        {
            pos = mark;
        }
    }

    /**
     * A CharacterReader implementation that performs potentially blocking
     * reads from an InputStream.
     * 
     * @author pdiffenderfer
     * 
     */
    private static abstract class StreamReader extends CharacterReader
    {

        // the last 'readLimit' characters in the stream.
        private int[] lookback = {};
        private int head;
        private int tail;

        /**
         * Reads a single character from the stream, or returns -1.
         */
        protected abstract int streamRead() throws IOException;

        @Override
        protected int read() throws IOException
        {
            if (head < tail)
            {
                return lookback[head++];
            }

            int i = streamRead();

            if (tail < lookback.length)
            {
                lookback[tail++] = i;
            }

            return i;
        }

        @Override
        public void mark( int readAheadLimit ) throws IOException
        {
            if (lookback.length < readAheadLimit)
            {
                lookback = Arrays.copyOf( lookback, readAheadLimit );
            }

            tail = 0;
            head = lookback.length;
        }

        @Override
        public void reset() throws IOException
        {
            head = 0;
            tail = lookback.length;
        }
    }

    /**
     * Returns a CharacterReader for a {@link CharSequence}.
     * 
     * @param x
     *        The {@link CharSequence} to create a CharacterReader for.
     * @return The reference to a new CharacterReader.
     */
    public static CharacterReader forString( final CharSequence x )
    {
        return new MemoryReader() {

            protected int length()
            {
                return x.length();
            }

            protected char charAt( int i )
            {
                return x.charAt( i );
            }
        };
    }

    /**
     * Returns a CharacterReader for a <code>byte[]</code>.
     * 
     * @param x
     *        The <code>byte[]</code> to read from.
     * @return The reference to a new CharacterReader.
     */
    public static CharacterReader forBytes( final byte[] x )
    {
        return new MemoryReader() {

            protected int length()
            {
                return x.length;
            }

            protected char charAt( int i )
            {
                return (char)(x[i] & 0xFF);
            }
        };
    }

    /**
     * Returns a CharacterReader for a <code>char[]</code>.
     * 
     * @param x
     *        The <code>char[]</code> to read from.
     * @return The reference to a new CharacterReader.
     */
    public static CharacterReader forChars( final char[] x )
    {
        return new MemoryReader() {

            protected int length()
            {
                return x.length;
            }

            protected char charAt( int i )
            {
                return x[i];
            }
        };
    }

    /**
     * Returns a CharacterReader for a {@link Reader}
     * 
     * @param reader
     *        The {@link Reader} to read from.
     * @return The reference to a new CharacterReader.
     */
    public static CharacterReader forReader( final Reader reader )
    {
        return new StreamReader() {

            protected int streamRead() throws IOException
            {
                return reader.read();
            }
        };
    }

    /**
     * Returns a CharacterReader for an {@link InputStream}
     * 
     * @param in
     *        The {@link InputStream} to read from.
     * @return The reference to a new CharacterReader.
     */
    public static CharacterReader forStream( final InputStream in )
    {
        return new StreamReader() {

            protected int streamRead() throws IOException
            {
                return in.read();
            }
        };
    }

    /**
     * Returns a CharacterReader for an {@link InputStream} with a given
     * character set.
     * 
     * @param in
     *        The {@link InputStream} to read from.
     * @param charsetName
     *        The name of the character set of the data in the
     *        {@link InputStream}.
     * @return The reference to a new CharacterReader.
     */
    public static CharacterReader forStream( InputStream in, String charsetName ) throws UnsupportedEncodingException
    {
        return forReader( new InputStreamReader( in, charsetName ) );
    }

}
