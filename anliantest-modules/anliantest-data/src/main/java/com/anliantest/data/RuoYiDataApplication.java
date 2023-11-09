package com.anliantest.data;

import com.anliantest.common.security.annotation.EnableCustomConfig;
import com.anliantest.common.security.annotation.EnableRyFeignClients;
import com.anliantest.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * 系统模块
 * 
 * @author ruoyi
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
@ComponentScans(value = {@ComponentScan("com.anliantest.system"),@ComponentScan("com.anliantest.message.api")})
public class RuoYiDataApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiDataApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  数据服务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
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
