package com.itheima.asm;

import org.objectweb.asm.FieldVisitor;

public class MyFieldVisitor extends FieldVisitor {
    public MyFieldVisitor(int api) {
        super(api);
    }
}
