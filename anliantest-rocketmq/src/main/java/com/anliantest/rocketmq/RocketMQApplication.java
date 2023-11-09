package com.anliantest.rocketmq;

import com.anliantest.common.security.annotation.EnableCustomConfig;
import com.anliantest.common.security.annotation.EnableRyFeignClients;
import com.anliantest.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * rocketMQ管理模块
 *
 * @author ruoyi
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
@EnableAsync
@ComponentScans(value = {@ComponentScan("com.anliantest.*")})
public class RocketMQApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RocketMQApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  RocketMQ模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
