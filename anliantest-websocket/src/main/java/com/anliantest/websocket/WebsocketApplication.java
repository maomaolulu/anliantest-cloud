package com.anliantest.websocket;

import com.anliantest.common.security.annotation.EnableCustomConfig;
import com.anliantest.common.security.annotation.EnableRyFeignClients;
import com.anliantest.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * websocket管理模块
 * 
 * @author ruoyi
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WebsocketApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(WebsocketApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Websocket模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
