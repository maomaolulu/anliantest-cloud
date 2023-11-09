package com.anliantest.file;

import com.anliantest.common.security.annotation.EnableCustomConfig;
import com.anliantest.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import com.anliantest.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * 文件服务
 *
 * @author ruoyi
 */
@EnableCustomSwagger2
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableCustomConfig
@EnableRyFeignClients
@ComponentScans(value = {@ComponentScan("com.anliantest.system.api.factory"),@ComponentScan("com.anliantest.rocketmq.api.factory")})
public class RuoYiFileApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiFileApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  文件服务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}
