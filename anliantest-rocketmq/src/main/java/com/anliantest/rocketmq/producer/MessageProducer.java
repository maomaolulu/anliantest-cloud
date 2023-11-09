package com.anliantest.rocketmq.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.anliantest.rocketmq.config.ProducerConfig.producer;

/**
 * 消息发送
 */
@Slf4j
public class MessageProducer {


    /**
     * 同步发送消息
     * @param topic 主题
     * @param tag 标签
     * @param key 自定义的key，根据业务来定
     * @param value 消息的内容
     * @return org.apache.rocketmq.client.producer.SendResult
     */
    public SendResult sendSynchronizeMessage(String topic, String tag, String key, String value){
        String body = "topic：【"+topic+"】, tag：【"+tag+"】, key：【"+key+"】, value：【"+value+"】";
        try {
            Message msg = new Message(topic,tag,key, value.getBytes(RemotingHelper.DEFAULT_CHARSET));
            log.info("生产者发送消息:"+ JSON.toJSONString(value));
            SendResult result = producer.send(msg);
            return result;
        } catch (UnsupportedEncodingException e) {
            log.error("消息初始化失败！body：{},e:{}",body,e.getMessage());

        } catch (MQClientException e) {
            log.error("消息发送失败! body：{},e:{}",body,e.getMessage());
        }catch (RemotingException e){
            log.error("消息发送失败! body：{},e:{}",body,e.getMessage());
        }catch (InterruptedException e){
            log.error("消息发送失败! body：{},e:{}",body,e.getMessage());
        }catch (MQBrokerException e){
            log.error("消息发送失败! body：{},e:{}",body,e.getMessage());
        }
        return null;
    }



    /**
     * 发送有序的消息
     * @param messagesList Message集合
     * @param messageQueueNumber 消息队列编号
     * @return org.apache.rocketmq.client.producer.SendResult
     */
    public SendResult sendOrderlyMessage(List<Message> messagesList, int messageQueueNumber) {
        SendResult result = null;
        for (Message message : messagesList) {
            try {
                result = producer.send(message, (list, msg, arg) -> {
                    Integer queueNumber = (Integer) arg;
                    return list.get(queueNumber);
                }, messageQueueNumber);
            } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
                log.error("发送有序消息失败");
                return result;
            }
        }
        return result;
    }

    /**
     * 推送延迟消息
     * @param topic
     * @param tag
     * @param key
     * @return boolean
     */
    public SendResult sendDelayMessage(String topic, String tag, String key, String value)
    {
        SendResult result = null;
        try
        {
            Message msg = new Message(topic,tag,key, value.getBytes(RemotingHelper.DEFAULT_CHARSET));
            //设置消息延迟级别，我这里设置5，对应就是延时一分钟
            // "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
            msg.setDelayTimeLevel(4);
            // 发送消息到一个Broker
            result = producer.send(msg);
            // 通过sendResult返回消息是否成功送达
            log.info("发送延迟消息结果：======sendResult：{}", result);
            DateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            log.info("发送时间：{}", format.format(new Date()));
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            log.error("延迟消息队列推送消息异常:{},推送内容:{}", e.getMessage(), result);
        }
        return result;
    }


}
