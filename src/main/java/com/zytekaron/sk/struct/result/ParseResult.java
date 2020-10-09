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

package com.zytekaron.sk.struct.result;

import com.zytekaron.sk.parse.nodes.Node;
import com.zytekaron.sk.types.error.SkError;
import lombok.Getter;

@Getter
public class ParseResult {
    private Node result;
    private SkError error;
    private int advancements = 0;
    
    public boolean success() {
        return error == null;
    }
    
    public Node register(ParseResult parseResult) {
        advancements += parseResult.advancements;
        if (!parseResult.success()) {
            error = parseResult.getError();
        }
        return parseResult.getResult();
    }
    
    public void registerAdvancement() {
        advancements++;
    }
    
    public ParseResult success(Node result) {
        this.result = result;
        return this;
    }
    
    public ParseResult failure(SkError error) {
        // set an error if and only if there isn't one already or there were no advancements on this result yet
        if (this.error == null || advancements == 0) {
            this.error = error;
        }
        return this;
    }
    
    public Node getResult() {
        return result;
    }
    
    public SkError getError() {
        return error;
    }
}