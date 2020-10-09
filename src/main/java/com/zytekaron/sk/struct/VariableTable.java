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

package com.zytekaron.sk.struct;

import com.zytekaron.sk.types.SkValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class VariableTable {
    private final Map<String, SkValue> variables = new HashMap<>();
    private final VariableTable parent;
    private boolean immutable = false;
    
    public VariableTable() {
        this(null);
    }
    
    public VariableTable(VariableTable parent) {
        this.parent = parent;
    }
    
    public SkValue put(String name, SkValue value) {
        return variables.put(name, value);
    }
    
    public boolean delete(String name) {
        VariableTable table = findTable(name);
        if (table == null || immutable) {
            return false;
        }
        if (table == this) {
            variables.remove(name);
            return true;
        }
        return table.delete(name);
    }
    
    public boolean contains(String name) {
        boolean contains = variables.containsKey(name);
        if (!contains && parent != null) {
            return parent.contains(name);
        }
        return contains;
    }
    
    public boolean containsHere(String name) {
        return variables.containsKey(name);
    }
    
    public SkValue get(String name) {
        SkValue value = variables.get(name);
        if (value == null && parent != null) {
            return parent.get(name);
        }
        return value;
    }
    
    private VariableTable findTable(String name) {
        if (containsHere(name)) {
            return this;
        } else if (parent != null) {
            return parent.findTable(name);
        } else {
            return null;
        }
    }
    
    public boolean isImmutable() {
        return immutable;
    }
}