package com.anliantest.third.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.redis.service.RedisService;
import com.anliantest.third.constant.AmapConstants;
import com.anliantest.third.service.IAmapApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Description 高德API对接
 * @Date 2023/8/25 14:10
 * @Author maoly
 **/
@RestController
@RequestMapping("/amap")
@Slf4j
public class AmapApiController {



    @Autowired
    private RedisService redisService;

    @Autowired
    private IAmapApiService amapApiService;

    @GetMapping("synchronizationData")
    public R synchronizationData(){
        amapApiService.synchronizationData();
        String redis_key_lock = AmapConstants.REDIS_KEY_LOCK;
        if(redisService.hasKey(redis_key_lock)){
            if(redisService.getCacheObject(redis_key_lock)){
               return R.fail("1小时内已更新过，请勿重复更新！");
            }
        }else{
            redisService.setCacheObject(redis_key_lock,true,60L, TimeUnit.MINUTES);
        }
        return R.ok("同步全国地址数据成功");
    }
}
