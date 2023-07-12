package com.chatgpt.demo.event;

import org.springframework.context.ApplicationEvent;

/**
 * clear invalid expired okhttp client connection
 */
public class ClearInvalidCnnEvent extends ApplicationEvent {

    public ClearInvalidCnnEvent(Object source) {
        super(source);
    }

}
