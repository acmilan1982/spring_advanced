package com.itheima.a02.Annotation;


import com.itheima.a02.Annotation.Base;
import com.itheima.a02.Annotation.Mytag;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Mytag("aa")
@Mytag("bb")
@Configuration
@ComponentScan(basePackages = "com.itheima.a02.component")
public class Sub extends Base {
}
