package com.anliantest.data.constant;

/**
 * 调用第三方url常量池
 *
 * @Author yrb
 * @Date 2023/8/17 10:24
 * @Version 1.0
 * @Description 调用第三方url常量池
 */
public interface UrlConstants {
    /**
     * 同步仪器设备信息到OA-测试
     */
    String OA_EQUIP_TEST = "http://192.168.0.204:9527/daily/asset/add";
    /**
     * 同步仪器设备信息到OA-正式
     */
    String OA_EQUIP_ONLINE = "http://192.168.0.105:9527/daily/asset/add";
}
