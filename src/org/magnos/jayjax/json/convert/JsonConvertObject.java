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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonConverters;
import org.magnos.jayjax.json.JsonObject;
import org.magnos.jayjax.json.JsonValue;


public class JsonConvertObject<T> extends JsonConverter<T, JsonObject>
{

    private static final Class<?>[] NO_PARAMETERS = {};
    private static final Object[] NO_ARGUMENTS = {};

    private Constructor<T> constructor;
    private Field[] fields;
    private JsonConverter<Object, JsonValue>[] converters;

    public JsonConvertObject( Class<T> type )
    {
        try
        {
            this.constructor = type.getConstructor( NO_PARAMETERS );
            this.fields = getFields( type );
            this.converters = getConverters( fields, type, this );
        }
        catch (Exception e)
        {
            throw new RuntimeException( e );
        }
    }
    
    @Override
    public T read( JsonObject value )
    {
        T instance = null;

        try
        {
            instance = constructor.newInstance( NO_ARGUMENTS );

            for (int i = 0; i < fields.length; i++)
            {
                JsonValue v = value.getValue( fields[i].getName() );

                if (v != null)
                {
                    fields[i].set( instance, converters[i].read( v ) );
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException( e );
        }

        return instance;
    }

    @Override
    public JsonObject write( T value )
    {
        JsonObject obj = new JsonObject();

        try
        {
            for (int i = 0; i < fields.length; i++)
            {
                Object v = fields[i].get( value );

                if (v != null)
                {
                    obj.set( fields[i].getName(), converters[i].write( v ) );
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException( e );
        }

        return obj;
    }

    private static Field[] getFields( Class<?> clazz )
    {
        List<Field> fieldList = new ArrayList<Field>();

        while (clazz != Object.class)
        {
            for (Field f : clazz.getDeclaredFields())
            {
                if ((f.getModifiers() & (Modifier.FINAL | Modifier.STATIC | Modifier.TRANSIENT)) == 0)
                {
                    f.setAccessible( true );
                    
                    fieldList.add( f );
                }
            }

            clazz = clazz.getSuperclass();
        }

        return fieldList.toArray( new Field[fieldList.size()] );
    }

    @SuppressWarnings ("rawtypes" )
    private static JsonConverter<Object, JsonValue>[] getConverters( Field[] fields, Class<?> thisClass, JsonConverter thisConverter )
    {
        JsonConverter[] converters = new JsonConverter[fields.length];

        for (int i = 0; i < fields.length; i++)
        {
            Class<?> type = fields[i].getType();
            
            if (type == thisClass)
            {
                converters[i] = thisConverter;
            }
            else
            {
                converters[i] = JsonConverters.getConverter( type );
            }
        }
        
        return converters;
    }

}
