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

package com.zytekaron.sk.parse;

import com.zytekaron.sk.parse.nodes.*;
import com.zytekaron.sk.struct.Token;
import com.zytekaron.sk.struct.TokenType;
import com.zytekaron.sk.struct.result.ParseResult;
import com.zytekaron.sk.types.error.SkError;
import com.zytekaron.sk.types.error.SkParsingError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static com.zytekaron.sk.struct.TokenType.*;
import static com.zytekaron.sk.struct.TokenTypes.NUMBERS;
import static com.zytekaron.sk.struct.TokenTypes.UNARY_OPERATORS;

public class Parser {
    private final List<Token> tokens;
    private Token currentToken;
    private int index = -1;
    
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        advance();
    }
    
    private void advance(ParseResult result) {
        result.registerAdvancement();
        advance();
    }
    
    private void advance() {
        index++;
        if (index < tokens.size()) {
            currentToken = tokens.get(index);
        }
    }
    
    public ParseResult parse() {
        if (currentToken.getType() == EOF) {
            return new ParseResult().success(null);
        }
        ParseResult result = primaryExpression();
        if (!result.success()) {
            return result;
        }
        if (currentToken.getType() != EOF) {
            SkError error = new SkParsingError(currentToken, "Unexpected end of input: expected primary expression, found '" + currentToken + "'");
            return result.failure(error);
        }
        return result;
    }
    
    private ParseResult primaryExpression() {
        if (currentToken.getType() == EOF) {
            SkError error = new SkParsingError(currentToken, "Unexpected end of input: expected expression, found '" + currentToken + "'");
            return new ParseResult().failure(error);
        }
        // declarators
        if (isCurrentTokenKeyword("var")) {
            return defineVariable();
        } else if (isCurrentTokenKeyword("fn")) {
            return defineFunction();
        } else {
            return expression();
        }
    }
    
    private ParseResult expression() {
        ParseResult result = new ParseResult();
        
        // <#> check if necessary
        if (currentToken.getType() == EOF) {
            SkError error = new SkParsingError(currentToken, "Unexpected end of input: found '" + currentToken + "'");
            return new ParseResult().failure(error);
        }
        
        ParseResult nodeResult = binaryOperationNode(this::compExpr, List.of(AND, OR));
        
        Node node = result.register(nodeResult);
        if (!result.success()) {
            return result;
        }
        
        if (isCurrentTokenType(SEMICOLON)) {
            advance(result);
            return result.success(node);
        } else {
            SkError error = new SkParsingError(currentToken, "Expected ';' but instead found '" + currentToken + "'");
            return result.failure(error); // <#> is this a problem? it's been registered
        }
    }
    
    private ParseResult compExpr() {
        ParseResult result = new ParseResult();
        
        if (currentToken.getType() == NOT) {
            Token token = currentToken;
            advance(result);
            
            ParseResult expr = compExpr();
            Node compExpr = result.register(expr);
            
            if (!result.success()) {
                return result;
            }
            
            Node node = new UnaryOperationNode(token, compExpr);
            return result.success(node);
        }
        
        return binaryOperationNode(this::arithmetic, List.of(EQ, NEQ, GTR, GEQ, LSS, LEQ));
    }
    
    private ParseResult arithmetic() {
        return binaryOperationNode(this::term, List.of(PLUS, MINUS));
    }
    
    private ParseResult term() {
        return binaryOperationNode(this::factor, List.of(MULTIPLY, DIVIDE, MODULO));
    }
    
    private ParseResult factor() {
        ParseResult result = new ParseResult();
        
        Token token = currentToken;
        TokenType type = token.getType();
        
        if (UNARY_OPERATORS.contains(type)) {
            advance(result);
            
            ParseResult factor = factor();
            Node fact = result.register(factor);
            
            if (!result.success()) {
                return result;
            }
            
            Node node = new UnaryOperationNode(token, fact);
            return result.success(node);
        }
        
        return power();
    }
    
    private ParseResult power() {
        return binaryOperationNode(this::atom, this::factor, List.of(POWER));
    }
    
    private ParseResult atom() {
        ParseResult result = new ParseResult();
        
        Token token = currentToken;
        TokenType type = token.getType();
        
        if (NUMBERS.contains(type)) {
            advance(result);
            Node node = new NumberNode(token);
            return result.success(node);
        }
        if (type == CHAR) {
            advance(result);
            Node node = new CharNode(token);
            return result.success(node);
        }
        if (type == STRING) {
            advance(result);
            Node node = new StringNode(token);
            return result.success(node);
        }
        if (type == IDENTIFIER) { // todo LATER may need to extract this once calls are implemented
            advance(result);
            Node node = new VarAccessNode(token);
            return result.success(node);
        }
        if (type == LPAREN) {
            advance(result);
            
            ParseResult expr = expression();
            Node node = result.register(expr);
            if (!result.success()) {
                return result;
            }
            
            if (currentToken.getType() == RPAREN) {
                advance(result);
                return result.success(node);
            } else {
                SkError error = new SkParsingError(currentToken, "Expected ')' but instead found '" + currentToken + "'");
                return result.failure(error);
            }
        } else {
            SkError error = new SkParsingError(currentToken, "Expected factor? but instead found '" + token + "'");
            return result.failure(error);
        }
    }
    
    private ParseResult defineFunction() {
        ParseResult result = new ParseResult();
    
        if (!isCurrentTokenKeyword("fn")) {
            SkError error = new SkParsingError(currentToken, "Expected 'fn' but instead found '" + currentToken + "'");
            return result.failure(error);
        }
        advance(result);
        
        if (!isCurrentTokenType(IDENTIFIER)) {
            SkError error = new SkParsingError(currentToken, "Expected identifier but instead found '" + currentToken + "'");
            return result.failure(error);
        }
        Token name = currentToken;
        advance(result);
        
        List<Node> params = new ArrayList<>();
        
        if (isCurrentTokenType(LPAREN)) { // allow omitted () for no-param functions
            advance(result); // yeet the LPAREN
            
            if (!isCurrentTokenType(RPAREN)) {
                do { // if there is a comma after any given fn-def-arg
                    advance(result); // yeet the COMMA or RPAREN
                    ParseResult paramResult = functionParameter();
                    Node param = result.register(paramResult);
                    if (!result.success()) {
                        return result;
                    }
                    params.add(param);
                } while (isCurrentTokenType(COMMA));
            }
            
            if (!isCurrentTokenType(RPAREN)) {
                SkError error = new SkParsingError(currentToken, "Expected ')' but instead found '" + currentToken + "'");
                return result.failure(error);
            }
            advance(result);
        }
        
        ParseResult scopeResult = strictScope();
        Node scope = result.register(scopeResult);
        if (!result.success()) {
            return result;
        }
        
        Node func = new FunctionDefineNode(name, params, scope);
        return result.success(func);
    }
    
    private ParseResult functionParameter() {
        ParseResult result = new ParseResult();
        
        boolean spread = false;
        if (isCurrentTokenType(SPREAD)) {
            spread = true;
            advance(result);
        }
        
        if (!isCurrentTokenType(IDENTIFIER)) {
            SkError error = new SkParsingError(currentToken, "Expected identifier, instead found '" + currentToken + "'");
            return result.failure(error);
        }
        Token name = currentToken;
        advance(result);
        
        // implement COLON type specifier here
        
        Node defaultValue = null;
        if (isCurrentTokenType(EQUALS)) {
            advance(result);
            
            ParseResult defaultResult = expression();
            Node value = result.register(defaultResult);
            if (!result.success()) {
                return result;
            }
            defaultValue = value;
            advance(result);
        }
        
        Node param = new FunctionParameterNode(name, defaultValue, spread);
        return result.success(param);
    }
    
    private ParseResult lenientScope() {
        if (isCurrentTokenType(SEMICOLON)) {
            Node node = new ScopeNode(new ArrayList<>());
            return new ParseResult().success(node);
        }
        
        if (isCurrentTokenType(LBRACE)) {
            return strictScope();
        } else {
            ParseResult result = new ParseResult();
            ParseResult expression = expression();
            Node expr = result.register(expression);
            if (!expression.success()) {
                return expression;
            }
            Node node = new ScopeNode(Collections.singletonList(expr));
            return result.success(node);
        }
    }
    
    private ParseResult strictScope() {
        ParseResult result = new ParseResult();
        
        if (!isCurrentTokenType(LBRACE)) {
            SkError error = new SkParsingError(currentToken, "Expected '{' but instead found '" + currentToken + "'");
            return result.failure(error);
        }
        advance(result);
        
        List<Node> expressions = new ArrayList<>();
        
        while (!isCurrentTokenType(RBRACE)) {
            ParseResult expression = primaryExpression();
            Node expr = result.register(expression);
            if (!result.success()) {
                return result;
            }
            
            expressions.add(expr);
            advance(result);
        }
        
        if (!isCurrentTokenType(RBRACE)) {
            SkError error = new SkParsingError(currentToken, "Expected '}' but instead found '" + currentToken + "'");
            return result.failure(error);
        }
        
        Node node = new ScopeNode(expressions);
        return result.success(node);
    }
    
    private ParseResult binaryOperationNode(Supplier<ParseResult> supplier, List<TokenType> types) {
        return binaryOperationNode(supplier, supplier, types);
    }
    
    private ParseResult binaryOperationNode(Supplier<ParseResult> supplier1, Supplier<ParseResult> supplier2, List<TokenType> types) {
        ParseResult result = new ParseResult();
        
        ParseResult leftParseResult = supplier1.get();
        Node left = result.register(leftParseResult);
        
        while (isCurrentTokenIn(types)) {
            Token operation = currentToken;
            
            advance(result);
            
            ParseResult rightResult = supplier2.get();
            Node right = result.register(rightResult);
            
            if (!result.success()) {
                return result;
            }
            
            left = new BinaryOperationNode(left, operation, right);
        }
        
        return result.success(left);
    }
    
    private ParseResult defineVariable() {
        ParseResult result = new ParseResult();
        
        advance(result);
        if (isCurrentTokenType(IDENTIFIER)) {
            SkError error = new SkParsingError(currentToken, "Expected identifier but instead found " + currentToken.getType());
            return result.failure(error);
        }
        
        Token variableName = currentToken;
        advance(result);
        
        if (isCurrentTokenType(EQUALS)) {
            advance(result);
    
            ParseResult expression = expression();
            Node expr = result.register(expression);
    
            Node node = new VarDefineNode(variableName, expr);
            return result.success(node);
        }
        
        Node node = new VarDeclareNode(variableName);
        return result.success(node);
    }
    
    private boolean isCurrentTokenKeyword(String value) {
        if (currentToken == null) {
            return false;
        } else {
            return currentToken.getType() == IDENTIFIER && currentToken.getValue().equals(value);
        }
    }
    
    private boolean isCurrentTokenType(TokenType type) {
        if (currentToken == null) {
            return false;
        } else {
            return currentToken.getType() == type;
        }
    }
    
    private boolean isCurrentTokenIn(List<TokenType> types) {
        if (currentToken == null) {
            return false;
        } else {
            return types.contains(currentToken.getType());
        }
    }
}