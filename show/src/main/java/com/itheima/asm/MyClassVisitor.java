package com.itheima.asm;

import org.objectweb.asm.*;

import java.io.IOException;

public class MyClassVisitor extends ClassVisitor {

    int asm_version;
    //构造器必须要传入 ASM版本
    public MyClassVisitor(int api) {
        super(api);
        asm_version = api;
    }

    /**
     * 因为我们需要访问类的名称，字段和注解，所以需要覆写以下三个方法
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println("visit方法执行-------->");
        System.out.println("类名："+name);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        System.out.println("visitAnnotation方法执行-------->");
        System.out.println("注解："+descriptor);
        // 这里需要返回我们自定义的注解访问者，由它去解析注解
        return new MyAnnotationVisitor(asm_version);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        System.out.println("visitField方法执行-------->");
        System.out.println("字段："+name+", 值"+value);
        // 这里需要返回我们的字段访问者，由它去解析字段
        return new MyFieldVisitor(asm_version);
    }


    public static void main(String[] args) throws IOException {
        MyClassVisitor cp = new MyClassVisitor(Opcodes.ASM8);
        ClassReader cr = new ClassReader("com.itheima.asm.MyLift");    // class加载到内存

        cr.accept(cp, 0);

    }

}