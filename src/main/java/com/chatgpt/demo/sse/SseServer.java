package com.chatgpt.demo.sse;

import com.chatgpt.demo.dto.PromptDTO;
import com.chatgpt.demo.event.Publisher;
import com.chatgpt.demo.service.PromptService;
import com.chatgpt.demo.vo.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;

@Slf4j
@Component
public class SseServer {

    @Resource
    private Publisher publisher;

    @Resource
    private PromptService promptService;

    @SneakyThrows
    public SseEmitter createSseEmitter(final String cid) {
        SseEmitter sseEmitter = new SseEmitter(0L);

        sseEmitter.onCompletion(() -> log.info("sse连接使用完成:cid={}", cid));
        sseEmitter.onTimeout(() -> log.error("sse消息发送超时:cid={},当前连接数:onlineCount={},timeout={}", cid, sseEmitter.getTimeout()));
        sseEmitter.onError(throwable -> {
            try {
                log.error("sse连接异常:cid={},error={}", cid, throwable.toString());
            } catch (Exception e) {
                log.error("sse连接异常:", e);
            }
        });

        log.info("sse连接建立完成:cid={}", cid);

        return sseEmitter;
    }

    @SneakyThrows
    public SseEmitter prompt(PromptDTO promptDTO) {
        SseEmitter sseEmitter = createSseEmitter(promptDTO.getCid());
        try {
            promptService.prompt(promptDTO, new SseEventSourceListener(promptDTO.getCid(), sseEmitter, publisher));
        } catch (Exception e) {
            log.error("sse发送指令异常:", e);
            sseEmitter.send(SseEmitter.event().id("ERROR").data(R.fail(e.getMessage())).reconnectTime(1000));
        }
        return sseEmitter;
    }


}
