package org.magnos.jayjax.json.convert;

import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonString;


public class JsonConvertString
{

    public static final JsonConverter<String, JsonString> STRING = new JsonConverter<String, JsonString>() {
//        public String empty() {
//            return "";
//        }
        public String read( JsonString value ) {
            return value.get();
        }
        public JsonString write( String value ) {
            return new JsonString( value );
        }
    };

    public static final JsonConverter<StringBuilder, JsonString> STRING_BUILDER = new JsonConverter<StringBuilder, JsonString>() {
//        public StringBuilder empty() {
//            return new StringBuilder();
//        }
        public StringBuilder read( JsonString value ) {
            return new StringBuilder( value.get() );
        }
        public JsonString write( StringBuilder value ) {
            return new JsonString( value );
        }
    };

    public static final JsonConverter<StringBuffer, JsonString> STRING_BUFFER = new JsonConverter<StringBuffer, JsonString>() {
//        public StringBuffer empty() {
//            return new StringBuffer();
//        }
        public StringBuffer read( JsonString value ) {
            return new StringBuffer( value.get() );
        }
        public JsonString write( StringBuffer value ) {
            return new JsonString( value );
        }
    };

    public static final JsonConverter<CharSequence, JsonString> CHAR_SEQUENCE = new JsonConverter<CharSequence, JsonString>() {
//        public CharSequence empty() {
//            return "";
//        }
        public CharSequence read( JsonString value ) {
            return value.get();
        }
        public JsonString write( CharSequence value ) {
            return new JsonString( value );
        }
    };

    public static final JsonConverter<Appendable, JsonString> APPENDABLE = new JsonConverter<Appendable, JsonString>() {
//        public Appendable empty() {
//            return new StringBuilder();
//        }
        public Appendable read( JsonString value ) {
            return new StringBuilder( value.get() );
        }
        public JsonString write( Appendable value ) {
            return new JsonString( value );
        }
    };
    
}
