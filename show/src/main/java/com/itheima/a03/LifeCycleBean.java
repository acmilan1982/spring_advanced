package com.itheima.a03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class LifeCycleBean {
    private static final Logger log = LoggerFactory.getLogger(LifeCycleBean.class);

    public LifeCycleBean() {
        log.debug("1-构造");
    }

    @Autowired
    public void autowire(@Value("${JAVA_HOME}") String home) {  // 值的注入，来源于配置文件或者环境变量
        log.debug("2-依赖注入: {}", home);
    }

    @PostConstruct
    public void init() {
        log.debug("3-初始化");
    }

    @PreDestroy
    public void destroy() {
        log.debug("4-销毁");
    }
}
