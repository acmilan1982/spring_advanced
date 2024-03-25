package com.itheima.a23;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.util.Arrays;
import java.util.Date;

public class TestServletDataBinderFactory {
    public static void main(String[] args) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("birthday", "1999|01|02");  // 使用默认的绑定器不能绑定 自定义日期格式
        request.setParameter("address.name", "西安");     // 使用默认的绑定器能绑定

        User target = new User();
        // "1. 用工厂来创建 DataBinder,有更多的选项，可以选择 ConversionService 或 PropertyEditorRegistry方式进行扩展
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, null);


        // "2. 扩展方式一： 用 @InitBinder 转换"      本质上使用    PropertyEditorRegistry PropertyEditor，ServletRequestDataBinderFactory的第一个参数
        // @InitBinder注解的方法
//        InvocableHandlerMethod method = new InvocableHandlerMethod(new MyController(), MyController.class.getMethod("aaa", WebDataBinder.class));
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(Arrays.asList(method), null);

        // "3. 扩展方式二： 用 ConversionService 转换"    ConversionService Formatter，ServletRequestDataBinderFactory的第二个参数
//        FormattingConversionService service = new FormattingConversionService();
//        service.addFormatter(new MyDateFormatter("用 ConversionService 方式扩展转换功能"));
//        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
//        initializer.setConversionService(service);
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, initializer);

        // "4.扩展方式 三 同时加了 @InitBinder 和 ConversionService"
//        InvocableHandlerMethod method = new InvocableHandlerMethod(new MyController(), MyController.class.getMethod("aaa", WebDataBinder.class));
//
//        FormattingConversionService service = new FormattingConversionService();
//        service.addFormatter(new MyDateFormatter("用 ConversionService 方式扩展转换功能"));
//        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
//        initializer.setConversionService(service);

//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(Arrays.asList(method), initializer);

        // 5. 使用默认 ConversionService 转换，，已经内置的特殊日期的转换器
        //springboot中使用
//        ApplicationConversionService service = new ApplicationConversionService();

        // spring mvc中使用，需要在属性上提供注解： @DateTimeFormat(pattern = "yyyy|MM|dd")
        DefaultFormattingConversionService service = new DefaultFormattingConversionService();
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(service);

        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, initializer);
        //-------------------------------------------



        WebDataBinder dataBinder = factory.createBinder(new ServletWebRequest(request), target, "user");
        dataBinder.bind(new ServletRequestParameterPropertyValues(request));
        System.out.println(target);
    }

    static class MyController {
        @InitBinder // 这些Binder给 ServletRequestDataBinderFactory 用，第一个参数
        public void aaa(WebDataBinder dataBinder) {  // 方法参数必须是 WebDataBinder 类型，方法内部可以扩展 WebDataBinder的功能
            // 扩展 dataBinder 的转换器
            dataBinder.addCustomFormatter(new MyDateFormatter("用 @InitBinder 方式扩展的"));
        }
    }

    public static class User {
        @DateTimeFormat(pattern = "yyyy|MM|dd")
        private Date birthday;
        private Address address;

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        @Override
        public String toString() {
            return "User{" +
                   "birthday=" + birthday +
                   ", address=" + address +
                   '}';
        }
    }

    public static class Address {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Address{" +
                   "name='" + name + '\'' +
                   '}';
        }
    }
}
