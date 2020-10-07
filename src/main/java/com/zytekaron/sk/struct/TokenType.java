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

public enum TokenType {
// Primitive types
    INT("Int"),
    LONG("Long"),
    DOUBLE("Double"),
    CHAR("Char"),
    STRING("String"),
    BOOL("Bool"),
    
// Identifiers
    IDENTIFIER("Identifier"),
    KEYWORD("Keyword"),
    
// Dual Binary operators
    // Arithmetic
    PLUS("+"),
    MINUS("-"),
    
// Operators
    // Arithmetic
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%"),
    POWER("**"),
    // Logical
    AND("&&"),
    OR("||"),
    NOT("!"),
    // Comparison
    EQ("=="),
    NEQ("!="),
    LSS("<"),
    GTR(">"),
    LEQ("<="),
    GEQ(">="),
    // Bitwise
    BIT_AND("&"),
    BIT_OR("|"),
    BIT_NOT("~"),
    
// Symbols
    // Variables
    EQUALS("="),
    MINUS_EQUALS("-="),
    DECREMENT("--"),
    
    // Arithmetic
    PLUS_EQUALS("+="),
    INCREMENT("++"),
    TIMES_EQUALS("*="),
    DIVIDE_EQUALS("/="),
    
    // Grouping
    LPAREN("("),
    RPAREN(")"),
    LBRACKET("["),
    RBRACKET("]"),
    LBRACE("{"),
    RBRACE("}"),
    
    // Other
    DOT("."),
    COMMA(","),
    ARROW("->"),
    COLON(":"),
    SPREAD("..."),
    SEMICOLON(";"),
    
// Parsing
    EOF("EOF");
    
    private final String name;
    
    TokenType(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}