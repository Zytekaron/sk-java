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

import com.zytekaron.sk.struct.Position;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ScopeNode extends Node {
    private final List<Node> expressions;
    
    public ScopeNode(List<Node> expressions) {
        // rule exception, because scopes with no statements __cannot__ error
        super(null, (Position) null);
        this.expressions = expressions;
    }
    
    @Override
    public String toString() {
        return String.format("Scope { %s }", expressions.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" ")));
    }
}