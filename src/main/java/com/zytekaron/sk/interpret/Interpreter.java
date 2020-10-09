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

package com.zytekaron.sk.interpret;

import com.zytekaron.sk.interpret.handlers.*;
import com.zytekaron.sk.parse.nodes.*;
import com.zytekaron.sk.struct.Context;
import com.zytekaron.sk.struct.result.RuntimeResult;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    private final Map<Class<? extends Node>, Handler> handlers = new HashMap<>();
    
    public Interpreter() {
        // Primitive types
        handlers.put(BoolNode.class, new BoolHandler());
        handlers.put(StringNode.class, new StringHandler());
        handlers.put(NumberNode.class, new NumberHandler());
        // Objects
        handlers.put(ArrayNode.class, new ArrayHandler(this));
        // Variables
        handlers.put(VarAccessNode.class, new VarAccessHandler());
        handlers.put(VarDeleteNode.class, new VarDeleteHandler());
        handlers.put(VarAssignNode.class, new VarAssignHandler(this));
        handlers.put(VarReassignNode.class, new VarReassignHandler(this));
        // Functions
        handlers.put(ReturnNode.class, new ReturnHandler(this));
        handlers.put(FunctionCallNode.class, new FunctionCallHandler(this));
        handlers.put(FunctionDefineNode.class, new FunctionDefineHandler(this));
        // Operations
        handlers.put(UnaryOperationNode.class, new UnaryOperationHandler(this));
        handlers.put(BinaryOperationNode.class, new BinaryOperationHandler(this));
    }
    
    public RuntimeResult visit(Node node, Context context) {
        if (node == null || context == null) {
            throw new RuntimeException("Found null node or context (" + node + ", " + context + ")");
        }
        Class<? extends Node> clazz = node.getClass();
        Handler handler = handlers.get(clazz);
        if (handler == null) {
            throw new RuntimeException("SkInterpreter missing Node handler for type " + clazz); // todo remove when done
        }
        return handler.handle(node, context);
    }
}