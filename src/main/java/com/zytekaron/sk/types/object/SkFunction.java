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

package com.zytekaron.sk.types.object;

import com.zytekaron.sk.parse.nodes.Node;
import com.zytekaron.sk.types.SkObject;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class SkFunction extends SkObject {
    private final List<SkParameter> parameters;
    private final List<Node> statements;
    private final boolean lambda;
    
    public SkFunction(List<SkParameter> parameters, List<Node> statements, boolean lambda) {
        super();
        this.parameters = parameters;
        this.statements = statements;
        this.lambda = lambda;
    }
    
    public SkFunction(List<SkParameter> parameters, List<Node> statements) {
        this(parameters, statements, false);
    }
    
    public SkFunction(List<SkParameter> parameters, Node statement) {
        this(parameters, Collections.singletonList(statement), true);
    }
    
    @Override
    public String toString() {
        return (lambda ? "Lambda ->" : "Function -> ") + Arrays.toString(statements.toArray());
    }
}