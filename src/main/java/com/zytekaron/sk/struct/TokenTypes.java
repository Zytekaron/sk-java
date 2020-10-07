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

import java.util.List;

import static com.zytekaron.sk.struct.TokenType.*;

public class TokenTypes {
    public static final List<TokenType> NUMBERS = List.of(INT, LONG, DOUBLE);
    
    public static final List<TokenType> OPERATORS = List.of(PLUS, MINUS, MULTIPLY, DIVIDE, MODULO);
    public static final List<TokenType> POWER_OPERATOR = List.of(POWER);
    public static final List<TokenType> UNARY_OPERATORS = List.of(PLUS, MINUS, NOT);
    public static final List<TokenType> BINARY_OPERATORS = List.of(MULTIPLY, DIVIDE, MODULO);
    
    public static final List<TokenType> SYMBOLS = List.of(LPAREN, RPAREN);
}