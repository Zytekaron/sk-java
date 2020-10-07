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

package com.zytekaron.sk.interpret.handlers;

import com.zytekaron.sk.interpret.Interpreter;
import com.zytekaron.sk.parse.nodes.BinaryOperationNode;
import com.zytekaron.sk.parse.nodes.Node;
import com.zytekaron.sk.struct.Context;
import com.zytekaron.sk.struct.Token;
import com.zytekaron.sk.struct.TokenType;
import com.zytekaron.sk.types.SkBool;
import com.zytekaron.sk.types.SkNumber;
import com.zytekaron.sk.types.SkString;
import com.zytekaron.sk.types.SkValue;
import com.zytekaron.sk.types.error.SkError;
import com.zytekaron.sk.types.error.SkRuntimeError;

public class BinaryOperationHandler implements Handler {
    private final Interpreter interpreter;
    
    public BinaryOperationHandler(Interpreter interpreter) {
        this.interpreter = interpreter;
    }
    
    @Override
    public SkValue handle(Node node, Context context) {
        return handle((BinaryOperationNode) node, context);
    }
    
    private SkValue handle(BinaryOperationNode node, Context context) {
        SkValue left = interpreter.visit(node.getLeftOperand(), context);
        if (left instanceof SkError) {
            return left;
        }
        
        SkValue right = interpreter.visit(node.getRightOperand(), context);
        if (right instanceof SkError) {
            return right;
        }
        
        Token operation = node.getOperation();
        TokenType operationType = operation.getType();
        
        try {
            SkValue value = execute(left, operationType, right, node, context);
            if (value == null) {
                return new SkRuntimeError(node, context, "An error occurred whilst performing this operation.");
            }
            return value;
        } catch (ArithmeticException e) {
            return new SkRuntimeError(node, context, "An error occurred whilst performing this operation.");
        }
    }
    
    private SkValue execute(SkValue left, TokenType operationType, SkValue right, BinaryOperationNode node, Context context) {
        switch (operationType) {
            case PLUS:
                return add(left, right);
            case MINUS:
                return subtract(left, right, node, context);
            case MULTIPLY:
                return multiply(left, right, node, context);
            case DIVIDE:
                return divide(left, right, node, context);
            case MODULO:
                return modulo(left, right, node, context);
            case POWER:
                return power(left, right, node, context);
            default:
                throw new RuntimeException("Invalid operation passed: " + operationType);
        }
    }
    
    private SkValue add(SkValue left, SkValue right) {
        if (left instanceof SkNumber) {
            SkNumber number = (SkNumber) left;
            return number.add(right);
        }
        if (left instanceof SkString) {
            SkString string = (SkString) left;
            return string.add(right);
        }
        // todo []+=?
        return left.toSkString().add(right);
    }
    
    private SkValue subtract(SkValue left, SkValue right, BinaryOperationNode node, Context context) {
        if (left instanceof SkNumber) {
            SkNumber number = (SkNumber) left;
            return number.subtract(right);
        }
        return new SkRuntimeError(node, context, "Invalid operand type '" + left.getType() + "' used in - expression");
    }
    
    private SkValue multiply(SkValue left, SkValue right, BinaryOperationNode node, Context context) {
        if (left instanceof SkNumber) {
            SkNumber number = (SkNumber) left;
            return number.multiply(right);
        }
        if (left instanceof SkString) {
            SkString string = (SkString) left;
            return string.multiply(right);
        }
        return new SkRuntimeError(node, context, "Invalid operand type '" + left.getType() + "' used in * expression");
    }
    
    private SkValue divide(SkValue left, SkValue right, BinaryOperationNode node, Context context) {
        if (left instanceof SkNumber) {
            SkNumber number = (SkNumber) left;
            return number.divide(right);
        }
        return new SkRuntimeError(node, context, "Invalid operand type '" + left.getType() + "' used in / expression");
    }
    
    private SkValue modulo(SkValue left, SkValue right, BinaryOperationNode node, Context context) {
        if (left instanceof SkNumber) {
            SkNumber number = (SkNumber) left;
            return number.modulo(right);
        }
        return new SkRuntimeError(node, context, "Invalid operand type '" + left.getType() + "' used in % expression");
    }
    
    private SkValue power(SkValue left, SkValue right, BinaryOperationNode node, Context context) {
        if (left instanceof SkNumber) {
            SkNumber number = (SkNumber) left;
            return number.power(right);
        }
        return new SkRuntimeError(node, context, "Invalid operand type '" + left.getType() + "' used in ** expression");
    }
    
    private SkValue and(SkValue left, SkValue right, BinaryOperationNode node, Context context) {
        return left.into(SkBool.class).and(right);
    }
    
    private SkValue or(SkValue left, SkValue right, BinaryOperationNode node, Context context) {
        return left.into(SkBool.class).or(right);
    }
    
    private SkValue equals(SkValue left, SkValue right, BinaryOperationNode node, Context context) {
        return left.equalTo(right);
    }
    
    private SkValue notEquals(SkValue left, SkValue right, BinaryOperationNode node, Context context) {
        return left.equalTo(right).not();
    }
}