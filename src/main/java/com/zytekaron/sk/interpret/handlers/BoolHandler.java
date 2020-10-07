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
import com.zytekaron.sk.parse.nodes.StringNode;
import com.zytekaron.sk.struct.Context;
import com.zytekaron.sk.struct.Token;
import com.zytekaron.sk.types.SkBool;
import com.zytekaron.sk.types.SkValue;

public class BoolHandler implements Handler {
    
    @Override
    public SkValue handle(Node node, Context context) {
        return handle((StringNode) node, context);
    }
    
    private SkValue handle(StringNode node, Context context) {
        Token token = node.getToken();
        String value = token.getValue();
        return SkBool.fromString(value);
    }
}