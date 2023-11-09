package com.anliantest.third.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.enums.rocketmq.MessageCodeEnum;
import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.common.redis.service.RedisService;
import com.anliantest.rocketmq.api.RemoteRocketMqSendService;
import com.anliantest.rocketmq.api.domain.RocketMqSend;
import com.anliantest.system.api.model.AmapData;
import com.anliantest.third.constant.AmapConstants;
import com.anliantest.third.service.IAmapApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Date 2023/8/30 10:39
 * @Author maoly
 **/
@Service
@Slf4j
public class AmapApiServiceImpl implements IAmapApiService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RemoteRocketMqSendService remoteRocketMqSendService;

    @Value("${amap.api.url}")
    private String amapApiUrl;

    @Override
    @Async
    public void synchronizationData() {
        String chineseAddress = HttpUtil.createGet(amapApiUrl).execute().body();
        if(StringUtils.isNotBlank(chineseAddress)){
            AmapData amapData = JSONObject.parseObject(chineseAddress, AmapData.class);
            if(amapData != null){
                if(amapData.getInfocode().equals(AmapConstants.INFO_CODE)){
                    String redis_key = AmapConstants.REDIS_KEY;
                    if(redisService.hasKey(redis_key)){
                        redisService.deleteObject(redis_key);
                    }
                    redisService.setCacheObject(redis_key,amapData,60L, TimeUnit.SECONDS);
                    String topic = MessageCodeEnum.THIRD_MESSAGE.getCode();
                    String tag = MessageCodeEnum.THIRD_ADDRESS_MESSAGE_TAG.getCode();
                    remoteRocketMqSendService.sendSynchronizeMessage(RocketMqSend.builder().topic(topic).tag(tag).messageText(redis_key).build(),
                            SecurityConstants.INNER);
                }else{
                    log.info("高德API拉取全国数据失败，status={}，infocode={}",amapData.getStatus(),amapData.getInfocode());
                }
            }
        }
    }
}
