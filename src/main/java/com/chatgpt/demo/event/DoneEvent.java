package com.chatgpt.demo.event;

import org.springframework.context.ApplicationEvent;

import java.util.Date;

public class DoneEvent extends ApplicationEvent {

    private String cid;
    private String answers;
    private Date doneDate;

    public DoneEvent(Object source, String cid, String answers, Date doneDate) {
        super(source);
        this.cid = cid;
        this.answers = answers;
        this.doneDate = doneDate;
    }

    public String getCid() {
        return cid;
    }

    public String getAnswers() {
        return answers;
    }

    public Date getDoneDate() {
        return doneDate;
    }

    @Override
    public String toString() {
        return "DoneEvent{ cid=" + cid + ", answers=" + answers + ", doneDate=" + doneDate + '}';
    }
}