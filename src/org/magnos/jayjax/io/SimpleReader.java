
package org.magnos.jayjax.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public abstract class SimpleReader extends Reader
{

	private int[] lookback = {};
	private int head;
	private int tail;

	protected abstract int readData() throws IOException;

	@Override
	public int read() throws IOException
	{
		if (head < tail)
		{
			return lookback[head++];
		}

		int i = readData();

		if (tail < lookback.length)
		{
			lookback[tail++] = i;
		}

		return i;
	}

	@Override
	public int read( char[] cbuf, int off, int len ) throws IOException
	{
		if (len == 0) 
		{
			return 0;
		}
		
		int count = 0;
		int d = 0;
		
		while ((d = read()) != -1 && count < len) 
		{
			cbuf[off++] = (char)d;
			count++;
		}
		
		return count == 0 ? -1 : count;
	}

	@Override
	public long skip(long n) throws IOException 
	{
		long total = 0;
		
		while (read() != -1 && total < n)
		{
			total++;
		}
		
		return total;
	}
	
	@Override
	public boolean markSupported()
	{
		return true;
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

	@Override
	public void close() throws IOException
	{

	}
	
	public static SimpleReader forString( final String x )
	{
		return new SimpleReader() {
			private int pos = 0;
			protected int readData() throws IOException {
				return pos == x.length() ? -1 : x.charAt( pos++ );
			}
			public long skip(long n) throws IOException {
				long amount = Math.min( n, x.length() - pos );
				pos += amount;
				return amount;
			}
		};
	}

	public static SimpleReader forStream( final InputStream x )
	{
		return new SimpleReader() {
			protected int readData() throws IOException {
				return x.read();
			}
			public long skip(long n) throws IOException {
				return x.skip(n);
			}
		};
	}
	
	public static SimpleReader forStream( InputStream x, String charsetName ) throws UnsupportedEncodingException
	{
		final Reader reader = new InputStreamReader( x, charsetName );
		
		return new SimpleReader() {
			protected int readData() throws IOException {
				return reader.read();
			}
			public long skip(long n) throws IOException {
				return reader.skip( n );
			}
		};
	}
	
	public static SimpleReader forBytes( final byte[] x )
	{
		return new SimpleReader() {
			private int pos = 0;
			protected int readData() throws IOException {
				return pos == x.length ? -1 : x[ pos++ ] & 0xFF;
			}
			public long skip(long n) throws IOException {
				long amount = Math.min( n, x.length - pos );
				pos += amount;
				return amount;
			}
		};
	}
	
	public static SimpleReader forChars( final char[] x )
	{
		return new SimpleReader() {
			private int pos = 0;
			protected int readData() throws IOException {
				return pos == x.length ? -1 : x[ pos++ ];
			}
			public long skip(long n) throws IOException {
				long amount = Math.min( n, x.length - pos );
				pos += amount;
				return amount;
			}
		};
	}

}
