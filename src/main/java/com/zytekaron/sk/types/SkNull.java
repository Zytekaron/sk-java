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

package com.zytekaron.sk.types;

public class SkNull extends SkValue {
    
    public SkNull() {
        super("null");
    }
    
    @Override
    public <T> T into(Class<T> clazz) {
        throw new RuntimeException("Unsupported operation 'cast' for type 'null'");
    }
    
    @Override
    protected int compare(SkValue other) {
        return other instanceof SkNull ? 0 : 1;
    }
    
    @Override
    public String toString() {
        return "null";
    }
}