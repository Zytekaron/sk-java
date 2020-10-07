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

import com.zytekaron.sk.struct.Position;
import com.zytekaron.sk.struct.Token;

public class SkParsingError extends SkError {
    private static final String ERROR_NAME = "ParsingError";
    
    public SkParsingError(Position start, Position end, String details) {
        super(ERROR_NAME, start, end, details);
    }
    
    public SkParsingError(Token token, String details) {
        super(ERROR_NAME, token, details);
    }
}