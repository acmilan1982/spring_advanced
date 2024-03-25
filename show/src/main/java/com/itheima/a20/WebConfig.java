package com.itheima.a20;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan  // 默认扫描配置类所在的包，以及其子包
@PropertySource("classpath:application.properties")  // 读取类路径下的配置文件
@EnableConfigurationProperties({WebMvcProperties.class, ServerProperties.class})   // 声明使用什么对象来绑定配置文件中指定的key(前缀)
public class WebConfig {
    // ⬅️内嵌 web 容器工厂
    @Bean
//    @RequestMapping
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(ServerProperties serverProperties) {
        return new TomcatServletWebServerFactory(serverProperties.getPort()
        );
    }

    // ⬅️创建 DispatcherServlet, DispatcherServlet是spring创建的，但在第一次使用时，被tomcat初始化
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    // ⬅️注册bean，用来向tomcat注册 DispatcherServlet, Spring MVC 的入口
    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet, WebMvcProperties webMvcProperties) {
        // 注册哪个bean，Servlet负责的请求路径
        DispatcherServletRegistrationBean registrationBean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        // 表示tomcat启动时就初始化 DispatcherServlet
        // 数字代表 众多servlet 初始化的优先级，小的数字优先级高
//        registrationBean.setLoadOnStartup(1);
        registrationBean.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());
        return registrationBean;
    }


    //--------------------------------------------------------------------------------------------------

    // 如果使用 DispatcherServlet 初始化时添加的默认组件,这些组件仅仅作为 DispatcherServlet的实例属性
    // 并不会作为 bean 被spring管理, 给测试带来困扰
    // ⬅️1. 加入RequestMappingHandlerMapping
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    // ⬅️2. 继续加入RequestMappingHandlerAdapter, 会替换掉 DispatcherServlet 默认的 4 个 HandlerAdapter
//    @Bean
//    public MyRequestMappingHandlerAdapter requestMappingHandlerAdapter() {
//        return  new MyRequestMappingHandlerAdapter();
//    }





    @Bean
    public MyRequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        MyRequestMappingHandlerAdapter handlerAdapter = new MyRequestMappingHandlerAdapter();


        ArrayList<HandlerMethodArgumentResolver> argumentResolvers  = new ArrayList<>();
        TokenArgumentResolver tokenArgumentResolver = new TokenArgumentResolver();
        argumentResolvers.add(tokenArgumentResolver);


        ArrayList<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers  = new ArrayList<>();
        YmlReturnValueHandler ymlReturnValueHandler = new YmlReturnValueHandler();
        handlerMethodReturnValueHandlers.add(ymlReturnValueHandler);

        handlerAdapter.setCustomArgumentResolvers(argumentResolvers);
        handlerAdapter.setCustomReturnValueHandlers(handlerMethodReturnValueHandlers);
        return handlerAdapter;
    }
//
//    public HttpMessageConverters httpMessageConverters() {
//        return new HttpMessageConverters();
//    }
//
//    // ⬅️3. 演示 RequestMappingHandlerAdapter 初始化后, 有哪些参数、返回值处理器
//
//    // ⬅️3.1 创建自定义参数处理器
//
//    // ⬅️3.2 创建自定义返回值处理器

}
