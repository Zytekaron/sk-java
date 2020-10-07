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

package com.zytekaron.sk.struct;

import lombok.Getter;

@Getter
public class Token {
    private final TokenType type;
    private final Position start;
    private final Position end;
    private final String value;
    
    public Token(TokenType type, Position start, String value) {
        this.type = type;
        this.start = start;
        this.value = value;
        
        Position end = start.copy();
        end.advance(null);
        this.end = end;
    }
    
    public Token(TokenType type, Position start, Position end, String value) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.value = value;
    }
    
    public Token(TokenType type, Position start) {
        this(type, start, (String) null);
    }
    
    public Token(TokenType type, Position start, Position end) {
        this(type, start, end, null);
    }
    
    @Override
    public String toString() {
        if (TokenTypes.OPERATORS.contains(type) || TokenTypes.SYMBOLS.contains(type)) {
            return type.toString();
        } else {
            return value == null ? type.toString() : type.toString() + '(' + value + ')';
        }
    }
}