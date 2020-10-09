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

import com.zytekaron.sk.types.primitive.SkDouble;
import com.zytekaron.sk.types.primitive.SkInt;
import com.zytekaron.sk.types.primitive.SkLong;
import com.zytekaron.sk.types.SkValue;

import java.util.function.Function;

public class Utils {
    
    public static SkValue math(Function<Integer, SkValue> intFunction,
                         Function<Long, SkValue> longFunction,
                         Function<Double, SkValue> doubleFunction,
                         SkValue other) {
        if (other instanceof SkInt) {
            SkInt skInt = (SkInt) other;
            return intFunction.apply(skInt.getValue());
        }
        if (other instanceof SkDouble) {
            SkDouble skDouble = (SkDouble) other;
            return doubleFunction.apply(skDouble.getValue());
        }
        if (other instanceof SkLong) {
            SkLong skLong = (SkLong) other;
            return longFunction.apply(skLong.getValue());
        }
        return null;
    }
}