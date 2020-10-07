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

package com.zytekaron/*
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
*/.sk.parse.nodes;

import com.zytekaron.sk.struct.Position;
import com.zytekaron.sk.struct.Token;
import lombok.Getter;

@Getter
public abstract class Node {
    private Position start;
    private Position end;
    
    public Node(Position start, Position end) {
        this.start = start;
        this.end = end;
    }
    
    public Node(Node node) {
        this.start = node.start;
        this.end = node.end;
    }
    
    public Node(Token token) {
        this.start = token.getStart();
        this.end = token.getEnd();
    }
    
    public Node(Token token, Node node) {
        this.start = token.getStart();
        this.end = node.end;
    }
    
    public void setEnd(Position end) {
        this.end = end;
    }
}