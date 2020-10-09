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

package com.zytekaron.sk.types.object;

import com.zytekaron.sk.types.SkValue;
import lombok.Getter;

@Getter
public class SkParameter {
    private final String name;
    private final SkValue defaultValue;
    private final boolean spread;
    
    // todo implement types ie SkType
    public SkParameter(String name, boolean spread, SkValue defaultValue) {
        this.name = name;
        this.spread = spread;
        this.defaultValue = defaultValue;
    }
    
    @Override
    public String toString() {
        return String.format("SkParam(%s = %s)", name, defaultValue);
    }
}