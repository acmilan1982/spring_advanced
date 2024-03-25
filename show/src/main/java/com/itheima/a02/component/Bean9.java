package com.itheima.a02.component;

import org.springframework.boot.context.properties.ConfigurationProperties;

/*
    java.home=
    java.version=
 */
//@ConfigurationProperties(prefix = "java")
public class Bean9 {

    private String home;

    private String version;

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Bean4{" +
               "home='" + home + '\'' +
               ", version='" + version + '\'' +
               '}';
    }
}
