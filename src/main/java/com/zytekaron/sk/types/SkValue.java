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

package com.zytekaron.sk.types;

import com.zytekaron.sk.types.object.SkString;
import com.zytekaron.sk.types.primitive.SkBool;
import lombok.Getter;

@Getter
public abstract class SkValue {
    private final String type;
    
    public SkValue(String type) {
        this.type = type;
    }
    
    protected abstract int compare(SkValue other);
    public abstract <T> T into(Class<T> clazz);
    
    public SkBool equalTo(SkValue other) {
        return new SkBool(compare(other) == 0);
    }
    
    public SkBool lessThan(SkValue other) {
        return new SkBool(compare(other) < 0);
    }
    
    public SkBool greaterThan(SkValue other) {
        return new SkBool(compare(other) > 0);
    }
    
    public SkString toSkString() {
        return new SkString(toString());
    }
}