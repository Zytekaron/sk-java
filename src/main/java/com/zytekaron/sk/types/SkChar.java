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

import lombok.Getter;

@Getter
public class SkChar extends SkValue {
    private final char value;
    
    public SkChar(char value) {
        super("Int");
        this.value = value;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T into(Class<T> clazz) {
        if (SkString.class.equals(clazz)) {
            return (T) toSkString();
        } else if (SkInt.class.equals(clazz)) {
            return (T) new SkInt(value);
        } else if (SkLong.class.equals(clazz)) {
            return (T) new SkLong(value);
        } else if (SkDouble.class.equals(clazz)) {
            return (T) new SkDouble(value);
        } else if (SkBool.class.equals(clazz)) {
            return (T) new SkBool(value != 0);
        } else if (SkChar.class.equals(clazz)) {
            return (T) new SkChar(value);
        }
        throw new RuntimeException("Class conversion not defined for type " + clazz.getSimpleName());
    }
    
    // todo implement & implement into(SkChar.class) for other types
    @Override
    protected int compare(SkValue other) {
        return 0;
    }
    
    @Override
    public String toString() {
        return Character.toString(value);
    }
}