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

import java.util.List;

@Getter
public class FunctionDefineNode extends Node {
    private final Token name;
    private final List<Node> params;
    private final Node scope;
    
    public FunctionDefineNode(Token name, List<Node> params, Node scope) {
        super(name);
        this.name = name;
        this.params = params;
        this.scope = scope;
        
        if (params.size() == 0) {
            setEnd(name.getEnd());
        } else {
            setEnd(params.get(params.size() - 1).getEnd());
        }
    }
    
    @Override
    public String toString() {
        return String.format("FuncDef(%s %s %s)", name, params, scope);
    }
}