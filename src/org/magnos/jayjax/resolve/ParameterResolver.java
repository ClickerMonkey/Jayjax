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

package org.magnos.jayjax.resolve;

import java.io.IOException;

import javax.servlet.ServletException;

import org.magnos.jayjax.ArgumentResolver;
import org.magnos.jayjax.Invocation;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonValue;
import org.magnos.jayjax.json.convert.JsonConverterFactory;


public class ParameterResolver extends ArgumentResolver
{

    protected final JsonConverter<Object, JsonValue> converter;

    public ParameterResolver( String name, Class<?> type )
    {
        super( name, type );

        this.converter = JsonConverterFactory.getConverter( (Class<Object>)type );
    }

    @Override
    public Object getArgument( Invocation invocation ) throws IOException, ServletException
    {
        JsonValue json = invocation.getParameters().getValue( name );

        if (json == null)
        {
            return converter.missing();
        }

        switch (json.getType())
        {
        case NULL:
            return null;
        case EMPTY:
            return converter.empty();
        default:
            return converter.read( json );
        }
    }

}
