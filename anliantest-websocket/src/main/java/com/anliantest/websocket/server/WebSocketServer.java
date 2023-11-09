package com.anliantest.websocket.server;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.anliantest.common.core.exception.auth.NotLoginException;
import com.anliantest.common.security.auth.AuthUtil;
import com.anliantest.system.api.model.LoginUser;
import com.anliantest.websocket.constant.TopicConstants;
import com.anliantest.websocket.domain.WebsocketPushBody;
import com.anliantest.websocket.domain.WebsocketResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import javax.websocket.*;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description websocket服务
 * @Date 2023/8/11 14:01
 * @Author maoly
 **/
@Slf4j
@ServerEndpoint(value = "/websocket/{token}")
@Component
public class WebSocketServer {

    private static ConcurrentHashMap<Long, Session> sessionMap;

    private static ConcurrentHashMap<Long, LoginUser> userMap;

    static {
        sessionMap = new ConcurrentHashMap<>();
        userMap = new ConcurrentHashMap<>();
    }
    /**
     * 连接建立成功调用的方法
     * @param session
     * @param token
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        LoginUser loginUser = AuthUtil.getLoginUser(token);
        if (loginUser == null) {
            log.error("webSocket连接失败，失败原因：token失效或无法解析");
            throw new NotLoginException("无效的token");
        }
        Long userId = loginUser.getUserid();
        sessionMap.put(userId, session);
        userMap.put(userId, loginUser);
        int size = sessionMap.size();
        log.warn("webSocket连接成功，userId：{},userName：{},当前在线人数：{}",loginUser.getUserid(),loginUser.getUsername(),size);

    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        removeMap(session);
    }
    private void removeMap(Session session) {
        Long userId = getUserIdBySession(session);
        if (ObjectUtil.isNull(userId)) {
            return;
        }
        sessionMap.remove(userId);
        userMap.remove(userId);
    }
    private Long getUserIdBySession(Session session) {
        for (Long userId : sessionMap.keySet()) {
            if (sessionMap.get(userId).getId().equals(session.getId())) {
                return userId;
            }
        }
        return null;
    }


    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        List<String> tokenValues = session.getRequestParameterMap().get("token");
        if(CollectionUtils.isNotEmpty(tokenValues)){
            LoginUser loginUser = AuthUtil.getLoginUser(tokenValues.get(0));
            if (loginUser == null) {
                log.error("webSocket连接失败，失败原因：token失效或无法解析");
                throw new NotLoginException("无效的token");
            }
        }
        if (StringUtils.isBlank(message)) {
            return;
        }
        WebsocketPushBody messageRequestBody = JSONObject.parseObject(message, WebsocketPushBody.class);
        if(messageRequestBody != null){
            WebsocketResponseBody messageResponseBody = getMessageResponseBody(messageRequestBody,getUserIdBySession(session));
            if(messageRequestBody.getTopic().equals(TopicConstants.PING_TOPIC)){
                messageResponseBody.setText("pong");
                sendMessage(session,JSONObject.toJSONString(messageResponseBody));
            }
            if(messageRequestBody.getTopic().equals(TopicConstants.SYSTEM_TOPIC)){
                List<Long> toUserIdList = messageRequestBody.getToUserIdList();
                if(CollectionUtils.isNotEmpty(toUserIdList)){
                    toUserIdList.forEach(t -> {
                        sendCustom(JSONObject.toJSONString(messageResponseBody), t);
                    });
                }
            }
            if(messageRequestBody.getTopic().equals(TopicConstants.GROUP_TOPIC)){
                sendGroup(JSONObject.toJSONString(messageResponseBody));
            }
        }
    }

    //群发消息
    private void sendGroup(String message) {
        Collection<Session> sessionCollection = sessionMap.values();
        if(CollectionUtils.isNotEmpty(sessionCollection)){
            sessionCollection.forEach(t -> {
                try {
                    sendMessage(t,message);
                } catch (IOException e) {
                    log.error("群发消息失败,请排查");
                }
            });
        }
    }

    /**
     * 消息返回体封装
     * @param messageRequestBody
     * @param sendUserId
     * @return
     */
    private WebsocketResponseBody getMessageResponseBody(WebsocketPushBody messageRequestBody, Long sendUserId){
        return WebsocketResponseBody.builder()
                .topic(messageRequestBody.getTopic())
                .bussinessType(messageRequestBody.getBussinessType())
                .sendUserId(sendUserId)
                .text(messageRequestBody.getText()).build();
    }

    private LoginUser getUserBySession(Session session) {
        Long userId = getUserIdBySession(session);
        if (ObjectUtil.isNull(userId)) {
            return null;
        }
        return userMap.get(userId);
    }


    /**
     * 发生错误时调用
     *
     * @OnError
     */
    @OnError
    public void onError(Session session, Throwable error) {
        Long userId = getUserIdBySession(session);
        if (ObjectUtil.isNull(userId)) {
            return;
        }
        log.error("用户错误:" + userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 发送自定义消息方法
     * @param message
     * @param toUserId
     */
    public static void sendCustom(String message, Long toUserId) {
        log.info("发送消息到:{},消息内容:{}", toUserId, message);
        if (ObjectUtil.isNull(toUserId) || StringUtils.isBlank(message)) {
            log.error("发送失败：消息体不完整");
            return;
        }
        try {
            sendMessage(sessionMap.get(toUserId), message);
        } catch (Exception e) {
            log.error("发送给{}的消息出错", toUserId);
        }
    }

    /**
     * 发送即时消息API调用
     * @param message
     * @param toUserIds
     */
    public static void sendApi(String message, List<Long> toUserIds) {
        log.info("API调用发送消息到:{},消息内容:{}", toUserIds, message);
        if (ObjectUtil.isNull(toUserIds) || StringUtils.isBlank(message)) {
            return;
        }
        toUserIds.forEach(t -> {
            try {
                sendMessage(sessionMap.get(t), message);
            } catch (IOException e) {
                log.error("发送给{}的消息出错", t);
            }
        });
    }

    public static void sendMessage(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }
}

