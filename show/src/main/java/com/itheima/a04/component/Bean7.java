package com.itheima.a04.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


public class Bean7 {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${JAVA_HOME}")
    String username;

    @Value("${username}")
    String user;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
