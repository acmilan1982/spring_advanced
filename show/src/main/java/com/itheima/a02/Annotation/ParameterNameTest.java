package com.itheima.a02.Annotation;

@Orange(getName = "3333",getValue = "4444")
public class ParameterNameTest {


    public static void main(String[] args)  throws Exception {

        Class<ParameterNameTest> clazz = ParameterNameTest.class;

        if(clazz.isAnnotationPresent(Orange.class)){
            // 获取 "类" 上的注解
            Orange getAnnotation = clazz.getAnnotation(Orange.class);
            System.out.println("\"类\"上的注解值获取到第一个 ："
                    + getAnnotation.getName()+ "，第二个："+ getAnnotation.getValue());
        }
    }
}
