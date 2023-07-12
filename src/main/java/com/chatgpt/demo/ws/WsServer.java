package com.chatgpt.demo.ws;

import com.chatgpt.demo.dto.PromptDTO;
import com.chatgpt.demo.event.Publisher;
import com.chatgpt.demo.service.PromptService;
import com.chatgpt.demo.util.JsonUtils;
import com.chatgpt.demo.vo.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
@ServerEndpoint("/gtpws/{cid}")
public class WsServer {

    private static int onlineCount = 0;

    private static ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    private static Publisher publisher;

    private static PromptService promptService;

    @Autowired
    public void setPromptService(PromptService promptService) {
        WsServer.promptService = promptService;
    }

    @Autowired
    public void setPublishEvent(Publisher publisher) {
        WsServer.publisher = publisher;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("cid") String cid) {
        if (sessions.containsKey(cid)) {
            sessions.remove(cid);
            sessions.put(cid, session);
        } else {
            sessions.put(cid, session);
            addOnlineCount();
        }
        log.info("ws建立连接:cid={},当前连接数:onlineCount={}", cid, getOnlineCount());
    }

    @OnClose
    public void onClose(Session session, @PathParam("cid") String cid) {
        if (sessions.containsKey(cid)) {
            sessions.remove(cid);
            subOnlineCount();
        }
        log.info("ws连接断开:cid={},当前连接数:onlineCount={}", cid, getOnlineCount());
    }

    @OnError
    public void onError(Session session, Throwable throwable, @PathParam("cid") String cid) {
        log.info("ws连接错误:cid={},错误原因:", cid, throwable);
        throwable.printStackTrace();
    }

    public static synchronized int getOnlineCount() {
        return WsServer.onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WsServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WsServer.onlineCount--;
    }


    @OnMessage
    @SneakyThrows
    public void onMessage(Session session, String msg, @PathParam("cid") String cid) {
        log.info("ws收到消息:cid={},msg={}", cid, msg);
        try {
            PromptDTO promptDTO = JsonUtils.toJavaObject(msg, PromptDTO.class);
            Assert.notNull(promptDTO, "无效的参数");

            promptService.prompt(promptDTO, new WsEventSourceListener(cid, session, publisher));
        } catch (Throwable e) {
            log.error("ws处理消息异常:cid={},错误原因:", cid, e);
            session.getBasicRemote().sendText(JsonUtils.toJson(R.fail(e.getMessage())));
        }

    }

}