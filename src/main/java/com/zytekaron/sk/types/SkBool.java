/*
   Copyright 2020 Michael Thornes

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.zytekaron.sk.types;

public class SkBool extends SkValue {
    private final boolean value;
    
    public SkBool(boolean value) {
        super("Boolean");
        this.value = value;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T into(Class<T> clazz) {
        if (SkString.class.equals(clazz)) {
            return (T) toSkString();
        } else if (SkInt.class.equals(clazz)) {
            return (T) new SkInt(value ? 1 : 0);
        } else if (SkLong.class.equals(clazz)) {
            return (T) new SkLong(value ? 1 : 0);
        } else if (SkDouble.class.equals(clazz)) {
            return (T) new SkDouble(value ? 1 : 0);
        } else if (SkBool.class.equals(clazz)) {
            return (T) new SkBool(value);
        }
        throw new RuntimeException("Class conversion not defined for type " + clazz.getSimpleName());
    }
    
    @Override
    protected int compare(SkValue other) {
        return new SkInt(value ? 1 : 0).compare(other);
    }
    
    public static SkBool fromString(String string) {
        boolean b = Boolean.parseBoolean(string);
        return new SkBool(b);
    }
    
    public SkBool and(SkValue other) {
        return and(other.into(SkBool.class));
    }
    
    public SkBool and(SkBool other) {
        return and(other.getValue());
    }
    
    public SkBool or(SkValue other) {
        return or(other.into(SkBool.class));
    }
    
    public SkBool or(SkBool other) {
        return or(other.getValue());
    }
    
    
    
    public SkBool and(boolean other) {
        return new SkBool(value && other);
    }
    
    public SkBool or(boolean other) {
        return new SkBool(value || other);
    }
    
    public SkBool not() {
        return new SkBool(!value);
    }
    
    @Override
    public String toString() {
        return Boolean.toString(value);
    }
    
    public boolean getValue() {
        return value;
    }
}