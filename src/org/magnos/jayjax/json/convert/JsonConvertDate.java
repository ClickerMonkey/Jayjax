package org.magnos.jayjax.json.convert;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonNumber;
import org.magnos.jayjax.json.JsonValue;



public class JsonConvertDate
{

	private static long getMillis(JsonValue json)
	{
		switch (json.getType()) 
		{
		case NUMBER:
			return ((JsonNumber)json).get().longValue();
		case STRING:
			try {
				String[] parts = json.getObject().toString().split( " " );
				SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss", Locale.ENGLISH);
			    sdf.setTimeZone(TimeZone.getTimeZone(parts[5].substring(0,6)+":"+parts[5].substring(6)));
			    return sdf.parse(parts[0]+" "+parts[1]+" "+parts[2]+" "+parts[3]+" "+parts[4]).getTime();  
			} catch (ParseException e) {
				throw new RuntimeException( e );
			}
		default:
			throw new RuntimeException( "Invalid date: " + json.getObject().toString() );
		}
	}
	
	public static final JsonConverter<Date, JsonValue> DATE = new JsonConverter<Date, JsonValue>() {
		public JsonValue write( Date value ) {
			return new JsonNumber( value.getTime() );
		}
		public Date read( JsonValue value ) {
			return new Date( getMillis( value ) );
		}
	};

	public static final JsonConverter<Timestamp, JsonValue> TIMESTAMP = new JsonConverter<Timestamp, JsonValue>() {
		public JsonValue write( Timestamp value ) {
			return new JsonNumber( value.getTime() );
		}
		public Timestamp read( JsonValue value ) {
			return new Timestamp( getMillis( value ) );
		}
	};

	public static final JsonConverter<Time, JsonValue> TIME = new JsonConverter<Time, JsonValue>() {
		public JsonValue write( Time value ) {
			return new JsonNumber( value.getTime() );
		}
		public Time read( JsonValue value ) {
			return new Time( getMillis( value ) );
		}
	};

	public static final JsonConverter<java.sql.Date, JsonValue> SQL_DATE = new JsonConverter<java.sql.Date, JsonValue>() {
		public JsonValue write( java.sql.Date value ) {
			return new JsonNumber( value.getTime() );
		}
		public java.sql.Date read( JsonValue value ) {
			return new java.sql.Date( getMillis( value ) );
		}
	};

	public static final JsonConverter<Calendar, JsonValue> CALENDAR = new JsonConverter<Calendar, JsonValue>() {
		public JsonValue write( Calendar value ) {
			return new JsonNumber( value.getTimeInMillis() );
		}
		public Calendar read( JsonValue value ) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis( getMillis( value ) );
			return cal;
		}
	};

}
