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
import com.zytekaron.sk.parse.nodes.VarAccessNode;
import com.zytekaron.sk.struct.Context;
import com.zytekaron.sk.struct.Token;
import com.zytekaron.sk.struct.VariableTable;
import com.zytekaron.sk.types.SkValue;
import com.zytekaron.sk.types.error.SkRuntimeError;

public class FunctionCallHandler implements Handler {
    private final Interpreter interpreter;
    
    public FunctionCallHandler(Interpreter interpreter) {
        this.interpreter = interpreter;
    }
    
    @Override
    public SkValue handle(Node node, Context context) {
        return handle((VarAccessNode) node, context);
    }
    
    private SkValue handle(VarAccessNode node, Context context) {
        VariableTable table = context.getVariableTable();
        
        Token token = node.getName();
        String name = token.getValue();
        
        if (table.contains(name)) {
            return table.get(name);
        } else {
            return new SkRuntimeError(token, context, "'" + name + "' is not defined");
        }
    }
}