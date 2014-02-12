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

package org.magnos.jayjax.json.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.magnos.jayjax.json.JsonBoolean;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonNumber;
import org.magnos.jayjax.json.JsonString;
import org.magnos.jayjax.json.JsonValue;


public class JsonConvertPrimitive
{

    public static final JsonConverter<Byte, JsonNumber> BYTE = new JsonConverter<Byte, JsonNumber>() {
        public Byte read( JsonNumber value ) {
            return value.get().byteValue();
        }
        public JsonNumber write( Byte value ) {
            return new JsonNumber( value );
        }
    };

    public static final JsonConverter<Short, JsonNumber> SHORT = new JsonConverter<Short, JsonNumber>() {
        public Short read( JsonNumber value ) {
            return value.get().shortValue();
        }
        public JsonNumber write( Short value ) {
            return new JsonNumber( value );
        }
    };

    public static final JsonConverter<Integer, JsonNumber> INT = new JsonConverter<Integer, JsonNumber>() {
        public Integer read( JsonNumber value ) {
            return value.get().intValue();
        }
        public JsonNumber write( Integer value ) {
            return new JsonNumber( value );
        }
    };

    public static final JsonConverter<Long, JsonNumber> LONG = new JsonConverter<Long, JsonNumber>() {
        public Long read( JsonNumber value ) {
            return value.get().longValue();
        }
        public JsonNumber write( Long value ) {
            return new JsonNumber( value );
        }
    };

    public static final JsonConverter<Float, JsonNumber> FLOAT = new JsonConverter<Float, JsonNumber>() {
        public Float read( JsonNumber value ) {
            return value.get().floatValue();
        }
        public JsonNumber write( Float value ) {
            return new JsonNumber( value );
        }
    };

    public static final JsonConverter<Double, JsonNumber> DOUBLE = new JsonConverter<Double, JsonNumber>() {
        public Double read( JsonNumber value ) {
            return value.get().doubleValue();
        }
        public JsonNumber write( Double value ) {
            return new JsonNumber( value );
        }
    };
    
    public static final JsonConverter<Character, JsonValue> CHAR = new JsonConverter<Character, JsonValue>() {
        public Character read( JsonValue value ) {
            if (value instanceof JsonNumber) {
                return (char)((JsonNumber)value).get().intValue();
            } else if (value instanceof JsonString) {
                return ((JsonString)value).get().charAt( 0 );
            } else {
                throw new RuntimeException( value.getClass().getSimpleName() +" is not valid for a Character" );
            }
        }
        public JsonValue write( Character value ) {
            return new JsonString( value.toString() );
        }
    };
    
    public static final JsonConverter<Boolean, JsonBoolean> BOOLEAN = new JsonConverter<Boolean, JsonBoolean>() {
        public Boolean read( JsonBoolean value ) {
            return value.get();
        }
        public JsonBoolean write( Boolean value ) {
            return new JsonBoolean( value );
        }
    };
    
    public static final JsonConverter<BigInteger, JsonNumber> BIG_INTEGER = new JsonConverter<BigInteger, JsonNumber>() {
		public BigInteger read( JsonNumber value ) {
			return new BigInteger( value.get().toString() );
		}
		public JsonNumber write( BigInteger value ) {
			return new JsonNumber( value.doubleValue() );
		}
	};
    
    public static final JsonConverter<BigDecimal, JsonNumber> BIG_DECIMAL = new JsonConverter<BigDecimal, JsonNumber>() {
		public BigDecimal read( JsonNumber value ) {
			return new BigDecimal( value.get().toString() );
		}
		public JsonNumber write( BigDecimal value ) {
			return new JsonNumber( value );
		}
	};
    
    public static final JsonConverter<AtomicInteger, JsonNumber> ATOMIC_INTEGER = new JsonConverter<AtomicInteger, JsonNumber>() {
		public AtomicInteger read( JsonNumber value ) {
			return new AtomicInteger( value.get().intValue() );
		}
		public JsonNumber write( AtomicInteger value ) {
			return new JsonNumber( value );
		}
	};
    
    public static final JsonConverter<AtomicLong, JsonNumber> ATOMIC_LONG = new JsonConverter<AtomicLong, JsonNumber>() {
		public AtomicLong read( JsonNumber value ) {
			return new AtomicLong( value.get().intValue() );
		}
		public JsonNumber write( AtomicLong value ) {
			return new JsonNumber( value );
		}
	};
    
}
