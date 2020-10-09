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

package com.zytekaron.sk.types.primitive;

import com.zytekaron.sk.types.SkValue;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;

@Getter
public class SkChar extends SkValue {
    private final char value;
    private final Map<Class<? extends SkValue>, Function<Character, SkValue>> converter = Map.of(
            SkInt.class, SkInt::new,
            SkLong.class, SkDouble::new,
            SkDouble.class, SkDouble::new,
            SkBool.class, value -> new SkBool(value != 0),
            SkChar.class, SkChar::new
    );
    
    public SkChar(char value) {
        super("Int");
        this.value = value;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T into(Class<T> clazz) {
        Function<Character, SkValue> function = converter.get(clazz);
        if (function == null) {
            return null;
        }
        return (T) function.apply(value);
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