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

package com.zytekaron.sk.parse.nodes;

import com.zytekaron.sk.struct.Token;
import lombok.Getter;

@Getter
public class BinaryOperationNode extends Node {
    private final Node leftOperand;
    private final Token operation;
    private final Node rightOperand;
    
    public BinaryOperationNode(Node leftOperand, Token operation, Node rightOperand) {
        super(leftOperand.getStart(), rightOperand.getEnd());
        this.leftOperand = leftOperand;
        this.operation = operation;
        this.rightOperand = rightOperand;
    }
    
    @Override
    public String toString() {
        return String.format("BinOp(%s %s %s)", leftOperand, operation, rightOperand);
    }
}