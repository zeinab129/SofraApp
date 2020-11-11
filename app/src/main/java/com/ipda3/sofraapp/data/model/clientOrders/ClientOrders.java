
package com.ipda3.sofraapp.data.model.clientOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientOrders {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ClientOrdersPagination data;

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

    public ClientOrdersPagination getData() {
        return data;
    }

    public void setData(ClientOrdersPagination data) {
        this.data = data;
    }

}
