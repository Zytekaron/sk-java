package com.zytekaron.sk.parse.nodes;

import com.zytekaron.sk.struct.Token;
import lombok.Getter;

@Getter
public class ElementAccessNode extends Node {
    private final Token name;
    private final Node value;
    
    public ElementAccessNode(Token name, Node value) {
        super(name, value);
        this.name = name;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return String.format("ElemAccess(%s %s)", name, value);
    }
}