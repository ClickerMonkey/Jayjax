
package org.magnos.jayjax.json;

public abstract class JsonConverter<T, JV extends JsonValue>
{

    public T missing()
    {
        return null;
    }

    public T empty()
    {
        return null;
    }

    public abstract T read( JV value );

    public abstract JV write( T value );
}
