package com.itheima.a05;

import com.itheima.a05.component.Bean1;
import com.itheima.a05.component.Bean4;
import com.itheima.a05.mapper.Mapper1;
import com.itheima.a05.mapper.Mapper2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

/*
    BeanFactory 后处理器的作用
 */
public class A05 {
    private static final Logger log = LoggerFactory.getLogger(A05.class);

    public static void main(String[] args) throws IOException {

        // ⬇️GenericApplicationContext 是一个【干净】的容器
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class); // 解析注解：@ComponentScan @Bean @Import @ImportResource




//
//
//
//          // ----ConfigurationClassPostProcessor的模拟实现，解析@ComponentScan 开始----------------------
////
////        // 判断指定类上是否存在指定的注解(ComponentScan)
//
//        ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
//
//        if (componentScan != null) {
//             //  遍历@ComponentScan的属性值
//            for (String p : componentScan.basePackages()) {
//                System.out.println(p);
//
//                // 包名，转换成文件路径
//                // com.itheima.a05.component -> classpath*:com/itheima/a05/component/**/*.class
//                String path = "classpath*:" + p.replace(".", "/") + "/**/*.class";
//                System.out.println(path);
//
////                // 读取指定目录下的所有class
//                Resource[] resources = context.getResources(path);
//                for (Resource resource : resources) {
//                    System.out.println(resource);
//                }
//
//                //  读取类的元信息
//                CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
////                Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
//
////              //生成bean的名字
//                AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
//                for (Resource resource : resources) {
//                    System.out.println("---------------------");
//                    System.out.println(resource);
//                    // 读取类的注解信息
//                    MetadataReader reader = factory.getMetadataReader(resource);
//                     // 打印类的信息
//                    System.out.println("类名:" + reader.getClassMetadata().getClassName());
//
//                     // class的注解信息
//                    AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
//                    // 直接注解
//                    System.out.println("是否加了 @Component:" + annotationMetadata.hasAnnotation(Component.class.getName()));
//                    // 注解的注解
//                    System.out.println("是否加了 @Component 派生:" + annotationMetadata.hasMetaAnnotation(Component.class.getName()));
//
//                    // 如果直接或间接加了@Component
//                    if (annotationMetadata.hasAnnotation(Component.class.getName())
//                            || annotationMetadata.hasMetaAnnotation(Component.class.getName())) { // hasMetaAnnotation：判断注解的注解
//                        AbstractBeanDefinition bd = BeanDefinitionBuilder.genericBeanDefinition(reader.getClassMetadata().getClassName())
//                                                                         .getBeanDefinition();
//
//
//                        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
//                        String name = generator.generateBeanName(bd, beanFactory);
//                        beanFactory.registerBeanDefinition(name, bd);
//                    }
//                }
//            }
//        }
//
//        // ----ConfigurationClassPostProcessor的模拟实现，解析@ComponentScan          结束---------------------------------------
//        //mybatis相关
////        context.registerBean(MapperScannerConfigurer.class, bd -> { // @MapperScanner
////            bd.getPropertyValues().add("basePackage", "com.itheima.a05.mapper");
////        });
//
//
//
//        // ----ConfigurationClassPostProcessor的模拟实现，解析 @bean 开始----------------------
//
//        CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
//        MetadataReader reader = factory.getMetadataReader(new ClassPathResource("com/itheima/a05/Config.class")); //MetadataReader: ASM实现
//
//        // 被@Bean的方法
//        Set<MethodMetadata> methods = reader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
//
//        for (MethodMetadata method : methods) {
//
//
//            System.out.println("被@bean的方法: " + method);
//
//            // 获取@Bean的属性：initMethod
//            String initMethod = method.getAnnotationAttributes(Bean.class.getName()).get("initMethod").toString();
//
//
//            // 每一个方法对应一个BeanDefinition
//            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
//            // 指定工厂，工厂方法
//            builder.setFactoryMethodOnBean(method.getMethodName(), "config");
//
//            // 指定工厂方法的参数，使用自动装配，主要是为了sqlSessionFactoryBean上的参数
//            builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);  // 没有该行会报错
//
//            if (initMethod.length() > 0) {
//                builder.setInitMethodName(initMethod);
//            }
//            AbstractBeanDefinition bd = builder.getBeanDefinition();
//            // 方法名作为beanname
//            context.getDefaultListableBeanFactory().registerBeanDefinition(method.getMethodName(), bd);
//        }
//          // ----ConfigurationClassPostProcessor的模拟实现，解析 @bean 结束----------------------
//
//
//


//        context.registerBean(ComponentScanPostProcessor.class); // 模拟实现，用来解析 @ComponentScan
//        context.registerBean(AtBeanPostProcessor.class);        // 模拟实现，用来解析 @Bean

//        context.registerBean(AtBeanPostProcessor.class); // 解析 @Bean
//        context.registerBean(MapperPostProcessor.class); // 解析 Mapper 接口


//        context.registerBean(AutowiredAnnotationBeanPostProcessor.class); // 用来解析@Autowired @Value
        // ⬇️初始化容器
        context.refresh();

        System.out.println("当前容器有哪些BeanDefinition：");
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
//
//        Mapper1 mapper1 = context.getBean(Mapper1.class);
//        Mapper2 mapper2 = context.getBean(Mapper2.class);


        System.out.println("bean1中的bean2------------->" + context.getBean(Bean1.class).getBean2());

        // ⬇️销毁容器
        context.close();

        /*
            学到了什么
                a. @ComponentScan, @Bean, @Mapper 等注解的解析属于核心容器(即 BeanFactory)的扩展功能
                b. 这些扩展功能由不同的 BeanFactory 后处理器来完成, 其实主要就是补充了一些 bean 定义
         */
    }
}
