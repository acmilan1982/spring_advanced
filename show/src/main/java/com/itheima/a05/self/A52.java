package com.itheima.a05.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;


public class A52 {



    public static void main(String[] args) throws NoSuchMethodException {
//        StandardAnnotationMetadata metadata = new StandardAnnotationMetadata(MetaDemo.class, true);

//        System.out.println(Component.class.getName());

        //---类上的注解
//        Service service = AnnotationUtils.findAnnotation( MetaDemo.class, Service.class);
//        System.out.println("service: annotation.value()-------->" + service.value());
        Component component1 = AnnotationUtils.findAnnotation( MetaDemo.class, Component.class);
        System.out.println("component: annotation.value()-------->" + component1.value());

        Component component2 = AnnotatedElementUtils.findMergedAnnotation( MetaDemo.class, Component.class);
        System.out.println("component: annotation.value()-------->" + component2.value());

        System.out.println("------------------------------------");
        //---方法上的注解------------------
        Method getName = MetaDemo.class.getDeclaredMethod("getName");
        System.out.println(getName.getDeclaringClass());


        RequestMapping requestMapping = AnnotationUtils.findAnnotation( getName, RequestMapping.class);
        System.out.println("requestMapping: annotation.value()-------->" + requestMapping.value()[0]);
        System.out.println("requestMapping:annotation.path()-------->" + requestMapping.path()[0]);

////
////        AnnotatedElementUtils.findMergedAnnotation(Object.class.getMethod("hashCode"), PostMapping.class);
//
//        PostMapping annotation = AnnotationUtils.findAnnotation( getName, PostMapping.class);
////        RequestMapping requestMapping = AnnotationUtils.findAnnotation( getName, RequestMapping.class);
//
////        RequestMapping annotation = AnnotatedElementUtils.findMergedAnnotation( getName, RequestMapping.class);
//        System.out.println("PostMapping: annotation.value()-------->" + annotation.value()[0]);
//        System.out.println("PostMapping:annotation.path()-------->" + annotation.path()[0]);



//        System.out.println("RequestMapping-------->" + requestMapping.path()[0]);

//        Component component = AnnotatedElementUtils.findMergedAnnotation(MetaDemo.class, Component.class);
//        System.out.println("annotation.value()-------->" + component.value());
         /*
        // 演示ClassMetadata的效果
        System.out.println("==============ClassMetadata==============");
        ClassMetadata classMetadata = metadata;
        System.out.println(classMetadata.getClassName()); //com.fsx.maintest.MetaDemo
        System.out.println(classMetadata.getEnclosingClassName()); //null  如果自己是内部类此处就有值了
        System.out.println(StringUtils.arrayToCommaDelimitedString(classMetadata.getMemberClassNames())); //com.fsx.maintest.MetaDemo$InnerClass 若木有内部类返回空数组[]
        System.out.println(StringUtils.arrayToCommaDelimitedString(classMetadata.getInterfaceNames())); // java.io.Serializable
        System.out.println("classMetadata.hasSuperClass(): " + classMetadata.hasSuperClass()); // true(只有Object这里是false)
        System.out.println("classMetadata.getSuperClassName(): " + classMetadata.getSuperClassName()); // java.util.HashMap

        System.out.println("classMetadata.isAnnotation( :" + classMetadata.isAnnotation()); // false（是否是注解类型的Class，这里显然是false）
        System.out.println("classMetadata.isFinal() :" + classMetadata.isFinal()); // false
        System.out.println("classMetadata.isIndependent() :" + classMetadata.isIndependent()); // true(top class或者static inner class，就是独立可new的)

        // 演示AnnotatedTypeMetadata的效果
        System.out.println("==============AnnotatedTypeMetadata==============");
        AnnotatedTypeMetadata annotatedTypeMetadata = metadata;
        System.out.println(annotatedTypeMetadata.isAnnotated(Service.class.getName())); // true（依赖的AnnotatedElementUtils.isAnnotated这个方法）
        System.out.println(annotatedTypeMetadata.isAnnotated(Component.class.getName())); // true

        System.out.println("@Service的属性 :" + annotatedTypeMetadata.getAnnotationAttributes(Service.class.getName())); //{value=serviceName}
        System.out.println("@Component的属性 :" + annotatedTypeMetadata.getAnnotationAttributes(Component.class.getName())); // {value=repositoryName}（@Repository的value值覆盖了@Service的）
        System.out.println("@EnableAsync的属性 :" + annotatedTypeMetadata.getAnnotationAttributes(EnableAsync.class.getName())); // {order=2147483647, annotation=interface java.lang.annotation.Annotation, proxyTargetClass=false, mode=PROXY}
        System.out.println("@ComponentScan的属性 :" + annotatedTypeMetadata.getAnnotationAttributes(ComponentScan.class.getName())); //{value=serviceName}


        // 看看getAll的区别：value都是数组的形式
        System.out.println(annotatedTypeMetadata.getAllAnnotationAttributes(Service.class.getName())); // {value=[serviceName]}
        System.out.println(annotatedTypeMetadata.getAllAnnotationAttributes(Component.class.getName())); // {value=[, ]} --> 两个Component的value值都拿到了，只是都是空串而已
        System.out.println(annotatedTypeMetadata.getAllAnnotationAttributes(EnableAsync.class.getName())); //{order=[2147483647], annotation=[interface java.lang.annotation.Annotation], proxyTargetClass=[false], mode=[PROXY]}


            /*
        // 演示AnnotationMetadata子接口的效果（重要）
        System.out.println("==============AnnotationMetadata==============");
        AnnotationMetadata annotationMetadata = metadata;
        System.out.println(annotationMetadata.getAnnotationTypes()); // [org.springframework.stereotype.Repository, org.springframework.stereotype.Service, org.springframework.scheduling.annotation.EnableAsync]
        System.out.println(annotationMetadata.getMetaAnnotationTypes(Service.class.getName())); // [org.springframework.stereotype.Component, org.springframework.stereotype.Indexed]
        System.out.println(annotationMetadata.getMetaAnnotationTypes(Component.class.getName())); // []（meta就是获取注解上面的注解,会排除掉java.lang这些注解们）

        System.out.println(annotationMetadata.hasAnnotation(Service.class.getName())); // true
        System.out.println(annotationMetadata.hasAnnotation(Component.class.getName())); // false（注意这里返回的是false）

        System.out.println(annotationMetadata.hasMetaAnnotation(Service.class.getName())); // false（注意这一组的结果和上面相反，因为它看的是meta）
        System.out.println(annotationMetadata.hasMetaAnnotation(Component.class.getName())); // true

        System.out.println(annotationMetadata.hasAnnotatedMethods(Autowired.class.getName())); // true
        annotationMetadata.getAnnotatedMethods(Autowired.class.getName()).forEach(methodMetadata -> {
            System.out.println(methodMetadata.getClass()); // class org.springframework.core.type.StandardMethodMetadata
            System.out.println(methodMetadata.getMethodName()); // getName
            System.out.println(methodMetadata.getReturnTypeName()); // java.lang.String
        });

        */

    }
}
