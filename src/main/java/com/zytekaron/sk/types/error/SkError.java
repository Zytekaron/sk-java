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
import com.zytekaron.sk.struct.Position;
import com.zytekaron.sk.struct.Token;
import com.zytekaron.sk.types.SkValue;
import lombok.Getter;

@Getter
public abstract class SkError extends SkValue {
    private final Position start;
    private final Position end;
    private final String name;
    private final String details;
    
    public SkError(String name, Position start, Position end, String details) {
        super("Error");
        this.start = start;
        this.end = end;
        this.name = name;
        this.details = details;
    }
    
    public SkError(String name, Token token, String details) {
        super("Error");
        this.start = token.getStart();
        this.end = token.getEnd();
        this.name = name;
        this.details = details;
    }
    
    public SkError(String name, Node node, String details) {
        super("Error");
        this.start = node.getStart();
        this.end = node.getEnd();
        this.name = name;
        this.details = details;
    }
    
    public void raise() {
        String text = toString()
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\b", "\\b");
        System.out.println(text);
        System.out.println("File " + start.getFile());
        System.out.println("Line " + start.getLine() + " to " + end.getLine());
        System.out.println("Column " + start.getColumn() + " to " + end.getColumn());
    }
    
    @Override
    protected int compare(SkValue other) {
        return 0;
    }
    
    @Override
    public <T> T into(Class<T> clazz) {
        return null;
    }
    
    @Override
    public String toString() {
        return name + ": " + details;
    }
}