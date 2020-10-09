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
import com.zytekaron.sk.parse.nodes.FunctionDefineNode;
import com.zytekaron.sk.parse.nodes.FunctionParameterNode;
import com.zytekaron.sk.parse.nodes.Node;
import com.zytekaron.sk.parse.nodes.ScopeNode;
import com.zytekaron.sk.struct.Context;
import com.zytekaron.sk.struct.Token;
import com.zytekaron.sk.struct.VariableTable;
import com.zytekaron.sk.struct.result.RuntimeResult;
import com.zytekaron.sk.types.SkValue;
import com.zytekaron.sk.types.error.SkError;
import com.zytekaron.sk.types.error.SkRuntimeError;
import com.zytekaron.sk.types.object.SkFunction;
import com.zytekaron.sk.types.object.SkParameter;

import java.util.ArrayList;
import java.util.List;

public class FunctionDefineHandler implements Handler {
    private final Interpreter interpreter;
    
    public FunctionDefineHandler(Interpreter interpreter) {
        this.interpreter = interpreter;
    }
    
    @Override
    public RuntimeResult handle(Node node, Context context) {
        return handle((FunctionDefineNode) node, context);
    }
    
    private RuntimeResult handle(FunctionDefineNode node, Context context) {
        RuntimeResult result = new RuntimeResult();
        
        VariableTable table = context.getVariableTable();
        
        Token token = node.getName();
        String name = token.getValue();
        
        List<Node> nodes = node.getParams();
        
        List<SkParameter> params = new ArrayList<>();
        for (Node inputNode : node.getParams()) {
            FunctionParameterNode input = (FunctionParameterNode) inputNode;
            
            SkValue defaultValue = null;
            if (input.getDefaultValue() != null) {
                RuntimeResult defaultResult = interpreter.visit(input.getDefaultValue(), context);
                defaultValue = result.register(defaultResult);
                if (result.shouldReturn()) {
                    return result;
                }
            }
            
            SkParameter param = new SkParameter(input.getName().getValue(), input.isSpread(), defaultValue);
            params.add(param);
        }
    
        // todo idk consider using RuntimeResult here
        SkError validationError = validateParameters(nodes, params, context);
        if (validationError != null) {
            return result.failure(validationError);
        }
        
        ScopeNode scope = (ScopeNode) node.getScope();
        List<Node> statements = scope.getExpressions();
        
        SkFunction function = new SkFunction(params, statements);
        table.put(name, function);
        return result.success(function);
    }
    
    private SkError validateParameters(List<Node> nodes, List<SkParameter> params, Context context) {
        boolean spreadBefore = false;
        boolean defaultBefore = false;
        for (int i = 0; i < nodes.size(); i++) {
            SkParameter param = params.get(i);
    
            if (param.isSpread()) {
                spreadBefore = true;
            } else if (spreadBefore) {
                Node node = nodes.get(i);
                return new SkRuntimeError(node, context, "Unexpected spread operator: already found one");
            }
    
            if (param.getDefaultValue() != null) {
                defaultBefore = true;
            } else if (defaultBefore) {
                Node node = nodes.get(i);
                return new SkRuntimeError(node, context, "Unexpected required parameter after default parameter");
            }
    
            // todo do type checking here -- if (!param.getDefaultValue().isOfType(param.getType())) error
        }
        return null;
    }
}