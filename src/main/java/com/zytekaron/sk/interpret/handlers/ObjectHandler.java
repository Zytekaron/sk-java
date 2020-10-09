/*
 *    Copyright 2020 Michael Thornes
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.zytekaron.sk.interpret.handlers;

import com.zytekaron.sk.interpret.Interpreter;
import com.zytekaron.sk.parse.nodes.Node;
import com.zytekaron.sk.parse.nodes.ObjectNode;
import com.zytekaron.sk.struct.Context;
import com.zytekaron.sk.struct.result.RuntimeResult;
import com.zytekaron.sk.types.SkObject;
import com.zytekaron.sk.types.SkValue;

import java.util.HashMap;
import java.util.Map;

public class ObjectHandler implements Handler {
    private final Interpreter interpreter;
    
    public ObjectHandler(Interpreter interpreter) {
        this.interpreter = interpreter;
    }
    
    @Override
    public RuntimeResult handle(Node node, Context context) {
        return handle((ObjectNode) node, context);
    }
    
    private RuntimeResult handle(ObjectNode node, Context context) {
        RuntimeResult result = new RuntimeResult();
        
        Map<String, Node> nodes = node.getNodes();
        
        Map<String, SkValue> values = new HashMap<>();
        for (Map.Entry<String, Node> entry : nodes.entrySet()){
            RuntimeResult res = interpreter.visit(entry.getValue(), context);
            SkValue value = result.register(res);
            if (result.shouldReturn()) {
                return result;
            }
            values.put(entry.getKey(), value);
        }
    
        SkObject object = new SkObject(values);
        return result.success(object);
    }
}