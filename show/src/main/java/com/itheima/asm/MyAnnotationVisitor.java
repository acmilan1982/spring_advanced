package com.itheima.asm;

import org.objectweb.asm.AnnotationVisitor;

/**
 * 如果没有额外操作，可以暂时不覆写方法
 */
public class MyAnnotationVisitor extends AnnotationVisitor {
    public MyAnnotationVisitor(int api) {
        super(api);
    }

    // 覆写注解访问的对应方法
    @Override
    public void visit(String name, Object value) {
        System.out.println("注解属性名："+name);
        System.out.println("注解属性值："+value);
        super.visit(name, value);
    }
}