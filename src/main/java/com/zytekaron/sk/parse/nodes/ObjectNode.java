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

package com.zytekaron.sk.parse.nodes;

import com.zytekaron.sk.struct.Position;
import lombok.Getter;

import java.util.Map;

@Getter
public class ObjectNode extends Node {
    private final Map<String, Node> nodes;
    
    public ObjectNode(Position start, Position end, Map<String, Node> nodes) {
        super(start, end);
        this.nodes = nodes;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Node> node : nodes.entrySet()) {
            builder.append(node.getKey())
                    .append(": ")
                    .append(node.getValue());
        }
        return builder.append("\n}").toString();
    }
}