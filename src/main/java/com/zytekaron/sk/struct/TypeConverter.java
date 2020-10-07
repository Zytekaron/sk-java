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

import com.zytekaron.sk.types.*;

// <#> responsibility for math errors is left to the user
public class TypeConverter {
    
    /**
     * Convert a SkValue to a Java String
     *
     * @param value The value to convert
     * @return SkBool or null if not converted
     */
    public static SkString toString(SkValue value) {
        if (value == null) {
            return new SkString("null");
        } else {
            return value.toSkString();
        }
    }
    
    /**
     * Convert a Java String to an SkInt
     * @param string The string to convert
     * @return SkInt or null if not converted
     */
    public static SkNumber stringToInt(String string) {
        int i = Integer.parseInt(string);
        return new SkInt(i);
    }
    
    /**
     * Convert a Java String to a SkFloat
     *
     * @param string The string to convert
     * @return SkFloat or null if not converted
     */
    public static SkNumber stringToFloat(String string) {
        double d = Double.parseDouble(string);
        return new SkDouble(d);
    }
    
    /**
     * Convert a SkValue to an SkBool
     *
     * @param value The value to convert
     * @return SkBool or null if not converted
     */
    public static SkBool valueToBool(SkValue value) {
        if (value instanceof SkInt && ((SkInt) value).getValue() == 0) {
            return new SkBool(false);
        }
        if (value instanceof SkDouble && ((SkDouble) value).getValue() == 0) {
            return new SkBool(false);
        }
        if (value instanceof SkString && ((SkString) value).getValue().isEmpty()) {
            return new SkBool(false);
        }
        // todo LATER implement for array type
        return new SkBool(true);
    }
}