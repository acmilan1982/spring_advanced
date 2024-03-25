package com.itheima.a02.Annotation;

import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotationTest {


    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        {
            Annotation[] annotations = Sub.class.getDeclaredAnnotations();
//        Annotation[] annotations = Sub.class.getAnnotations();
            for (Annotation annotation:annotations) {
                System.out.println(annotation);
            }
//        System.out.println(Sub.class.isAnnotationPresent(Inheritable.class));

            System.out.println("-------------------------------------------------------");

        }

        {
            Annotation[] annotations = Sub.class.getAnnotations();
            for (Annotation annotation:annotations) {
                System.out.println(annotation);
            }

            System.out.println("-------------------------------------------------------");

        }

        System.out.println("执行方法：Sub.class.getAnnotation(Inheritable.class)");
        System.out.println(Sub.class.getAnnotation(Inheritable.class));

        System.out.println("-------------------------------------------------------");
        System.out.println(" 执行方法：Sub.class.getDeclaredAnnotation(Inheritable.class)");
        System.out.println(Sub.class.getDeclaredAnnotation(Inheritable.class));
        System.out.println("-------------------------------------------------------");


        /*
             Annotation 类
             java.lang.annotation.Annotation 是一个接口，所有注解都隐式地继承了该接口。它有一个重要方法：

         */

        System.out.println("-------------------------------------------------------");
        ComponentScan componentScan = Sub.class.getAnnotation(ComponentScan.class);

        System.out.println(componentScan.basePackages()[0]);
        System.out.println(componentScan.value().length);

        Method[] methods = componentScan.annotationType().getMethods();
        for (Method method:methods) {
            if(method.getName().equals("basePackages")){
                System.out.println(method.getName());
                String[] basePackages = (String[]) method.invoke(componentScan);
                System.out.println(basePackages[0]);
            }


        }
        System.out.println("-------------------------------------------------------");
        /*
            ava.lang.annotation.Annotation 是一个接口，所有注解都隐式地继承了该接口。它有一个重要方法：

            Class<? extends Annotation> annotationType();
            使用 getClass() 方法只能获取到一个代理对象，而非该接口的 class 对象。

         */
        Annotation[] annotations =  componentScan.annotationType().getDeclaredAnnotations();


        for (Annotation annotation:annotations) {
            System.out.println(annotation);
        }




        /*
            AnnotatedElement 接口
            java.lang.reflect.AnnotatedElement 是一个接口，Class、Constructor、Field、Method 等类都直接或间接地实现了该接口。它有以下重要方法：

            boolean isAnnotationPresent(Class<? extends Annotation> annotationClass)

            <T extends Annotation> T getAnnotation(Class<T> annotationClass);
            <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass)

            Annotation[] getAnnotations();
            Annotation[] getDeclaredAnnotations();

            <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass)
            <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass)

         */
    }

}
