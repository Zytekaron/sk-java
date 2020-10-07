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
public class FunctionParameterNode extends Node {
    private final Token name;
//    private final Token type; todo LATER implement the type here -- allow primitive values and classes, try to exclude Int Long etc.
    private final Node defaultValue;
    private final boolean spread;
    
    public FunctionParameterNode(Token name, Node defaultValue, boolean spread) {
        super(name, defaultValue);
        this.name = name;
        this.defaultValue = defaultValue;
        this.spread = spread;
    }
    
    @Override
    public String toString() {
        // ...name = default, name = default, ...name, name
        return "FnParam(" + (spread ? "..." : "") + name + (defaultValue == null ? "" : " = " + defaultValue) + ")";
    }
}