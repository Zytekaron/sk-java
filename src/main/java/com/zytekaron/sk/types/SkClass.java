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

package com.zytekaron.sk.types;

import com.zytekaron.sk.types.object.SkFunction;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SkClass extends SkObject {
    private final Map<String, SkFunction> methods = new HashMap<>();
    private final SkClass parent;
    private final String name;
    
    public SkClass() {
        this(null, "UnnamedClass");
    }
    
    public boolean hasMethod(String name) {
        return methods.containsKey(name);
    }
    
    public SkFunction getMethod(String name) {
        return methods.get(name);
    }
    
    public void addMethod(String name, SkFunction function) {
        methods.put(name, function);
    }
    
    public SkClass(SkClass parent, String name) {
        this.parent = parent;
        this.name = name;
    }
}