package com.chatgpt.demo.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Component
public class Publisher {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishDoneEvent(String cid, String answers, Date doneDate) {
        log.info("发布DoneEvent事件:cid={},answers={},doneDate={}", cid, answers, doneDate);
        applicationEventPublisher.publishEvent(new DoneEvent(this, cid, answers, doneDate));
    }

    public void publishClearInvalidCnnEvent() {
        applicationEventPublisher.publishEvent(new ClearInvalidCnnEvent(this));
    }

}