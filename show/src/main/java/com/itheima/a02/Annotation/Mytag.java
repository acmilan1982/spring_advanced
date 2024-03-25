package com.itheima.a02.Annotation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Mytags.class)
public @interface Mytag {
    String value() default "0";
}
