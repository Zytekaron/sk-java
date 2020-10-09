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

package com.zytekaron.sk.types.object;

import com.zytekaron.sk.struct.TypeConverter;
import com.zytekaron.sk.types.SkObject;
import com.zytekaron.sk.types.SkValue;
import com.zytekaron.sk.types.primitive.SkBool;
import com.zytekaron.sk.types.primitive.SkDouble;
import com.zytekaron.sk.types.primitive.SkInt;
import lombok.Getter;

@Getter
public class SkString extends SkObject {
    private final String value;
    
    public SkString(String value) {
        super();
        this.value = value;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T into(Class<T> clazz) {
        if (SkString.class.equals(clazz)) {
            return (T) this;
        } else if (SkInt.class.equals(clazz)) {
            return (T) TypeConverter.stringToInt(value); // todo implement
        } else if (SkDouble.class.equals(clazz)) {
            return (T) TypeConverter.stringToFloat(value); // todo implement
        } else if (SkBool.class.equals(clazz)) {
            return (T) SkBool.fromString(value);
        }
        throw new RuntimeException("Class conversion not defined for type " + clazz.getSimpleName());
    }
    
    @Override
    protected int compare(SkValue other) {
        return 0;
    }
    
    public SkString add(SkValue other) {
        return new SkString(value + other.toString());
    }
    
    public SkString multiply(SkValue input) {
        if (input instanceof SkInt) {
            int amount = ((SkInt) input).getValue();
            String result = value.repeat(amount);
            return new SkString(result);
        }
        if (input instanceof SkDouble) {
            double amount = ((SkDouble) input).getValue();
            String result = value.repeat((int) amount);
            return new SkString(result);
        }
        return null;
    }
    
    public SkString add(String other) {
        return new SkString(value + other);
    }
    
    public SkString multiply(int amount) {
        return new SkString(value.repeat(amount));
    }
    
    @Override
    public String toString() {
        return value;
    }
}