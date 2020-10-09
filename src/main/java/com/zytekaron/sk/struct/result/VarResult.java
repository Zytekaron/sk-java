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

import com.zytekaron.sk.types.error.SkError;
import lombok.Getter;

@Getter
public class VarResult {
    private SkError error;
    
    public boolean success() {
        return error != null;
    }
    
    public VarResult setSuccess() {
        return this;
    }
    
    public VarResult failure(SkError error) {
        this.error = error;
        return this;
    }
}