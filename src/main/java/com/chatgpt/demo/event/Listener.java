package com.chatgpt.demo.event;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Component
public class Listener {

    @Resource
    private OkHttpClient okHttpClient;

    @Async("threadPool")
    @EventListener
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void handleDoneEvent(DoneEvent event) {
        log.info("监听到DoneEvent事件:{}", event);

        String cid = event.getCid();
        String answers = event.getAnswers();
        // todo 业务处理
    }

    @Async("threadPool")
    @EventListener
    public void handleClearOCPEvent(ClearInvalidCnnEvent event) {
        okHttpClient.connectionPool().evictAll();
    }

}