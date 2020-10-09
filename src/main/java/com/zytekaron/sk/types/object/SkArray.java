package com.zytekaron.sk.types.object;

import com.zytekaron.sk.types.SkObject;
import com.zytekaron.sk.types.SkValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class SkArray extends SkObject {
    private final List<SkValue> elements;
    
    public SkArray(List<SkValue> elements) {
        super();
        this.elements = elements;
    }
    
    @Override
    public String toString() {
        return Arrays.toString(elements.toArray());
    }
}