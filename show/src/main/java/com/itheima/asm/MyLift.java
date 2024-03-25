package com.itheima.asm;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component("test")
@ComponentScan(basePackages = "com.org.asm")
public class MyLift {

    private String name;

    private static Integer age=27;
}