package com.itheima.a05.self1;

import com.itheima.a05.self.MetaDemo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;


public class A53 {



    public static void main(String[] args) throws NoSuchMethodException {
        final RequestMapping annotation = AnnotationUtils.getAnnotation(
                CityController.class.getMethod("index", Model.class),
                RequestMapping.class
        );
        annotation.path();

    }
}
