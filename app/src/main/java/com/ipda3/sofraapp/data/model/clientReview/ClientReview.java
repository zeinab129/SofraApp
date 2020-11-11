package com.ipda3.sofraapp.data.model.clientReview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientReview {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ClientReviewData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ClientReviewData getData() {
        return data;
    }

    public void setData(ClientReviewData data) {
        this.data = data;
    }
}
