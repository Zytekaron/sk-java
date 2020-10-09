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
import com.zytekaron.sk.parse.nodes.FunctionCallNode;
import com.zytekaron.sk.parse.nodes.Node;
import com.zytekaron.sk.struct.Context;
import com.zytekaron.sk.struct.Token;
import com.zytekaron.sk.struct.VariableTable;
import com.zytekaron.sk.struct.result.RuntimeResult;
import com.zytekaron.sk.types.SkNull;
import com.zytekaron.sk.types.SkValue;
import com.zytekaron.sk.types.error.SkError;
import com.zytekaron.sk.types.error.SkRuntimeError;
import com.zytekaron.sk.types.object.SkFunction;
import com.zytekaron.sk.types.object.SkParameter;

import java.util.ArrayList;
import java.util.List;

// todo todo todo result.successReturn();
public class FunctionCallHandler implements Handler {
    private final Interpreter interpreter;
    
    public FunctionCallHandler(Interpreter interpreter) {
        this.interpreter = interpreter;
    } // todo todo make sure arg names ae unique for context building && call with context & variables
    
    @Override
    public RuntimeResult handle(Node node, Context context) {
        return handle((FunctionCallNode) node, context);
    }
    
    private RuntimeResult handle(FunctionCallNode node, Context context) {
        RuntimeResult result = new RuntimeResult();
        
        VariableTable table = context.getVariableTable();
        
        // Make sure the function exists
        Token token = node.getName();
        String name = token.getValue();
        if (!table.contains(name)) {
            SkError error = new SkRuntimeError(token, context, "'" + name + "' is not defined");
            return result.failure(error);
        }
    
        // Get a SkFunction from the VariableTable
        SkValue functionValue = table.get(name);
        if (!(functionValue instanceof SkFunction)) {
            SkError error = new SkRuntimeError(token, context, "'" + name + "' is not a function");
            return result.failure(error);
        }
        SkFunction function = (SkFunction) functionValue;
        
        // Visit all the parameters to obtain SkValues
        List<SkValue> params = new ArrayList<>();
        for (Node param : node.getParams()) {
            RuntimeResult res = interpreter.visit(param, context);
            if (res.shouldReturn()) {
                return res;
            }
            params.add(res.getResult());
        }
        
        // Make sure the parameters are valid (correct amount, type) fixme
        SkError validateError = validateParameters(function, params, context);
        if (validateError != null) {
            return result.failure(validateError);
        }
        
        // Call the function
        return execute(function, context);
    }
    
    private RuntimeResult execute(SkFunction function, Context context) {
        RuntimeResult result = new RuntimeResult();
        
        List<Node> nodes = function.getStatements();
        for (Node node : nodes) {
            // todo 1 what are the side effects? does this modify the interpreter? probably, @ RuntimeResult consistent
            RuntimeResult res = interpreter.visit(node, context);
            if (res.shouldReturn()) {
                return res;
            }
        }
        
        // todo -1 consider if this is successReturn or just success --
        //  probably success, since it's not a ReturnNode, it's just a SkValue returner
        //  encapsulated within a RuntimeResult
        return result.success(new SkNull());
    }
    
    private SkError validateParameters(SkFunction function, List<SkValue> args, Context context) {
        List<SkParameter> params = function.getParameters();
        
        int min = 0;
        for (SkParameter param : params) {
            if (param.getDefaultValue() != null) {
                break;
            }
            min++;
        }
        
        int max = params.size();
        if (max > 0 && params.get(params.size() - 1).getDefaultValue() != null) {
            max = Integer.MAX_VALUE;
        }
        
        if (args.size() < min || args.size() > max) {
            // fixme nulls
            return new SkRuntimeError((Node) null, context, "Expected " + min + " to " + max + " args, instead got " + args.size());
        }
        
        for (int i = 0; i < 10; i++) {
            SkParameter param = params.get(i);
            SkValue input = args.get(i);
            
            // todo validate type
        }
        
        return null;
    }
}