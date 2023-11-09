package com.anliantest.rocketmq.service.impl;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.rocketmq.service.IConsumerService;
import com.anliantest.system.api.RemoteChineseAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 服务消费端统一业务处理
 * @Date 2023/8/28 15:08
 * @Author maoly
 **/
@Service
public class ConsumerServiceImpl implements IConsumerService {

    @Autowired
    private RemoteChineseAddressService remoteChineseAddressService;

    @Override
    public void saveChineseAddress(String body) {
        remoteChineseAddressService.saveChineseAddress(body, SecurityConstants.INNER);
    }
}
