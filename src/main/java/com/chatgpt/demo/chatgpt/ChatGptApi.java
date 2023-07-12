package com.chatgpt.demo.chatgpt;

import com.chatgpt.demo.contants.ChatGtpModelEnum;
import com.chatgpt.demo.util.JsonUtils;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ChatGptApi {

    private static final okhttp3.MediaType APPLICATION_JSON = okhttp3.MediaType.parse(MediaType.APPLICATION_JSON_UTF8_VALUE);
    private static final CacheControl OKHTTP3_CACHE_CONTROL = new CacheControl.Builder().maxAge(10, TimeUnit.SECONDS).build();

    @Resource
    private OkHttpClient okHttpClient;

    /**
     * 流式问答
     *
     * @param messages            指令上下文
     * @param eventSourceListener 事件监听器
     */
    public void streamChatCompletions(List<ChatMessage> messages, EventSourceListener eventSourceListener) {
        log.info("发送指令: {}", messages);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                // todo chatGPT模型,可根据实际场景调整
                .model(ChatGtpModelEnum.gpt35turbo16k.getModel())
                .messages(messages)
                .stream(true)
                .build();

        EventSource.Factory factory = EventSources.createFactory(okHttpClient);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(RequestBody.create(JsonUtils.toJson(chatCompletionRequest), APPLICATION_JSON))
                .cacheControl(OKHTTP3_CACHE_CONTROL)
                .build();

        factory.newEventSource(request, eventSourceListener);
    }

}