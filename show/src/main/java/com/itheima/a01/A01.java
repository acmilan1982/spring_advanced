package com.itheima.a01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

/*
    BeanFactory 与 ApplicationContext 的区别
 */
@SpringBootApplication  // 引导类的注解
public class A01 {

    private static final Logger log = LoggerFactory.getLogger(A01.class);

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {

        ConfigurableApplicationContext context = SpringApplication.run(A01.class, args);


        /*
            1. 到底什么是 BeanFactory
                - 它是 ApplicationContext 的父接口
                - 它才是 Spring 的核心容器,  ApplicationContext内部组合一个BeanFactory对象，即BeanFactory是ApplicationContext是一个成员变量
         */
//
//        context.getBean("aaa");
//
        System.out.println(context);

//
//        /*
//            2. BeanFactory 能干点啥
//                - 表面上只有 getBean
//                - 实际上控制反转、基本的依赖注入、直至 Bean 的生命周期的各种功能, 都由它的实现类提供
//         */
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
//        map.forEach((k,v) -> System.out.println(k + "===" + v));

        System.out.println("============================");

        map.entrySet().stream().filter(e -> e.getKey().startsWith("component"))
                .forEach(e -> {
                    System.out.println(e.getKey() + "=" + e.getValue());
                });

        System.out.println("-----------------------------");

        /*
            3. ApplicationContext 比 BeanFactory 多点啥

            MessageSource： 国际化
         */
        System.out.println(context.getMessage("hi", null, Locale.CHINA));
        System.out.println(context.getMessage("hi", null, Locale.ENGLISH));
        System.out.println(context.getMessage("hi", null, Locale.JAPANESE));



        System.out.println("-----------------------------");


        System.out.println("-------读取资源  开始----------------------");
        // Resource：对资源文件的抽象
        // classpath表示从类路径下寻找资源，file表示从磁盘路径下寻找资源
        // ResourcePatternResolver
        Resource[] resources = context.getResources("classpath:application.properties");
        for (Resource resource : resources) {
            System.out.println(resource);
        }


        // classpath表示从类路径下寻找资源
        // classpath： 从类路径下找
        // classpath*： 从类路径下找，包括jar包
        Resource[] factories = context.getResources("classpath*:META-INF/spring.factories");
        for (Resource factorie : factories) {
            System.out.println(factorie);
        }


        System.out.println("-------读取资源  结束----------------------");



        System.out.println("-------配置信息  开始----------------------");

        System.out.println(context.getEnvironment().getProperty("java_home"));
        System.out.println(context.getEnvironment().getProperty("server.port"));

        System.out.println("-------配置信息  结束----------------------");



        System.out.println("-------发布事件  开始----------------------");
//        context.publishEvent(new UserRegisteredEvent(context));
        context.getBean(Component1.class).register();

        /*
            4. 学到了什么
                a. BeanFactory 与 ApplicationContext 并不仅仅是简单接口继承的关系, ApplicationContext 组合并扩展了 BeanFactory 的功能
                b. 又新学一种代码之间解耦途径
            练习：完成用户注册与发送短信之间的解耦, 用事件方式、和 AOP 方式分别实现
         */




    }
}
