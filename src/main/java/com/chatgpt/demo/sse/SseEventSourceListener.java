package com.chatgpt.demo.sse;

import com.chatgpt.demo.chatgpt.GtpEventSourceListener;
import com.chatgpt.demo.event.Publisher;
import com.chatgpt.demo.vo.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Slf4j
public class SseEventSourceListener extends GtpEventSourceListener {

    private SseEmitter sseEmitter;

    public SseEventSourceListener(String cid, SseEmitter sseEmitter, Publisher publisher) {
        super(cid, new StringBuilder(), publisher);
        this.sseEmitter = sseEmitter;
    }

    @SneakyThrows
    @Override
    public void onFailure(String errMsg) {
        sseEmitter.send(R.fail(errMsg));
    }

    @SneakyThrows
    @Override
    public void onEvent(String answer) {
        sseEmitter.send(R.ok(answer));
    }

}
