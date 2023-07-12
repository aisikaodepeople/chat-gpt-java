package com.chatgpt.demo.controller;

import com.chatgpt.demo.dto.PromptDTO;
import com.chatgpt.demo.sse.SseServer;
import com.chatgpt.demo.vo.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

/**
 * AI对话
 */
@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController {

    private final SseServer sseServer;

    /**
     * 创建会话
     *
     * @return
     */
    @GetMapping("/add")
    public R<String> add() {
        return R.ok(UUID.randomUUID().toString());
    }

    /**
     * 发送指令
     *
     * @param promptDTO
     * @return
     */
    @GetMapping("/prompt")
    public SseEmitter prompt(PromptDTO promptDTO) {
        return sseServer.prompt(promptDTO);
    }


}
