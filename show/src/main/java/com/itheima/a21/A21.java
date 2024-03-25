package com.itheima.a21;

import org.apache.tomcat.util.digester.ArrayStack;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
    目标: 解析控制器方法的参数值

    常见的参数处理器如下:
        org.springframework.web.method.annotation.RequestParamMethodArgumentResolver@abbc908
        org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver@44afefd5
        org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver@9a7a808
        org.springframework.web.servlet.mvc.method.annotation.PathVariableMapMethodArgumentResolver@72209d93
        org.springframework.web.servlet.mvc.method.annotation.MatrixVariableMethodArgumentResolver@2687f956
        org.springframework.web.servlet.mvc.method.annotation.MatrixVariableMapMethodArgumentResolver@1ded7b14
        org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor@29be7749
        org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor@5f84abe8
        org.springframework.web.servlet.mvc.method.annotation.RequestPartMethodArgumentResolver@4650a407
        org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver@30135202
        org.springframework.web.method.annotation.RequestHeaderMapMethodArgumentResolver@6a4d7f76
        org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver@10ec523c
        org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver@53dfacba
        org.springframework.web.servlet.mvc.method.annotation.SessionAttributeMethodArgumentResolver@79767781
        org.springframework.web.servlet.mvc.method.annotation.RequestAttributeMethodArgumentResolver@78411116
        org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver@aced190
        org.springframework.web.servlet.mvc.method.annotation.ServletResponseMethodArgumentResolver@245a060f
        org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor@6edaa77a
        org.springframework.web.servlet.mvc.method.annotation.RedirectAttributesMethodArgumentResolver@1e63d216
        org.springframework.web.method.annotation.ModelMethodProcessor@62ddd21b
        org.springframework.web.method.annotation.MapMethodProcessor@16c3ca31
        org.springframework.web.method.annotation.ErrorsMethodArgumentResolver@2d195ee4
        org.springframework.web.method.annotation.SessionStatusMethodArgumentResolver@2d6aca33
        org.springframework.web.servlet.mvc.method.annotation.UriComponentsBuilderMethodArgumentResolver@21ab988f
        org.springframework.web.servlet.mvc.method.annotation.PrincipalMethodArgumentResolver@29314cc9
        org.springframework.web.method.annotation.RequestParamMethodArgumentResolver@4e38d975
        org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor@35f8a9d3
 */
public class A21 {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        // 准备测试 Request
        HttpServletRequest request = mockRequest();

        // 要点1. 控制器方法被封装为 HandlerMethod
        HandlerMethod handlerMethod = new HandlerMethod(new Controller(), Controller.class.getMethod("test", String.class, String.class, int.class, String.class, MultipartFile.class, int.class, String.class, String.class, String.class, HttpServletRequest.class, User.class, User.class, User.class));

        // 要点2. 准备对象绑定与类型转换
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, null);

        // 要点3. 准备 ModelAndViewContainer 用来存储中间 Model 结果，某些参数解析器会用到这个 container
        ModelAndViewContainer container = new ModelAndViewContainer();

        // 要点4. 解析每个参数值
        for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
//            // 参数名解析器
//            parameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());
//            // 打印参数详细信息
//            System.out.println(parameter.getParameterIndex() + "-->"+
//                               parameter.getParameterName()  + "-->"  +
//                               parameter.getParameterType());
            String annotations = Arrays.stream(parameter.getParameterAnnotations()).map(a -> a.annotationType().getSimpleName()).collect(Collectors.joining());
            String str = annotations.length() > 0 ? " @" + annotations + " " : " ";
//            System.out.println(str);

            // 多个解析器组合
            HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
            composite.addResolvers(
//                  // 传入beanFactory，因为有些参数可能来源于 propertySource
                    new RequestParamMethodArgumentResolver(beanFactory, false),  //false 表示方法参数必须有 @RequestParam才解析，没有则不解析
                                                                                                //ture，表示如果方法参数不带 @RequestParam 的参数，也由当前 Resolver 来解析
                    new PathVariableMethodArgumentResolver(),         // 解析被 @PathVariable 的参数
                    new RequestHeaderMethodArgumentResolver(beanFactory),  // @RequestHeader
                    new ServletCookieValueMethodArgumentResolver(beanFactory),   //@CookieValue
                    new ExpressionValueMethodArgumentResolver(beanFactory),  // @Value，从容器中获取数据
                    new ServletRequestMethodArgumentResolver(),  // request, response, session ，没有注解，根据参数的类型来解析
                                                                 // 看他的 supportsParameter，可以解析多种类型


                    // 请求中的 名值对 与 参数的对象绑定，请求的参数名就是对象的属性， 对象会存入 ModelAndViewContainer
                    new ServletModelAttributeMethodProcessor(false), // false表示必须有 @ModelAttribute,处理带有@ModelAttribute的参数，

                    // 注意，必须在下一个 Resolver 之前
                    new RequestResponseBodyMethodProcessor(Arrays.asList(new MappingJackson2HttpMessageConverter())),


                    // 1.先处理没有 注解的 对象
                    new ServletModelAttributeMethodProcessor(true), // 省略了 @ModelAttribute，         处理不带有@ModelAttribute的参数

                    // 2.再处理没有 注解的 基本类型
                    new RequestParamMethodArgumentResolver(beanFactory, true) // 省略了 @RequestParam 的参数
            );
//


            // 参数名解析器
            parameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());
            // 判断当前解析器是否支持 解析该方法参数。遍历所有解析器，直接找到合适的
            if (composite.supportsParameter(parameter)) {
                // resolveArgument，解析当前方法参数
                Object v = composite.resolveArgument(parameter, container, new ServletWebRequest(request), factory);

                System.out.println("[" + parameter.getParameterIndex() + "] " + str + parameter.getParameterType().getSimpleName() + " " + parameter.getParameterName() + "---------------->" + v);
                System.out.println("解析结果的类型：" + v.getClass());  // 由 binderFactory 负责解析
                System.out.println("模型数据为：" + container.getModel());
            } else {
                System.out.println("[" + parameter.getParameterIndex() + "] " + str + parameter.getParameterType().getSimpleName() + " " + parameter.getParameterName());
            }






        }

        /*
            学到了什么
                a. 每个参数处理器能干啥
                    1) 看是否支持某种参数
                    2) 获取参数的值
                b. 组合模式在 Spring 中的体现
                c. @RequestParam, @CookieValue 等注解中的参数名、默认值, 都可以写成活的, 即从 ${ } #{ }中获取
         */
    }

    private static HttpServletRequest mockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name1", "zhangsan");
        request.setParameter("name2", "lisi");
        request.setParameter("home", "hz");
        request.addPart(new MockPart("file", "abc", "hello".getBytes(StandardCharsets.UTF_8)));
        Map<String, String> map = new AntPathMatcher().extractUriTemplateVariables("/test/{id}", "/test/123");
        System.out.println(map);
        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, map);
        request.setContentType("application/json");
        request.setCookies(new Cookie("token", "123456"));
        request.setParameter("name", "张三");
        request.setParameter("age", "18");
        request.setContent(new String("                    {\n" +
                "                        \"name\":\"李四\",\n" +
                "                        \"age\":20 \n" +
                "                    }").getBytes());


        return new StandardServletMultipartResolver().resolveMultipart(request);
    }

    static class Controller {
        public void test(
                @RequestParam("name1") String name1, // name1=张三， 解析请求中的 名=值 对，名=值对可以在请求行或请求体中
                String name2,                        // name2=李四  ，默认由 @RequestParam 解析
                @RequestParam("age") int age,        // age=18，涉及数据类型的转换
                @RequestParam(name = "home", defaultValue = "${JAVA_HOME}") String home1, // 如果请求中没有该参数，可以设置默认值(默认值可以是非请求中的数据，是容器中的数据)
                @RequestParam("file") MultipartFile file, // 上传文件
                @PathVariable("id") int id,               //  /test/124   /test/{id}，请求路径中的一部分，作为方法的参数
                @RequestHeader("Content-Type") String header,  // 请求头的数据
                @CookieValue("token") String token,
                @Value("${JAVA_HOME}") String home2, // 从spring中 获取数据  ${}：环境变量，配置文件等    #{}：EL表达式，    从容器中获取数据
                HttpServletRequest request,          // request, response, session ...  ，根据参数的类型来解析
                @ModelAttribute("abc") User user1,   // name=zhang&age=18        @ModelAttribute:请求中的 名值对 与 参数的对象绑定，请求中的参数名就是对象的属性，
                                                     // 处理的结果，作为模型数据，存到 ModelAndViewContainer
                User user2,                          // name=zhang&age=18，可以省略 @ModelAttribute() 注解
                @RequestBody User user3              // json  从请求体中获取json格式数据，然后封装成对象
        ) {
        }
    }

    static class User {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                   "name='" + name + '\'' +
                   ", age=" + age +
                   '}';
        }
    }
}
