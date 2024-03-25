package com.itheima.a02;



import com.itheima.a02.component.Bean7;
import com.itheima.a02.component.Component1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

public class TestBeanFactory_AnnotationConfigApplicationContext {

    public static void main(String[] args) throws Exception {


        //AnnotationConfigApplicationContext加载Spring配置类初始化Spring容器
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);

        System.out.println("-----------------------------");
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);
        ConfigurableListableBeanFactory beanFactory = ctx.getBeanFactory();

        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
        map.entrySet().stream()
                .forEach(e -> {
                    System.out.println(e.getKey() + "=" + e.getValue());
                    System.out.println("---");
                });

        System.out.println("-----------------------------");

        System.out.println("-------读取资源  开始----------------------");
        // Resource：对资源文件的抽象
        // classpath表示从类路径下寻找资源
        // ResourcePatternResolver
        org.springframework.core.io.Resource[] resources = ctx.getResources("classpath:application.properties");
        for (org.springframework.core.io.Resource resource : resources) {
            System.out.println(resource);
        }


        // classpath表示从类路径下寻找资源
        // classpath： 从类路径下找
        // classpath*： 从类路径下找，包括jar包
        org.springframework.core.io.Resource[] factories = ctx.getResources("classpath*:META-INF/spring.factories");
        for (Resource factorie : factories) {
            System.out.println(factorie);
        }


        System.out.println("-------读取资源  结束----------------------");

        System.out.println(ctx.getEnvironment().getProperty("java_home"));
        System.out.println(ctx.getEnvironment().getProperty("server.port"));


        Bean7 Bean7 = ctx.getBean(Bean7.class);

        System.out.println(Bean7.getUrl());
        System.out.println(Bean7.getUsername());
        System.out.println(Bean7.getUser());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");

        ctx.getBean(Component1.class).register();

    }
}
