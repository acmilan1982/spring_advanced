package com.itheima.a04;

import com.itheima.a04.component.Bean7;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// AutowiredAnnotationBeanPostProcessor 运行分析
public class DigInAutowired {
    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("bean2", new Bean2()); //  不会走 创建过程,依赖注入,初始化 过程，已经是一个成品的bean了
        beanFactory.registerSingleton("bean3", new Bean3());
        beanFactory.registerSingleton("bean7", new Bean7());

        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver()); // @Value，获取@Value中指定的值
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders); // ${} 的解析器

        // 1. 查找哪些属性、方法加了 @Autowired, 这称之为 InjectionMetadata
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor(); //  用来解析@Autowired @Value
        processor.setBeanFactory(beanFactory);


        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");

//        System.out.println(beanFactory.getBean(com.itheima.a04.component.Bean7.class).getUrl());
//        System.out.println(beanFactory.getBean(com.itheima.a04.component.Bean7.class).getUsername());
//        System.out.println(beanFactory.getBean(com.itheima.a04.component.Bean7.class).getUser());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");

        Bean1 bean1 = new Bean1();
        System.out.println(bean1);
//        processor.postProcessProperties(null, bean1, "bean1"); // 执行依赖注入 ，即解析@Autowired @Value
//        System.out.println(bean1);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");

        // 开始模拟AutowiredAnnotationBeanPostProcessor的实现


        //-------------- ContextAnnotationAutowireCandidateResolver 反射执行开始--------------------

        Method findAutowiringMetadata = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        findAutowiringMetadata.setAccessible(true);


        // 1. 被@Autowired的成员元素，可能是变量，方法
        InjectionMetadata metadata = (InjectionMetadata) findAutowiringMetadata.invoke(processor, "bean1", Bean1.class, null);// 获取 Bean1 上加了 @Value @Autowired 的成员变量，方法参数信息
        System.out.println(metadata);

        // 2. 调用 InjectionMetadata 来进行依赖注入, 注入时按类型查找值
        metadata.inject(bean1, "bean1", null);
        System.out.println(bean1);

        //-------------- ContextAnnotationAutowireCandidateResolver 反射执行结束--------------------




        //------------- inject的执行过程

        System.out.println("如何按类型查找值>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
        //  如何按类型查找值
        Field bean3 = Bean1.class.getDeclaredField("bean3");
        DependencyDescriptor dd1 = new DependencyDescriptor(bean3, false);  // 依赖的信息，封装了依赖的类型
        Object o = beanFactory.doResolveDependency(dd1, null, null, null);  // 根据依赖，从beanFactory里去找依赖
        System.out.println(o);


        Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);

        // 一个方法可能有多个参数需要注入,每个需要注入的参数对应一个MethodParameter
        DependencyDescriptor dd2 =
                new DependencyDescriptor(new MethodParameter(setBean2, 0), true);  // 一个方法可能有多个参数需要注入
        Object o1 = beanFactory.doResolveDependency(dd2, null, null, null);
        System.out.println(o1);


        Method setHome = Bean1.class.getDeclaredMethod("setHome", String.class);
        DependencyDescriptor dd3 = new DependencyDescriptor(new MethodParameter(setHome, 0), true);
        Object o2 = beanFactory.doResolveDependency(dd3, null, null, null);
        System.out.println(o2);

    }
}
