package com.chatgpt.demo.dto;

import lombok.Data;

@Data
public class PromptDTO {

    /**
     * 会话ID
     */
    private String cid;

    /**
     * 指令
     */
    private String prompt;


}
