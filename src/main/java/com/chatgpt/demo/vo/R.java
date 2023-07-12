package com.chatgpt.demo.vo;

import lombok.Data;

/**
 * 返回结果封装
 *
 * @param <T>
 */
@Data
public class R<T> {
    private boolean isSuccess;
    private int code;
    private String msg;
    private T data;

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        return ok(200, "success", data);
    }

    public static <T> R<T> ok(int code, String msg, T data) {
        R<T> r = new R<T>();
        r.setCode(200);
        r.setMsg(msg);
        r.setData(data);
        r.setSuccess(true);
        return r;
    }

    public static <T> R<T> fail(int code, String msg, T data) {
        R<T> r = new R<T>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        r.setSuccess(false);
        return r;
    }

    public static <T> R<T> fail(int code, String msg) {
        return fail(100, msg, null);
    }

    public static <T> R<T> fail(String msg) {
        return fail(100, msg, null);
    }

}
