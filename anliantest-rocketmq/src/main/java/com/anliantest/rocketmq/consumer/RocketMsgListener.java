package com.anliantest.rocketmq.consumer;

import com.alibaba.fastjson.JSON;
import com.anliantest.common.core.constant.Constants;
import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.enums.rocketmq.MessageCodeEnum;
import com.anliantest.common.core.utils.SpringUtils;
import com.anliantest.file.api.RemoteSysAttachmentService;
import com.anliantest.file.api.domain.ProgressManage;
import com.anliantest.file.api.domain.SysAttachment;
import com.anliantest.job.api.RemoteJobService;
import com.anliantest.job.api.domain.SysJobApi;
import com.anliantest.rocketmq.service.IConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 消息监听
 */
@Slf4j
@Service
public class RocketMsgListener implements MessageListenerConcurrently {


    private static IConsumerService consumerService;
    private static RemoteJobService remoteJobService;
    private static RemoteSysAttachmentService remoteSysAttachmentService;

    static {
        consumerService = SpringUtils.getBean(IConsumerService.class);
        remoteJobService = SpringUtils.getBean(RemoteJobService.class);
        remoteSysAttachmentService = SpringUtils.getBean(RemoteSysAttachmentService.class);
    }


    /**
     * 消费消息
     *
     * @param list    msgs.size() >= 1
     *                DefaultMQPushConsumer.consumeMessageBatchMaxSize=1，you can modify here
     *                这里只设置为1，当设置为多个时，list中只要有一条消息消费失败，就会整体重试
     * @param consumeConcurrentlyContext 上下文信息
     * @return 消费状态  成功（CONSUME_SUCCESS）或者 重试 (RECONSUME_LATER)
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
       log.info(list.toString());
        if (!CollectionUtils.isEmpty(list)) {

            for (MessageExt messageExt : list) {
                String body = new String(messageExt.getBody());
                String tags = messageExt.getTags();
                String topic = messageExt.getTopic();
                String msgId = messageExt.getMsgId();
                String keys = messageExt.getKeys();
                int reConsume = messageExt.getReconsumeTimes();
                if (reConsume == Constants.MESSAGE_RECONSUME_TIMESC) {
                    log.error("消息重试超过3次，消费失败！");
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                if (MessageCodeEnum.THIRD_MESSAGE.getCode().equals(topic)){
                    if (MessageCodeEnum.THIRD_ADDRESS_MESSAGE_TAG.getCode().equals(tags)){
                        consumerService.saveChineseAddress(body);
                    }else {
                        log.info("未匹配到Tag【{}】" + tags);
                    }
                }

                //导出api
                if (MessageCodeEnum.PROGRESS_MANAGE.getCode().equals(topic)){
                    if (MessageCodeEnum.SYS_JOB_EXPORT.getCode().equals(tags)){


                        //该处转换类型失败会导致无法回填进度状态
                        ProgressManage progressManage = JSON.parseObject(body, ProgressManage.class);
                        //进度id
                        Long id = progressManage.getId();

                        SysJobApi sysJobApi =new SysJobApi();
                        try {
                            String exportJson = progressManage.getExportJson();

                            if(exportJson!=null){
                                sysJobApi = JSON.parseObject(body, SysJobApi.class);
                            }
                            sysJobApi.setCreateBy(progressManage.getCreateBy());


                            Long export = remoteJobService.export(sysJobApi, SecurityConstants.INNER);
                            // export：文件id
                            if(export!=null){
                                //导出成功  修改文件状态为永久
                                SysAttachment sysAttachment = new SysAttachment();
                                sysAttachment.setPId(id);
                                sysAttachment.setTempId(0);
                                sysAttachment.setId(export);
                                Boolean aBoolean = remoteSysAttachmentService.updateById(sysAttachment, SecurityConstants.INNER);
                                //！！   此处最后再判断一次文件转为永久是否成功
                                if(!aBoolean){
                                    //导出成功 回填进度状态为成功
                                    progressManage.setStatus(2);
                                    progressManage.setResultDescription("导出失败");
                                    progressManage.setFailureReason("转换失败");
                                }else {
                                    //导出成功 回填进度状态为成功
                                    progressManage.setStatus(1);
                                    progressManage.setResultDescription("导出成功");
                                }



                            }else {
                                //导出失败 回填进度状态为失败
                                progressManage.setStatus(2);
                                progressManage.setResultDescription("导出失败");
                                progressManage.setFailureReason("生成失败");
                            }
                        }catch (Exception e){
                            //回填进度状态为失败
                            progressManage.setStatus(2);
                            progressManage.setResultDescription("导出失败");
                            progressManage.setFailureReason("调用失败");

                        }

                        remoteSysAttachmentService.updateProgressManageById(progressManage, SecurityConstants.INNER);

                    }else {
                        log.info("未匹配到Tag【{}】" + tags);
                    }
                }

            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
