package com.chatgpt.demo.service;

import com.chatgpt.demo.chatgpt.ChatGptApi;
import com.chatgpt.demo.dto.PromptDTO;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class PromptService {

    @Resource
    private ChatGptApi chatGptApi;

    /*缓存每次会话的指令(可自行调整存入数据库)*/
    public static final Map<String, List<ChatMessage>> msgCtx = new ConcurrentHashMap();
    /*每次发送的上下文指令语句字数限制8000字,可根据实际业务场景自行调整*/
    public static final int GTP_MAX_TOKENS = 8000;

    /**
     * 指令上下文语境
     *
     * @param cid
     * @return
     */
    public List<ChatMessage> getPromptContext(String cid) {
        List<ChatMessage> messages = new ArrayList<>();

        List<ChatMessage> messageList = msgCtx.get(cid);

        if (messageList != null) {
            int index = 0;
            int maxTokens = 0;
            for (int i = messageList.size() - 1; i >= 0; i--) {
                maxTokens += StringUtils.length(messageList.get(i).getContent());
                if (maxTokens >= GTP_MAX_TOKENS) {
                    index = i;
                    break;
                }
            }

            for (int j = index; j < messageList.size(); j++) {
                messages.add(new ChatMessage(ChatMessageRole.USER.value(), messageList.get(j).getContent()));
            }
        }

        return messages;
    }


    /**
     * 下发指令
     *
     * @param promptDTO
     * @param eventSourceListener
     * @throws Exception
     */
    public void prompt(PromptDTO promptDTO, EventSourceListener eventSourceListener) throws Exception {
        String cid = promptDTO.getCid();
        String prompt = promptDTO.getPrompt();

        List<ChatMessage> messages = getPromptContext(cid);
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), prompt));

        // todo 放入缓存
        msgCtx.put(cid, messages);

        chatGptApi.streamChatCompletions(messages, eventSourceListener);
    }


}
