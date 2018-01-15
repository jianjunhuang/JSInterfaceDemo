package com.library.jianjunhuang.jsbridge.bean;

import org.json.JSONObject;

/**
 * Created by jianjunhuang on 18-1-13.
 */

public class Data {
    private String api;
    private int callback;
    private String message;
    private Object data;
    private int status;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public int getCallback() {
        return callback;
    }

    public void setCallback(int callback) {
        this.callback = callback;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
