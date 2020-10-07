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
import com.zytekaron.sk.parse.nodes.Node;
import com.zytekaron.sk.parse.nodes.UnaryOperationNode;
import com.zytekaron.sk.struct.Context;
import com.zytekaron.sk.struct.Token;
import com.zytekaron.sk.struct.TokenType;
import com.zytekaron.sk.types.*;
import com.zytekaron.sk.types.error.SkRuntimeError;

import static com.zytekaron.sk.struct.TokenType.*;

public class UnaryOperationHandler implements Handler {
    private final Interpreter interpreter;
    
    public UnaryOperationHandler(Interpreter interpreter) {
        this.interpreter = interpreter;
    }
    
    @Override
    public SkValue handle(Node node, Context context) {
        return handle((UnaryOperationNode) node, context);
    }
    
    private SkValue handle(UnaryOperationNode node, Context context) {
        Token operator = node.getOperator();
        Node operand = node.getOperand();
        
        SkValue value = interpreter.visit(operand, context);
        
        if (value instanceof SkNumber) {
            return handleNumber((SkNumber) value, operator, operand, context);
        } else if (value instanceof SkString) {
            SkNumber number = SkNumber.fromString(value.toString());
            return handleNumber(number, operator, operand, context);
        } else if (value instanceof SkBool) {
            return handleBoolean((SkBool) value, operator, operand, context);
        } else {
            return new SkRuntimeError(operator.getStart(), operand.getEnd(), context, "Expected number or string type, instead found " + value.getType());
        }
    }
    
    private SkValue handleNumber(SkNumber number, Token operator, Node operand, Context context) {
        TokenType operatorType = operator.getType();
        
        if (operatorType == PLUS) {
            return number;
        } else if (operatorType == MINUS) {
            return invertNumber(number);
        } else if (operatorType == NOT) {
            return new SkRuntimeError(operand.getStart(), operand.getEnd(), context, "Invalid type coercion from type '" + number.getType() + "' to 'boolean'");
        } else {
            throw new RuntimeException("Invalid unary operator passed: " + operator);
        }
    }
    
    private SkValue handleBoolean(SkBool bool, Token operator, Node operand, Context context) {
        TokenType operatorType = operator.getType();
        
        if (operatorType == PLUS || operatorType == MINUS) {
            // todo trust the code and don't use java exceptions
            // todo make sure we're not allowing boolean to number conversions in the final version
            return new SkRuntimeError(operand.getStart(), operand.getEnd(), context, "Invalid type coercion from type 'boolean' to 'int'");
        } else if (operatorType == NOT) {
            return invertBoolean(bool);
        } else {
            throw new RuntimeException("Invalid unary operator passed: " + operator);
        }
    }
    
    private SkValue invertNumber(SkNumber number) {
        // fixme fixme number#multiply
        if (number instanceof SkInt) {
            SkInt skInt = (SkInt) number;
            return skInt.multiply(-1);
        } else if (number instanceof SkDouble) {
            SkDouble skDouble = (SkDouble) number;
            return skDouble.multiply(-1);
        } else {
            throw new RuntimeException("Invalid SkNumber type passed...maybe try supporting it? " + number.getClass().getSimpleName());
        }
    }
    
    private SkValue invertBoolean(SkBool bool) {
        boolean value = bool.getValue();
        return new SkBool(!value);
    }
}