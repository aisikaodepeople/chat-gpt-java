package com.chatgpt.demo.chatgpt;

import com.chatgpt.demo.event.Publisher;
import com.chatgpt.demo.util.JsonUtils;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

;

@Slf4j
public abstract class GtpEventSourceListener extends EventSourceListener {

    protected String cid;
    protected StringBuilder answers;
    protected Publisher publisher;

    public GtpEventSourceListener(String cid, StringBuilder answers, Publisher publisher) {
        this.cid = cid;
        this.answers = answers;
        this.publisher = publisher;
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        log.info("ChatGPT事件监听器建立:cid={}", cid);
    }

    @Override
    public void onClosed(EventSource eventSource) {
        log.info("ChatGPT事件监听器关闭:cid={}", cid);
        eventSource.cancel();
        publisher.publishClearInvalidCnnEvent();
    }

    @SneakyThrows
    public void onDone() {
        // todo 数据接收完毕的回调函数,可扩展自己的业务
        log.info("ChatGPT数据接收完毕:cid={},answers={}", cid, answers);
        publisher.publishDoneEvent(cid, answers.toString(), new Date());
    }

    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        log.error("ChatGPT事件监听异常:cid={},eventSource={},error={},response={}", cid, eventSource, t, response);
        try {
            if (response != null && !response.isSuccessful()) {
                log.error("ChatGPT返回错误信息:{}", response.body().string());
                onFailure(response.message());
            } else {
                onFailure(t.getMessage());
            }
        } finally {
            eventSource.cancel();
            publisher.publishClearInvalidCnnEvent();
        }
    }

    public abstract void onFailure(String errMsg);

    public abstract void onEvent(String answer);

    @SneakyThrows
    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        log.info("ChatGPT回答:id={},type={},cid={},data={}", id, type, cid, data);

        if (StringUtils.equals(data, "[DONE]")) {
            onEvent("DONE");
            // todo 数据接收完毕,执行回调函数
            onDone();
            return;
        }

        ChatCompletionChunk chatCompletionChunk = JsonUtils.toJavaObject(data, ChatCompletionChunk.class);
        List<ChatCompletionChoice> choices = chatCompletionChunk.getChoices();
        if (CollectionUtils.isEmpty(choices)) {
            return;
        }
        String answer = choices.get(0).getMessage().getContent();
        if (answer != null) {
            answers.append(answer);
            // todo 流式接收数据,通过sse或websocket推送到前端,实现键盘敲击效果
            onEvent(answer);
        }
    }

}
