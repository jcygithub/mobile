package com.example.administrator.mvpdemo.service.entity;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
public class books {

    /**
     * msg : missing_args
     * request : GET /v2/book/search
     * code : 1002
     */
    private String msg;
    private String request;
    private int code;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public String getRequest() {
        return request;
    }

    public int getCode() {
        return code;
    }

}
