package com.itheima.a20.bridge;

import java.lang.reflect.Method;

public class BridgeTest {
    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> clazz = Class.forName("com.itheima.a20.bridge.UserInfoOperator");
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            System.out.println(method);
        }



    }
}
