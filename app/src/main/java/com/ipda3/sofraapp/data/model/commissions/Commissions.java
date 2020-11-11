
package com.ipda3.sofraapp.data.model.commissions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Commissions {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private CommissionsData data;

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

    public CommissionsData getData() {
        return data;
    }

    public void setData(CommissionsData data) {
        this.data = data;
    }

}
