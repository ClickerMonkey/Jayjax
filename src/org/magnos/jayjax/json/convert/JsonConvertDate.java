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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonNumber;
import org.magnos.jayjax.json.JsonString;
import org.magnos.jayjax.json.JsonValue;



public class JsonConvertDate
{
	
	public static final JsonConverter<Date, JsonValue> DATE = new JsonConverter<Date, JsonValue>() {
		public JsonValue write( Date value ) {
			return new JsonNumber( value.getTime() );
		}
		public Date read( JsonValue value ) {
			return new Date( parse( value ) );
		}
	};

	public static final JsonConverter<Timestamp, JsonValue> TIMESTAMP = new JsonConverter<Timestamp, JsonValue>() {
		public JsonValue write( Timestamp value ) {
			return new JsonNumber( value.getTime() );
		}
		public Timestamp read( JsonValue value ) {
			return new Timestamp( parse( value ) );
		}
	};

	public static final JsonConverter<Time, JsonValue> TIME = new JsonConverter<Time, JsonValue>() {
		public JsonValue write( Time value ) {
			return new JsonNumber( value.getTime() );
		}
		public Time read( JsonValue value ) {
			return new Time( parse( value ) );
		}
	};

	public static final JsonConverter<java.sql.Date, JsonValue> SQL_DATE = new JsonConverter<java.sql.Date, JsonValue>() {
		public JsonValue write( java.sql.Date value ) {
			return new JsonNumber( value.getTime() );
		}
		public java.sql.Date read( JsonValue value ) {
			return new java.sql.Date( parse( value ) );
		}
	};

	public static final JsonConverter<Calendar, JsonValue> CALENDAR = new JsonConverter<Calendar, JsonValue>() {
		public JsonValue write( Calendar value ) {
			return new JsonNumber( value.getTimeInMillis() );
		}
		public Calendar read( JsonValue value ) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis( parse( value ) );
			return cal;
		}
	};

	private static final Pattern TIMEZONE = Pattern.compile( "((GMT)?\\s?[+-]\\d{1,2}(\\s|$|:?\\d{2}))" );
	private static final Pattern HOUR = Pattern.compile( "(^|[\\sT])(0?[0-9]|1[0-9]|2[0-3]):" ); 
	private static final Pattern MINUTE = Pattern.compile( ":([0-5][0-9]):" );
	private static final Pattern SECOND = Pattern.compile( ":([0-5][0-9])(?:\\s|\\.|$)" );
	private static final Pattern MILLIS = Pattern.compile( "\\.([\\d]{2,6})" );
	private static final Pattern DAY = Pattern.compile( "(\\s|/|-)([0-3]?[0-9])(\\s|/|T)" );
	private static final Pattern YEAR = Pattern.compile( "(^|[\\s/])(\\d{2}$|\\d{4})($|[\\s-])" );	
	private static final Pattern PM = Pattern.compile( "\\s(PM)(\\s|$)" );
	
	private static final Pattern[] MONTHS = 
	{
		Pattern.compile( "JAN(UARY)?|(^|\\s)0?1/|-(01|1)-" ),
		Pattern.compile( "FEB(RUARY)?|(^|\\s)0?2/|-(02|2)-" ),
		Pattern.compile( "MAR(CH)?|(^|\\s)0?3/|-(03|3)-" ),
		Pattern.compile( "APR(IL)?|(^|\\s)0?4/|-(04|4)-" ),
		Pattern.compile( "MAY|(^|\\s)0?5/|-(05|5)-" ),
		Pattern.compile( "JUNE?|(^|\\s)0?6/|-(06|6)-" ),
		Pattern.compile( "JULY?|(^|\\s)0?7/|-(07|7)-" ),
		Pattern.compile( "AUG(UST)?|(^|\\s)0?8/|-(08|8)-" ),
		Pattern.compile( "SEP(TEMBER)?|(^|\\s)0?9/|-(09|9)-" ),
		Pattern.compile( "OCT(OBER)?|(^|\\s)10/|-10-" ),
		Pattern.compile( "NOV(EMBER)?|(^|\\s)11/|-11-" ),
		Pattern.compile( "DEC(EMBER)?|(^|\\s)12/|-12-" )
	};
	
	private static int find(Pattern p, int group, String x, int missingValue) 
	{
		String found = find(p, group, x, null);
		
		return found == null ? missingValue : Integer.parseInt( found, 10 );
	}
	
	private static String find(Pattern p, int group, String x, String missingValue) 
	{
		Matcher m = p.matcher( x );
		return m.find() ? m.group(group) : missingValue;
	}
	
	private static int indexMatch(Pattern[] patterns, String x, int notFound) 
	{
		for (int i = 0; i < patterns.length; i++) 
		{
			if (patterns[i].matcher( x ).find()) 
			{
				return i;
			}
		}
		
		return notFound;
	}
	
	public static long parse(JsonValue json)
	{
		switch (json.getType()) 
		{
		case NUMBER:
			return ((JsonNumber)json).get().longValue();
			
		case STRING:
			String x = ((JsonString)json).get().toUpperCase();

			Calendar c = Calendar.getInstance();
			c.set( Calendar.HOUR_OF_DAY, find( HOUR, 2, x, 0 ) + (find( PM, 1, x, null ) != null ? 12 : 0) );
			c.set( Calendar.MINUTE, 	 find( MINUTE, 1, x, 0 ) );
			c.set( Calendar.SECOND,		 find( SECOND, 1, x, 0 ) );
			c.set( Calendar.MILLISECOND, find( MILLIS, 1, x, 0 ) );
			c.set( Calendar.MONTH, indexMatch( MONTHS, x, c.get( Calendar.MONTH ) ) );
			c.set( Calendar.DATE, 		 find( DAY, 2, x, c.get( Calendar.DATE ) ) );
			c.set( Calendar.YEAR, 		 find( YEAR, 2, x, c.get( Calendar.YEAR ) ) );

			String timezone = find( TIMEZONE, 1, x, null );
			if (timezone != null)
			{
				c.setTimeZone( TimeZone.getTimeZone( timezone ) );
			}

			return c.getTimeInMillis();
			
		default:
			throw new RuntimeException( "Invalid date: " + json.getObject().toString() );
		}
	}
	
}
