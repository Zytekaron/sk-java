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

import com.zytekaron.sk.parse.nodes.Node;
import com.zytekaron.sk.parse.nodes.VarAssignNode;
import com.zytekaron.sk.struct.Context;
import com.zytekaron.sk.struct.Token;
import com.zytekaron.sk.struct.VariableTable;
import com.zytekaron.sk.struct.result.RuntimeResult;
import com.zytekaron.sk.types.SkValue;
import com.zytekaron.sk.types.error.SkError;
import com.zytekaron.sk.types.error.SkRuntimeError;

public class VarDeleteHandler implements Handler {
    
    @Override
    public RuntimeResult handle(Node node, Context context) {
        return handle((VarAssignNode) node, context);
    }
    
    private RuntimeResult handle(VarAssignNode node, Context context) {
        RuntimeResult result = new RuntimeResult();
        
        VariableTable table = context.getVariableTable();
        
        Token nameToken = node.getName();
        String name = nameToken.getValue();
    
        if (!table.contains(name)) {
            SkError error = new SkRuntimeError(node, context, "Variable '" + name + "' not defined");
            return result.failure(error);
        }
        if (table.isImmutable()) {
            SkError error = new SkRuntimeError(node, context, "Variable '" + name + "' cannot be deleted");
            return result.failure(error);
        }
        
        SkValue value = table.get(name);
        table.delete(name);
        return result.success(value);
    }
}