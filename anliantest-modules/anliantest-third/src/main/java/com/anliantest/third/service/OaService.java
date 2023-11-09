package com.anliantest.third.service;

import java.util.Map;

/**
 * @Author yrb
 * @Date 2023/7/26 14:08
 * @Version 1.0
 * @Description OA系统服务层接口
 */
public interface OaService {
    /**
     * 获取OA用户信息
     * @param param 邮箱
     * @return result
     */
    Map<String, String> getUserInfo(Map<String,String> param);
}
