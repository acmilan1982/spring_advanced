package com.itheima.a05.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;
import java.util.HashMap;

// 准备一个Class类 作为Demo演示
//@Repository("bsoft_repository")
@Service("service_Name")
@ComponentScan(basePackages = "com.bsoft")
@EnableAsync
@ImportResource

public
class MetaDemo extends HashMap<String, String> implements Serializable {
    private static class InnerClass {
    }

//    @Autowired
//    @PostMapping(path = "/fuck")
    @RequestMapping(path = "/fuck")
    private String getName() {
        return "demo";
    }

    @PostMapping("/home")
    private String getName2() {
        return "demo";
    }
}