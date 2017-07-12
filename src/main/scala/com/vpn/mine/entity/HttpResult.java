package com.vpn.mine.entity;

/**
 * Created by coder on 17-7-3.
 */
public class HttpResult {

    //错误代码
    private String err;

    //数据
    private String data;

    //状态
    private String status;

    //反馈提示信息
    private String info;

    public HttpResult() {
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
