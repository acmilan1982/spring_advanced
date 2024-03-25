package com.itheima.a20;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class A20 {
    private static final Logger log = LoggerFactory.getLogger(A20.class);

    public static void main(String[] args) throws Exception {
        // 内嵌tomcat
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);


        // 作用 解析 @RequestMapping 以及派生注解，生成路径与控制器方法的映射关系, 在该bean初始化时就生成
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
//
        // 获取映射结果,k是与路径，请求方式等相关的信息
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        System.out.println("遍历handlerMapping的内容 ---------------------->");
        handlerMethods.forEach((k, v) -> {
            System.out.println(k + "=" + v);
        });
        System.out.println("遍历handlerMapping的内容 end     ---------------------->");
//
//
//
//        // 模拟请求，请求来了，获取控制器方法  返回处理器执行链对象
//        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test1");
//     //   MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test2");
//        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/test3");
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/test4");
//        request.setParameter("name", "张三");
        request.addHeader("token", "某个令牌");
        MockHttpServletResponse response = new MockHttpServletResponse();
//
//
        // Handler的执行链，包括拦截器
        HandlerExecutionChain chain = handlerMapping.getHandler(request);
        System.out.println("HandlerExecutionChain --->: " + chain);
//
//
//
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>");
//
        // HandlerAdapter 作用: 调用控制器方法
        MyRequestMappingHandlerAdapter handlerAdapter = context.getBean(MyRequestMappingHandlerAdapter.class);
        //RequestMappingHandlerAdapter的核心方法：invokeHandlerMethod
        handlerAdapter.invokeHandlerMethod(request, response, (HandlerMethod) chain.getHandler());
//
//        // 检查响应
//        byte[] content = response.getContentAsByteArray();
//        System.out.println(new String(content, StandardCharsets.UTF_8));




        System.out.println(">>>>>>>>>>>>>>>>>>>>> 所有参数解析器");
        for (HandlerMethodArgumentResolver resolver : handlerAdapter.getArgumentResolvers()) {
            System.out.println(resolver);
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>> 所有参数解析器  end");


        System.out.println(">>>>>>>>>>>>>>>>>>>>> 所有返回值解析器");
        for (HandlerMethodReturnValueHandler handler : handlerAdapter.getReturnValueHandlers()) {
            System.out.println(handler);
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>> 所有返回值解析器  end");





         // 检查 自定义返回值解析器 的响应
         byte[] content = response.getContentAsByteArray();
         System.out.println(new String(content, StandardCharsets.UTF_8));



        /*
        学到了什么
            a. DispatcherServlet 是在第一次被访问时执行初始化, 也可以通过配置修改为 Tomcat 启动后就初始化
            b. 在初始化时会从 Spring 容器中找一些 Web 需要的组件, 如 HandlerMapping、HandlerAdapter 等

        本章介绍两个最为重要的组件
            a. RequestMappingHandlerAdapter, 以 @RequestMapping 作为映射路径
            b. RequestMappingHandlerAdapter, 调用 handler
            c. 控制器的具体方法会被当作 handler
                - handler 的参数和返回值多种多样
                - 需要解析方法参数, 由 HandlerMethodArgumentResolver 来做
                - 需要处理方法返回值, 由 HandlerMethodReturnValueHandler 来做
        */



    }


}
