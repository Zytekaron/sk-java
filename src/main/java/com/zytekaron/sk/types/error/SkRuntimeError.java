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

package com.zytekaron.sk.types.error;

import com.zytekaron.sk.parse.nodes.Node;
import com.zytekaron.sk.struct.Context;
import com.zytekaron.sk.struct.Position;
import com.zytekaron.sk.struct.Token;

import java.util.ArrayList;
import java.util.List;

public class SkRuntimeError extends SkError {
    private static final String ERROR_NAME = "RuntimeError";
    private final Context context;
    
    public SkRuntimeError(Position start, Position end, Context context, String details) {
        super(ERROR_NAME, start, end, details);
        this.context = context;
    }
    
    public SkRuntimeError(Token token, Context context, String details) {
        super(ERROR_NAME, token, details);
        this.context = context;
    }
    
    public SkRuntimeError(Node node, Context context, String details) {
        super(ERROR_NAME, node, details);
        this.context = context;
    }
    
    public List<String> generateStack() {
        List<String> stack = new ArrayList<>();
        Position position = getStart();
        Context ctx = context;
        while (ctx != null) {
            String str = String.format("File %s, line %s, at %s", position.getFile(), position.getLine() + 1, context.getDisplayName());
            stack.add(str);
            ctx = context.getParent();
        }
        return stack;
    }
    
    @Override
    public String toString() {
        String error = getName() + ": " + getDetails() + '\n';
        error += "Traceback (most recent call last):\n";
        error += String.join("\n", generateStack());
        return error;
    }
}