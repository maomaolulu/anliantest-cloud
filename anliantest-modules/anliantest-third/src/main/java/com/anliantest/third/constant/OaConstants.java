package com.anliantest.third.constant;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author yrb
 * @Date 2023/7/26 13:51
 * @Version 1.0
 * @Description OA系统常量池
 */
public interface OaConstants {
    @ApiModelProperty(value = "测试环境地址前缀")
    String URL_PRE_TEST = "http://192.168.0.204:9527";

    @ApiModelProperty(value = "正式环境地址前缀")
    String URL_PRE_ONLINE = "http://60.190.21.178:98";

    @ApiModelProperty(value = "免密登录秘钥")
    String SECTET = "d2e6fb1c22da59492f0ded58b4f9c2de";
}
