package com.itheima.a02;

import com.itheima.a02.component.Bean7;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.security.Key;

public class TestBeanFactory {

    public static void main(String[] args) {

//        Annotation[] annotations = Config.class.getDeclaredAnnotations();

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver()); // @Value相关,获取@Value中指定的值
//
        // bean 的定义，包括（class, scope, 初始化, 销毁），BeanFactory根据BeanDefinition去实例化对象
        AbstractBeanDefinition beanDefinition =
                BeanDefinitionBuilder.genericBeanDefinition(config.class).setScope("singleton").getBeanDefinition();
        beanFactory.registerBeanDefinition("config", beanDefinition);

        System.out.println("纯粹的DefaultListableBeanFactory，只有一些主动注册的bean，不包含任何 后处理器------------------------------------>");
        // 纯粹的DefaultListableBeanFactory，只有一些主动注册的bean，不包含任何 后处理器
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }



        System.out.println("主动注册------------------------------------>");
        // 给 BeanFactory 添加一些常用的后处理器(BeanFactoryPostProcessor),这些后处理器，可以对 DefaultListableBeanFactory 做一些功能扩展
        // 这里仅仅注册了BeanFactoryPostProcessor，还未执行BeanFactoryPostProcessor
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);

        for (String name : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition definition = beanFactory.getBeanDefinition(name);
            System.out.println(name + "------------>" + definition.getBeanClassName());


        }


        // BeanFactory 后处理器主要功能，补充了一些 bean 定义
        // 执行 BeanFactoryPostProcessor，处理@Configuration，@Bean等
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().forEach(
                beanFactoryPostProcessor -> beanFactoryPostProcessor.postProcessBeanFactory(beanFactory)
        );

        System.out.println("BeanFactoryPostProcessor处理之后:------------------------------------>");
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        //--注意，此时，bean之间的依赖关系还未建立
        // 此时，bean1中的bean2是null
        System.out.println("beanFactory.getBean(Bean1.class).getBean2() ------------------------->" + beanFactory.getBean(Bean1.class).getBean2());


        // // internalConfigurationAnnotationProcessor 解析 @Autowired
        // internalCommonAnnotationProcessor 解析 @Resource
        // bean的后处理器-BeanPostProcessor
        // BeanPostProcessor, 针对 bean 的生命周期的各个阶段提供扩展, 例如 @Autowired @Resource ...
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().stream()
                 .sorted(beanFactory.getDependencyComparator())
                .forEach(beanPostProcessor -> {
                    System.out.println("注册---BeanPostProcessor>>>>" + beanPostProcessor);
                    beanFactory.addBeanPostProcessor(beanPostProcessor);
                });

        System.out.println("注册---------end");

        // 此时，bean1中的bean2有值了
        System.out.println(beanFactory.getBean(Bean1.class).getBean2());


//        for (String name : beanFactory.getBeanDefinitionNames()) {
//            System.out.println(name);
//        }

        // 准备好所有单例，初始化所有单例
        beanFactory.preInstantiateSingletons();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
        System.out.println(beanFactory.getBean(Bean1.class).getBean2());


        /*
//        beanFactory.getBeansOfType(BeanPostProcessor.class).values().stream()
//                // .sorted(beanFactory.getDependencyComparator())
//                .forEach(beanPostProcessor -> {
//                    System.out.println(">>>>" + beanPostProcessor);
//                    beanFactory.addBeanPostProcessor(beanPostProcessor);
//                });


        System.out.println(beanFactory.getBean(Bean1.class).getInter());



/*

        AbstractBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(Config.class);
        beanFactory.registerBeanDefinition("config", beanDefinition);

        System.out.println("仅有DefaultListableBeanFactory---------begin");
        // 纯粹的DefaultListableBeanFactory，只有一些主动注册的bean，不包含任何 后处理器
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        System.out.println("已持有后处理器");

        beanFactory.getBeansOfType(BeanPostProcessor.class).values().stream()
               // .sorted(beanFactory.getDependencyComparator())
                .forEach(beanPostProcessor -> {
                    System.out.println(">>>>" + beanPostProcessor);
                    beanFactory.addBeanPostProcessor(beanPostProcessor);
                });



        System.out.println("仅有DefaultListableBeanFactory---------end");


        // 给 BeanFactory 添加一些常用的后处理器,这些后处理器，可以对 DefaultListableBeanFactory 做一些功能扩展
        // 这里指数注册了
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        beanFactory.registerBeanDefinition("org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor",new RootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver()); // @Value相关,获取@Value中指定的值
        System.out.println("注册---------begin");
        // BeanFactory 后处理器主要功能，补充了一些 bean 定义
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().forEach(beanFactoryPostProcessor -> {
            System.out.println("注册---BeanFactoryPostProcessor>>>>" + beanFactoryPostProcessor);
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });


        // internalConfigurationAnnotationProcessor 解析了 Config中的 bean1,bean2，即解析注解@Bean
        // beanFactory的后处理器


//
//        for (String name :beanFactory.getBeanDefinitionNames()) {
//            System.out.println(name);
//        }
//
//        System.out.println(beanFactory.getBean(Bean1.class).getBean2());


        // // internalConfigurationAnnotationProcessor 解析 @Autowired
        // internalCommonAnnotationProcessor 解析 @Resource
        // bean的后处理器
        // Bean 后处理器, 针对 bean 的生命周期的各个阶段提供扩展, 例如 @Autowired @Resource ...
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().stream()
                .sorted(beanFactory.getDependencyComparator())
                .forEach(beanPostProcessor -> {
            System.out.println("注册---BeanPostProcessor>>>>" + beanPostProcessor);
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        });

        System.out.println("注册---------end");

        System.out.println("已经注册的bean:");
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        System.out.println("  ");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");


//        System.out.println(beanFactory.getBean(Bean1.class).getBean2());

        beanFactory.preInstantiateSingletons(); // 准备好所有单例，初始化所有单例
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");

//        System.out.println(beanFactory.getBean(Bean1.class).getBean2());
//        System.out.println(beanFactory.getBean(Bean1.class));
//        System.out.println(beanFactory.getBean(Bean1.class).getInter());

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");

        System.out.println(beanFactory.getBean(Bean7.class).getUrl());
        System.out.println(beanFactory.getBean(Bean7.class).getUsername());
        System.out.println(beanFactory.getBean(Bean7.class).getUser());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");


        /*
            学到了什么:
            a. beanFactory 不会做的事
                   1. 不会主动调用 BeanFactory 后处理器
                   2. 不会主动添加 Bean 后处理器
                   3. 不会主动初始化单例
                   4. 不会解析beanFactory 还不会解析 ${ } 与 #{ }，占位符.EL表达式
            b. bean 后处理器会有排序的逻辑
         */

//        System.out.println("Common:" + (Ordered.LOWEST_PRECEDENCE - 3));
//        System.out.println("Autowired:" + (Ordered.LOWEST_PRECEDENCE - 2));


    }

    @Configuration
    static class config {

        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }

        @Bean
        public Bean3 bean3() {
            return new Bean3();
        }

        @Bean
        public Bean4 bean4() {
            return new Bean4();
        }
    }

    interface Inter {

    }

    static class Bean3 implements Inter {

    }

    static class Bean4 implements Inter {

    }

    static class Bean1 {
        private static final Logger log = LoggerFactory.getLogger(Bean1.class);

        public Bean1() {
            log.debug("构造 Bean1()");
        }

        @Autowired
        private Bean2 bean2;

        public Bean2 getBean2() {
            return bean2;
        }

        @Autowired
        @Resource(name = "bean4")
        private Inter bean3;   // 如果没有@Resource，会进行 成员变量 的名字和类名的匹配

        public Inter getInter() {
            return bean3;
        }


//        @Autowired
//////        @Resource(name = "bean4")
//        private Inter inter;
//
//        public Inter getInter() {
//            return inter;
//        }

    }

    static class Bean2 {
        private static final Logger log = LoggerFactory.getLogger(Bean2.class);

        public Bean2() {
            log.debug("构造 Bean2()");
        }
    }
}
