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

package com.zytekaron.sk.struct.result;

import com.zytekaron.sk.types.SkValue;
import com.zytekaron.sk.types.error.SkError;
import lombok.Getter;

@Getter
public class RuntimeResult {
    private SkValue result;
    private SkError error;
    private SkValue returnValue;
    private boolean loopContinue;
    private boolean loopBreak;
    
    private void reset() {
        result = null;
        error = null;
        returnValue = null;
        loopContinue = false;
        loopBreak = false;
    }
    
    public boolean success() {
        return error == null;
    }
    
    public SkValue register(RuntimeResult result) {
        error = result.error;
        returnValue = result.returnValue;
        loopContinue = result.loopContinue;
        loopBreak = result.loopBreak;
        return result.result;
    }
    
    public RuntimeResult failure(SkError error) {
        reset();
        this.error = error;
        return this;
    }
    
    public RuntimeResult success(SkValue result) {
        reset();
        this.result = result;
        return this;
    }
    
    public RuntimeResult successReturn(SkValue returnValue) {
        reset();
        this.returnValue = returnValue;
        return this;
    }
    
    public RuntimeResult successContinue() {
        reset();
        this.loopContinue = true;
        return this;
    }
    
    public RuntimeResult successBreak() {
        reset();
        this.loopBreak = true;
        return this;
    }
    
    public boolean shouldReturn() {
        return error != null || returnValue != null || loopContinue || loopBreak;
    }
}