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

import com.zytekaron.sk.struct.Token;
import com.zytekaron.sk.struct.TokenType;

import static com.zytekaron.sk.struct.TokenType.*;

public abstract class SkNumber extends SkValue {
    
    public SkNumber(String type) {
        super(type);
    }
    
    public abstract SkValue add(SkValue other);
    
    public abstract SkValue subtract(SkValue other);
    
    public abstract SkValue multiply(SkValue other);
    
    public abstract SkValue divide(SkValue other);
    
    public abstract SkValue modulo(SkValue other);
    
    public abstract SkValue power(SkValue other);
    
    public static SkNumber fromString(String string) {
        if (string.contains(".")) {
            double d = Double.parseDouble(string);
            return new SkDouble(d);
        } else if (string.endsWith("L")) {
            long l = Long.parseLong(string);
            return new SkLong(l);
        } else {
            int i = Integer.parseInt(string);
            return new SkInt(i);
        }
    }
    
    public static SkNumber fromToken(Token token) {
        TokenType type = token.getType();
        String value = token.getValue();
        if (type == INT) {
            int i = Integer.parseInt(value);
            return new SkInt(i);
        } else if (type == LONG) {
            long l = Long.parseLong(value);
            return new SkLong(l);
        } else if (type == DOUBLE) {
            double d = Double.parseDouble(value);
            return new SkDouble(d);
        } else {
            return null;
        }
    }
}