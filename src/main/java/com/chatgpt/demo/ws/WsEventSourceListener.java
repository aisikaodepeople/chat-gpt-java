package com.chatgpt.demo.ws;

import com.chatgpt.demo.chatgpt.GtpEventSourceListener;
import com.chatgpt.demo.event.Publisher;
import com.chatgpt.demo.util.JsonUtils;
import com.chatgpt.demo.vo.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;

@Slf4j
public class WsEventSourceListener extends GtpEventSourceListener {

    private Session session;

    public WsEventSourceListener(String cid, Session session, Publisher publisher) {
        super(cid, new StringBuilder(), publisher);
        this.session = session;
    }

    @SneakyThrows
    @Override
    public void onFailure(String errMsg) {
        session.getBasicRemote().sendText(JsonUtils.toJson(R.fail(errMsg)));
    }

    @SneakyThrows
    @Override
    public void onEvent(String answer) {
        session.getBasicRemote().sendText(JsonUtils.toJson(R.ok(answer)));
    }

}