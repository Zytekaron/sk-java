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

import com.zytekaron.sk.struct.Utils;
import lombok.Getter;

@Getter
public class SkInt extends SkNumber {
    private final int value;
    
    public SkInt(int value) {
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
        }
        throw new RuntimeException("Class conversion not defined for type " + clazz.getSimpleName());
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
    
    
    
    public SkInt add(int other) {
        return new SkInt(value + other);
    }
    
    public SkLong add(long other) {
        return new SkLong(value + other);
    }
    
    public SkDouble add(double other) {
        return new SkDouble(value + other);
    }
    
    public SkInt subtract(int other) {
        return new SkInt(value - other);
    }
    
    public SkLong subtract(long other) {
        return new SkLong(value - other);
    }
    
    public SkDouble subtract(double other) {
        return new SkDouble(value - other);
    }
    
    public SkInt multiply(int other) {
        return new SkInt(value * other);
    }
    
    public SkLong multiply(long other) {
        return new SkLong(value * other);
    }
    
    public SkDouble multiply(double other) {
        return new SkDouble(value * other);
    }
    
    public SkInt divide(int other) {
        return new SkInt(value / other);
    }
    
    public SkLong divide(long other) {
        return new SkLong(value / other);
    }
    
    public SkDouble divide(double other) {
        return new SkDouble(value / other);
    }
    
    public SkInt modulo(int other) {
        return new SkInt(value % other);
    }
    
    public SkLong modulo(long other) {
        return new SkLong(value % other);
    }
    
    public SkDouble modulo(double other) {
        return new SkDouble(value % other);
    }
    
    public SkInt power(int other) {
        return new SkInt((int) Math.pow(value, other));
    }
    
    public SkLong power(long other) {
        return new SkLong((long) Math.pow(value, other));
    }
    
    public SkDouble power(double other) {
        return new SkDouble(Math.pow(value, other));
    }
    
    
    
    @Override
    public String toString() {
        return Integer.toString(value);
    }
}