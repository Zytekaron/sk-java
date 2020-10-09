package com.zytekaron.sk.types;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SkObject extends SkValue {
    private final Map<String, SkValue> values;
    private final SkClass instanceOf;
    
    public SkObject() {
        this(null, new HashMap<>());
    }
    
    public SkObject(SkClass instanceOf) {
        this(instanceOf, new HashMap<>());
    }
    
    public SkObject(Map<String, SkValue> values) {
        this(null, values);
    }
    
    public SkObject(SkClass instanceOf, Map<String, SkValue> values) {
        super(instanceOf == null ? "Object" : instanceOf.getName());
        this.instanceOf = instanceOf;
        this.values = values;
    }
    
    public SkValue get(String key) {
        return values.computeIfAbsent(key, __ -> new SkNull());
    }
    
    public SkValue put(String key, SkValue value) {
        return values.put(key, value);
    }
    
    public SkValue find(String name) {
        return values.computeIfAbsent(name, __ -> instanceOf == null ? new SkNull() : instanceOf.getMethod(name));
    }
    
    @Override
    protected int compare(SkValue other) {
        if (other instanceof SkObject) {
            SkObject obj = (SkObject) other;
            return values.equals(obj.values) ? 0 : 1;
        }
        return 1;
    }
    
    @Override
    public <T> T into(Class<T> clazz) {
        return null;
    }
    
    @Override
    public String toString() {
        return ""; // todo implement Object#toString base
    }
}