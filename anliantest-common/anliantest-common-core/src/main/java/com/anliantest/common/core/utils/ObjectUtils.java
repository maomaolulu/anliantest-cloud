package com.anliantest.common.core.utils;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * 类型转换工具类
 * @author gy
 * @date 2023-06-08 18:31
 */
public class ObjectUtils {
    private ObjectUtils(){

    }

    /**
     * 通过JSON实现简单类型转换(实体类)
     * @param o
     * @param outClass
     * @return outClass类型的Object
     */
    public static <T> T transformObj(Object o,Class<T> outClass){
        return JSON.parseObject(JSON.toJSONString(o), outClass);
    }

    /**
     * 通过JSON实现简单类型转换(数组)
     * @param in
     * @param outClass
     * @return outClass类型的Object
     */
    public static <T> List<T> transformArrObj(Object in, Class<T> outClass) {
        return JSON.parseArray(JSON.toJSONString(in), outClass);
    }

}
