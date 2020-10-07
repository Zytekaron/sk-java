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

package com.zytekaron.sk.lex;

import com.zytekaron.sk.struct.Position;
import com.zytekaron.sk.struct.Token;
import com.zytekaron.sk.struct.TokenType;
import com.zytekaron.sk.types.error.SkError;
import com.zytekaron.sk.types.error.SkLexingError;
import com.zytekaron.sk.struct.result.LexResult;

import java.util.ArrayList;
import java.util.List;

import static com.zytekaron.sk.struct.TokenType.*;

public class Lexer {
    private static final String NUMBERS = "0123456789";
    private static final String IDENTIFIERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_$";
    private static final List<String> KEYWORDS = List.of(
            "int", "long", "float", "double", "string", "bool",
            "var", "const",
            "if", "else", "for", "of", "in", "while", "switch", "case", "return", "break", "continue",
            "mew", "delete"
    );
    private static final List<String> BOOLEANS = List.of("true", "false");
    
    private final String text;
    private final Position pos = new Position();
    private Character currentChar;
    
    public Lexer(String text) {
        this.text = text;
        advance();
    }
    
    private void advance() {
        pos.advance(currentChar);
        if (pos.getIndex() < text.length()) {
            currentChar = text.charAt(pos.getIndex());
        } else {
            currentChar = null;
        }
    }
    
    public LexResult<List<Token>> tokenize() {
        LexResult<List<Token>> result = new LexResult<>();
        
        List<Token> tokens = new ArrayList<>();
        
        Token token;
        LexResult<Token> res;
        while (currentChar != null) {
            token = null;
            res = null;
            
            if (currentChar == ' ' || currentChar == '\t') {
                advance();
                continue;
            } else if (currentChar == '"') {
                res = createString();
            } else if (currentChar == '+') {
                res = createSimpleToken(PLUS, new char[] { '=', '+' }, new TokenType[] { PLUS_EQUALS, INCREMENT });
            } else if (currentChar == '-') {
                res = createSimpleToken(MINUS, new char[] { '=', '-', '>' }, new TokenType[] { MINUS_EQUALS, DECREMENT, ARROW });
            } else if (currentChar == '*') {
                res = createSimpleToken(MULTIPLY, new char[] { '=', '*' }, new TokenType[] { TIMES_EQUALS, POWER });
            } else if (currentChar == '/') {
                Position start = pos.copy();
                advance();
                if (currentChar == '/') {
                    while (currentChar != null && !currentChar.equals('\n')) {
                        advance();
                    }
                    advance();
                } else if (currentChar == '=') {
                    token = new Token(DIVIDE_EQUALS, start, pos);
                    advance();
                } else {
                    token = new Token(DIVIDE, pos);
                }
            } else if (currentChar == '=') {
                token = new Token(EQUALS, pos);
                advance();
            } else if (currentChar == '.') {
                Position start = pos.copy();
                advance();
                if (currentChar == '.') {
                    advance();
                    if (currentChar == '.') {
                        token = new Token(SPREAD, start, pos);
                    }
                } else {
                    token = new Token(DOT, pos);
                }
            } else if (currentChar == ':') {
                token = new Token(COLON, pos);
                advance();
            } else if (currentChar == ';') {
                token = new Token(SEMICOLON, pos);
                advance();
            } else if (currentChar == '[') {
                token = new Token(LBRACKET, pos);
                advance();
            } else if (currentChar == ']') {
                token = new Token(RBRACKET, pos);
                advance();
            } else if (currentChar == '{') {
                token = new Token(LBRACE, pos);
                advance();
            } else if (currentChar == '}') {
                token = new Token(RBRACE, pos);
                advance();
            } else if (currentChar == ',') {
                token = new Token(COMMA, pos);
                advance();
            } else if (currentChar == '&') {
                res = createDualToken(BIT_AND, '&', AND);
            } else if (currentChar == '|') {
                res = createDualToken(BIT_OR, '|', OR);
            } else if (currentChar == '!') {
                res = createDualToken(NOT, '=', NEQ);
            } else if (currentChar == '>') {
                res = createDualToken(GTR, '=', GEQ);
            } else if (currentChar == '<') {
                res = createDualToken(LSS, '=', LEQ);
            } else if (currentChar == '(') {
                token = new Token(LPAREN, pos);
                advance();
            } else if (currentChar == ')') {
                token = new Token(RPAREN, pos);
                advance();
            } else if (currentChar == '~') {
                token = new Token(BIT_NOT, pos);
                advance();
            } else if (NUMBERS.indexOf(currentChar) != -1) {
                res = createNumber();
            } else if (IDENTIFIERS.indexOf(currentChar) != -1) {
                res = createIdentifier();
            } else {
                Position start = pos.copy();
                SkError error = new SkLexingError(start, pos, "Unexpected character '" + currentChar + "'");
                return result.failure(error);
            }
            if (token != null) {
                tokens.add(token);
            } else if (res != null) {
                if (res.success()) {
                    tokens.add(res.getResult());
                } else {
                    return result.failure(res.getError());
                }
            }
        }
        
        Token eof = new Token(EOF, pos);
        tokens.add(eof);
        return result.success(tokens);
    }
    
    private LexResult<Token> createDualToken(TokenType withoutType, char nextChar, TokenType withType) {
        return createSimpleToken(withoutType, new char[] { nextChar }, new TokenType[] { withType });
    }
    
    private LexResult<Token> createSimpleToken(TokenType withoutType, char[] chars, TokenType[] types) {
        LexResult<Token> result = new LexResult<>();
        Position start = pos.copy();
        
        advance();
        if (currentChar == null) { // this is assuming all `withoutType` chars CANNOT be the last char
            SkError error = new SkLexingError(pos.copy(), pos, "Unexpected character '" + currentChar + "'");
            return result.failure(error);
        }
        
        for (int i = 0; i < chars.length; i++) {
            if (currentChar.equals(chars[i])) {
                Token token = new Token(types[i], start, pos);
                advance();
                return result.success(token);
            }
        }
        
        Token token = new Token(withoutType, start, pos);
        return result.success(token);
    }
    
    private LexResult<Token> createNumber() {
        LexResult<Token> result = new LexResult<>();
        
        StringBuilder number = new StringBuilder();
        Position start = pos.copy();
        boolean decimal = false;
        
        while (currentChar != null && (NUMBERS + '.').indexOf(currentChar) != -1) {
            if (currentChar.equals('.')) {
                if (decimal) break;
                decimal = true;
                number.append('.');
            } else {
                number.append(currentChar);
            }
            advance();
        }
        
        if (currentChar != null && currentChar == 'L') {
            advance();
            Token token = new Token(LONG, start, pos, number.toString());
            return result.success(token);
        }
        
        Token token;
        if (decimal) {
            token = new Token(DOUBLE, start, pos, number.toString());
        } else {
            token = new Token(INT, start, pos, number.toString());
        }
        return result.success(token);
    }
    
    private LexResult<Token> createIdentifier() { // identifier, keyword, bool
        LexResult<Token> result = new LexResult<>();
        
        StringBuilder identifier = new StringBuilder();
        Position start = pos.copy();
        
        while (currentChar != null && (IDENTIFIERS + NUMBERS).indexOf(currentChar) != -1) {
            identifier.append(currentChar);
            advance();
        }
        
        String id = identifier.toString();
        Token token;
        if (KEYWORDS.contains(id)) {
            token = new Token(KEYWORD, start, pos, id);
        } else if (BOOLEANS.contains(id)) {
            token = new Token(BOOL, start, pos, id);
        } else {
            token = new Token(IDENTIFIER, start, pos, id);
        }
        return result.success(token);
    }
    
    private LexResult<Token> createString() {
        LexResult<Token> result = new LexResult<>();
        
        StringBuilder string = new StringBuilder();
        Position start = pos.copy();
        advance(); // left quote
        
        while (currentChar != null) {
            if (currentChar == '\\') {
                advance();
                if (currentChar == null) {
                    SkError error = new SkLexingError(pos.copy(), pos, "Unexpected end of input");
                    return result.failure(error);
                } else if (currentChar.equals('n')) {
                    string.append('\n');
                } else if (currentChar.equals('t')) {
                    string.append('\t');
                } else if (currentChar.equals('"')) {
                    string.append('"');
                } else if (currentChar.equals('`')) {
                    string.append('`');
                } else if (currentChar.equals('\'')) {
                    string.append('\'');
                } else if (currentChar.equals('u')) {
                    // todo implement characters (new String(Character.toChars()))
                    for (int i = 0; i < 4; i++) {
                        advance();
                        
                    }
                    string.append("<?>");
                } else {
                    SkError error = new SkLexingError(pos.copy(), pos, "Invalid escape sequence: \\" + currentChar);
                    return result.failure(error);
                }
            } else if (currentChar == '"') {
                break;
            } else {
                string.append(currentChar);
            }
            advance();
        }
        
        advance(); // right quote
        Token token = new Token(STRING, start, pos, string.toString());
        return result.success(token);
    }
}