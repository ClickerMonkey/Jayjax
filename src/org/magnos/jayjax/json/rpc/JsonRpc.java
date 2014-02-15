package org.magnos.jayjax.json.rpc;

import java.util.regex.Matcher;

import org.magnos.jayjax.Function;
import org.magnos.jayjax.Invocation;
import org.magnos.jayjax.Jayjax;
import org.magnos.jayjax.json.JsonArray;
import org.magnos.jayjax.json.JsonNull;
import org.magnos.jayjax.json.JsonNumber;
import org.magnos.jayjax.json.JsonObject;
import org.magnos.jayjax.json.JsonString;
import org.magnos.jayjax.json.JsonType;
import org.magnos.jayjax.json.JsonValue;

public class JsonRpc
{

	public static final int ERROR_PARSE_ERROR = -32700;
	public static final int ERROR_INVALID_REQUEST = -32600;
	public static final int ERROR_METHOD_NOT_FOUND = -32601;
	public static final int ERROR_INVALID_PARAMS = -32602;
	public static final int ERROR_INTERNAL_ERROR = -32603;
	public static final int ERROR_SERVER_ERROR = -32000;
	
	public static final int VERSION_10 = 1;
	public static final int VERSION_11 = 2;
	public static final int VERSION_20 = 3;
	
	public static JsonValue handleRequest(JsonValue json)
	{
		switch (json.getType()) 
		{
		case OBJECT:
			return handleSingleRequest( (JsonObject)json );
			
		case ARRAY:
			JsonArray out = new JsonArray();
			JsonArray in = (JsonArray)json;
			for (JsonValue v : in.getObject()) {
				if (v.getType() == JsonType.OBJECT) {
					out.add( handleSingleRequest( (JsonObject)v ) );
				} else {
					out.add( createError( VERSION_20, ERROR_INVALID_REQUEST, "Invalid Request", null ) );
				}
			}
			return out;
			
		default:
			return createError( VERSION_20, ERROR_INVALID_REQUEST, "Invalid Request", null );
		}
	}
	
	public static JsonObject handleSingleRequest(JsonObject request)
	{
		int version = getVersion( request );
		Long identifier = getIdentifier( request );
		
		JsonObject response = new JsonObject();
		
		setVersion( response, version );
		
		if (!validate( request, "method", JsonType.STRING )) 
		{
			return createError( version, ERROR_INVALID_REQUEST, "Invalid Method Typpe", identifier );
		}
		
		String method = request.get( "method", String.class );
		
		Function function = Jayjax.getFunction( method );
		
		if (function == null)
		{
			return createError( version, ERROR_METHOD_NOT_FOUND, "Method Not Found", identifier );
		}
		
		Matcher matcher = function.getAction().matcher( method );
		
		if (!matcher.matches())
		{
			return createError( version, ERROR_METHOD_NOT_FOUND, "Method Not Found", identifier );
		}
		
		Invocation invocation = Jayjax.newInvocation();
		invocation.setFunction( function );
		invocation.setMatcher( matcher );
		
		return response;
	}
	
	public static JsonObject createError(int version, int errorCode, String message, Long id)
	{
		JsonObject json = new JsonObject();
		
		setVersion( json, version );
		
		JsonObject error = new JsonObject();
		error.set( "code", new JsonNumber( errorCode ) );
		error.set( "message", new JsonString( message ) );
		json.set( "error", error );
		
		if (id == null) {
			json.set( "id", JsonNull.INSTANCE );
		} else {
			json.set( "id", new JsonNumber( id ) );
		}
		
		return json;
	}
	
	private static int getVersion(JsonObject json)
	{
		if (validate( json, "version", JsonType.STRING )) {
			return VERSION_11;
		} else if (validate( json, "jsonrpc", JsonType.STRING )) {
			return VERSION_20;
		} else {
			return VERSION_10;
		}
	}
	
	private static Long getIdentifier(JsonObject json)
	{
		JsonValue value = json.getValue( "id" );
		
		if (value == null  || value.getType() != JsonType.NUMBER)
		{
			return null;
		}
		
		return ((JsonNumber)value).get().longValue();
	}
	
	private static void setVersion(JsonObject json, int version)
	{
		switch (version) {
		case VERSION_10:
			break;
		case VERSION_11:
			json.set( "version", new JsonString( "1.1" ) );
			break; 
		case VERSION_20:
			json.set( "jsonrpc", new JsonString( "2.0" ) );
			break;
		}
	}
	
	private static boolean validate(JsonObject json, String parameter, JsonType expectedType)
	{
		return json.has( parameter ) && json.getType( parameter ) == expectedType;
	}
	
}
