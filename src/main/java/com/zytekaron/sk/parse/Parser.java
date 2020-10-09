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
import com.zytekaron.sk.struct.Position;
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
        advance(null);
    }
    
    private void advance(ParseResult result) {
        if (result != null) {
            result.registerAdvancement();
        }
        index++;
        if (index < tokens.size()) {
            currentToken = tokens.get(index);
        }
    }
    
    public ParseResult parse() {
        if (currentToken.getType() == EOF) {
            return new ParseResult().success(null);
        }
        ParseResult result;
        do {
            result = primaryExpression();
            if (!result.success()) {
                return result;
            }
        } while (!isCurrentTokenType(EOF));
        return result;
    }
    
    private ParseResult primaryExpression() {
        if (currentToken.getType() == EOF) {
            SkError error = new SkParsingError(currentToken, "Unexpected end of input: expected expression, found '" + currentToken + "'");
            return new ParseResult().failure(error);
        }
        
        ParseResult result = new ParseResult();
        
        Node node;
        if (isCurrentTokenKeyword("fn")) {
            ParseResult res = defineFunction();
            node = result.register(res);
            if (!result.success()) {
                return result;
            }
        } else if (isCurrentTokenKeyword("var")) {
            ParseResult res = defineVariable();
            node = result.register(res);
            if (!result.success()) {
                return result;
            }
        } else if (isCurrentTokenType(LBRACE)) {
            ParseResult res = scope();
            node = result.register(res);
            if (!result.success()) {
                return result;
            }
        } else {
            ParseResult res = expression();
            node = result.register(res);
            if (!result.success()) {
                return result;
            }
        }
        
        if (!isCurrentTokenType(SEMICOLON)) {
            SkError error = new SkParsingError(currentToken, "Expected ';' but instead found '" + currentToken + "'");
            return result.failure(error);
        }
        advance(result);
        return result.success(node);
//        } else {
//            SkError error = new SkParsingError(currentToken, "Expected ';' but instead found '" + currentToken + "'");
//            return result.failure(error); // <#> is this a problem? it's been registered
//        }
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
        
        return result.success(node);
    }
    
    private ParseResult compExpr() {
        ParseResult result = new ParseResult();
        
        if (isCurrentTokenType(NOT)) {
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
        
        if (NUMBERS.contains(currentToken.getType())) {
            advance(result);
            Node node = new NumberNode(token);
            return result.success(node);
        }
        if (isCurrentTokenType(CHAR)) {
            advance(result);
            Node node = new CharNode(token);
            return result.success(node);
        }
        if (isCurrentTokenType(STRING)) {
            advance(result);
            Node node = new StringNode(token);
            return result.success(node);
        }
        if (isCurrentTokenType(IDENTIFIER)) {
            Token identifier = currentToken;
            advance(result);
    
            if (isCurrentTokenType(EQUALS)) {
                advance(result);
        
                ParseResult expression = expression();
                Node expr = result.register(expression);
                if (!result.success()) {
                    return result;
                }
        
                Node node = new VarReassignNode(identifier, expr);
                return result.success(node);
            }
    
            if (isCurrentTokenType(LBRACKET)) {
                ParseResult elementResult = expression();
                Node element = result.register(elementResult);
                if (!result.success()) {
                    return result;
                }
                
                if (!isCurrentTokenType(RBRACKET)) {
                    SkError error = new SkParsingError(currentToken, "Expected ']' but instead found '" + currentToken + "'");
                    return result.failure(error);
                }
                advance(result);
                
                Node node = new ElementAccessNode(identifier, element);
                return result.success(node);
            }
    
            if (isCurrentTokenType(LPAREN)) {
                List<Node> params = new ArrayList<>();
    
                while (!isCurrentTokenType(RPAREN)) {
                    advance(result);
                    if (isCurrentTokenType(RPAREN)) {
                        break;
                    }
                    
                    ParseResult paramResult = expression();
                    Node param = result.register(paramResult);
                    if (!result.success()) {
                        return result;
                    }
                    params.add(param);
                }
    
                if (!isCurrentTokenType(RPAREN)) {
                    SkError error = new SkParsingError(currentToken, "Expected ')' but instead found '" + currentToken + "'");
                    return result.failure(error);
                }
                advance(result);
    
                Node node = new FunctionCallNode(identifier, params);
                return result.success(node);
            }
            
            Node node = new VarAccessNode(token);
            return result.success(node);
        }
        if (isCurrentTokenType(LPAREN)) {
            advance(result);
            
            ParseResult expr = expression();
            Node node = result.register(expr);
            if (!result.success()) {
                return result;
            }
            
            if (isCurrentTokenType(RPAREN)) {
                advance(result);
                return result.success(node);
            } else {
                SkError error = new SkParsingError(currentToken, "Expected ')' but instead found '" + currentToken + "'");
                return result.failure(error);
            }
        }
        if (isCurrentTokenType(LBRACKET)) {
            ParseResult arrayResult = arrayLiteral();
            Node array = result.register(arrayResult);
            if (!result.success()) {
                return result;
            }
            return result.success(array);
        }
        
        SkError error = new SkParsingError(currentToken, "Expected factor? but instead found '" + token + "'");
        return result.failure(error);
    }
    
    private ParseResult arrayLiteral() {
        ParseResult result = new ParseResult();
        
        if (!isCurrentTokenType(LBRACKET)) {
            SkError error = new SkParsingError(currentToken, "Expected '[' but instead found '" + currentToken + "'");
            return result.failure(error);
        }
        Position start = currentToken.getStart().copy();
        
        List<Node> nodes = new ArrayList<>();
    
        while (!isCurrentTokenType(RBRACKET)) {
            advance(result);
            if (isCurrentTokenType(RBRACKET)) {
                break;
            }
    
            ParseResult nodeResult = expression();
            Node node = result.register(nodeResult);
            if (!result.success()) {
                return result;
            }
            nodes.add(node);
        }
    
        if (!isCurrentTokenType(RBRACKET)) {
            SkError error = new SkParsingError(currentToken, "Expected ']' but instead found '" + currentToken + "'");
            return result.failure(error);
        }
        Position end = currentToken.getEnd().copy();
        advance(result);
        
        Node node = new ArrayNode(start, end, nodes);
        return result.success(node);
    }
    
    private ParseResult defineVariable() {
        ParseResult result = new ParseResult();
        
        advance(result);
        if (!isCurrentTokenType(IDENTIFIER)) {
            SkError error = new SkParsingError(currentToken, "Expected identifier but instead found " + currentToken.getType());
            return result.failure(error);
        }
        
        Token variableName = currentToken;
        advance(result);
        
        if (isCurrentTokenType(EQUALS)) {
            advance(result);
            
            ParseResult expression = expression();
            Node expr = result.register(expression);
            if (!result.success()) {
                return result;
            }
            
            Node node = new VarAssignNode(variableName, expr);
            return result.success(node);
        }
        
        Node node = new VarAssignNode(variableName, null);
        return result.success(node);
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
        
        if (isCurrentTokenType(LPAREN)) {
            while (!isCurrentTokenType(RPAREN)) {
                advance(result);
                if (isCurrentTokenType(RPAREN)) {
                    break;
                }
                
                ParseResult paramResult = functionParameter();
                Node param = result.register(paramResult);
                if (!result.success()) {
                    return result;
                }
                params.add(param);
            }
            
            if (!isCurrentTokenType(RPAREN)) {
                SkError error = new SkParsingError(currentToken, "Expected ')' but instead found '" + currentToken + "'");
                return result.failure(error);
            }
            advance(result);
        }
        
        if (isCurrentTokenType(SEMICOLON)) {
            advance(result);
            Node func = new FunctionDefineNode(name, params, new ScopeNode(new ArrayList<>()));
            return result.success(func);
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
        
        // todo implement type specifier
        
        Node defaultValue = null;
        if (isCurrentTokenType(EQUALS)) {
            advance(result);
            
            ParseResult defaultResult = expression();
            Node value = result.register(defaultResult);
            if (!result.success()) {
                return result;
            }
            
            defaultValue = value;
        }
        
        Node param = new FunctionParameterNode(name, defaultValue, spread);
        return result.success(param);
    }
    
    private ParseResult scope() {
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
        }
        
        if (!isCurrentTokenType(RBRACE)) {
            SkError error = new SkParsingError(currentToken, "Expected '}' but instead found '" + currentToken + "'");
            return result.failure(error);
        }
        advance(result);
        
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
        if (!result.success()) {
            return result;
        }
        
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
    
    private boolean isCurrentTokenType(TokenType type) {
        if (currentToken == null) {
            return false;
        } else {
            return currentToken.getType() == type;
        }
    }
    
    private boolean isCurrentTokenKeyword(String value) {
        if (currentToken == null || isCurrentTokenType(EOF)) {
            return false;
        } else {
            return currentToken.getType() == KEYWORD && currentToken.getValue().equals(value);
        }
    }
    
    private boolean isCurrentTokenIn(List<TokenType> types) {
        if (currentToken == null || isCurrentTokenType(EOF)) {
            return false;
        } else {
            return types.contains(currentToken.getType());
        }
    }
}