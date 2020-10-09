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

import com.zytekaron.sk.struct.Utils;
import com.zytekaron.sk.types.SkNumber;
import com.zytekaron.sk.types.SkValue;
import com.zytekaron.sk.types.object.SkString;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;

@Getter
public class SkDouble extends SkNumber {
    private final double value;
    private final Map<Class<? extends SkValue>, Function<Double, SkValue>> converter = Map.of(
            SkInt.class, value -> new SkInt(value.intValue()),
            SkLong.class, SkDouble::new,
            SkDouble.class, SkDouble::new,
            SkBool.class, value -> new SkBool(value != 0)
    );
    
    public SkDouble(double value) {
        super("Double");
        this.value = value;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T into(Class<T> clazz) {
        Function<Double, SkValue> function = converter.get(clazz);
        if (function == null) {
            return null;
        }
        return (T) function.apply(value);
    }
    
    @Override
    protected int compare(SkValue other) {
        return 0;
    }
    
    @Override
    public SkValue add(SkValue other) {
        if (other instanceof SkString) {
            return toSkString().add(other);
        }
        return Utils.math(this::add, this::add, this::add, other);
    }
    
    @Override
    public SkValue subtract(SkValue other) {
        return Utils.math(this::subtract, this::subtract, this::subtract, other);
    }
    
    @Override
    public SkValue multiply(SkValue other) {
        return Utils.math(this::multiply, this::multiply, this::multiply, other);
    }
    
    @Override
    public SkValue divide(SkValue other) {
        return Utils.math(this::divide, this::divide, this::divide, other);
    }
    
    @Override
    public SkValue modulo(SkValue other) {
        return Utils.math(this::modulo, this::modulo, this::modulo, other);
    }
    
    @Override
    public SkValue power(SkValue other) {
        return Utils.math(this::power, this::power, this::power, other);
    }
    
    
    
    public SkDouble add(int other) {
        return new SkDouble(value + other);
    }
    
    public SkDouble add(double other) {
        return new SkDouble(value + other);
    }
    
    public SkDouble subtract(int other) {
        return new SkDouble(value - other);
    }
    
    public SkDouble subtract(double other) {
        return new SkDouble(value - other);
    }
    
    public SkDouble multiply(int other) {
        return new SkDouble(value * other);
    }
    
    public SkDouble multiply(double other) {
        return new SkDouble(value * other);
    }
    
    public SkDouble divide(int other) {
        return new SkDouble(value / other);
    }
    
    public SkDouble divide(double other) {
        return new SkDouble(value / other);
    }
    
    public SkDouble modulo(int other) {
        return new SkDouble(value % other);
    }
    
    public SkDouble modulo(double other) {
        return new SkDouble(value % other);
    }
    
    public SkDouble power(int other) {
        return new SkDouble((int) Math.pow(value, other));
    }
    
    public SkDouble power(double other) {
        return new SkDouble(Math.pow(value, other));
    }
    
    @Override
    public String toString() {
        return Double.toString(value);
    }
}