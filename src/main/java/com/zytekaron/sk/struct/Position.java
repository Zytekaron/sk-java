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

import lombok.Getter;

@Getter
public class Position {
    private final String file;
    private int index;
    private int line;
    private int column;
    
    public Position() {
        this("<stdin>");
    }
    
    public Position(String file) {
        this(file, -1, 0, -1);
    }
    
    public Position(String file, int index, int line, int column) {
        this.file = file;
        this.index = index;
        this.line = line;
        this.column = column;
    }
    
    public void advance(Character currentChar) {
        index++;
        if (currentChar != null && currentChar.equals('\n')) {
            line++;
            column = 0;
        } else {
            column++;
        }
    }
    
    public Position copy() {
        return new Position(file, index, line, column);
    }
}