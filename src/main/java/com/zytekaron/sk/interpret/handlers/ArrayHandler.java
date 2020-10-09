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
import com.zytekaron.sk.parse.nodes.ArrayNode;
import com.zytekaron.sk.parse.nodes.Node;
import com.zytekaron.sk.struct.Context;
import com.zytekaron.sk.struct.result.RuntimeResult;
import com.zytekaron.sk.types.SkValue;
import com.zytekaron.sk.types.object.SkArray;

import java.util.ArrayList;
import java.util.List;

public class ArrayHandler implements Handler {
    private final Interpreter interpreter;
    
    public ArrayHandler(Interpreter interpreter) {
        this.interpreter = interpreter;
    }
    
    @Override
    public RuntimeResult handle(Node node, Context context) {
        return handle((ArrayNode) node, context);
    }
    
    private RuntimeResult handle(ArrayNode node, Context context) {
        RuntimeResult result = new RuntimeResult();
        
        List<Node> nodes = node.getNodes();
        
        List<SkValue> elements = new ArrayList<>();
        for (Node n : nodes) {
            RuntimeResult res = interpreter.visit(n, context);
            if (!res.shouldReturn()) {
                return res;
            }
            elements.add(res.getResult());
        }
        
        SkArray array = new SkArray(elements);
        
        return result.success(array);
    }
}