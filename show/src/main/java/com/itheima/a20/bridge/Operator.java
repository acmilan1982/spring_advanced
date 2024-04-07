package com.itheima.a20.bridge;

/**
 * @author renzhiqiang
 * @date 2022/2/20 18:30
 */
public interface Operator<T> {
    /**
     * process method
     * @param t
     */
    void process(T t);
}