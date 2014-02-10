package org.magnos.jayjax.json;


public interface JsonConverter<T, JV extends JsonValue>
{
    public T read(JV value);
    public JV write(T value);
}
