
package com.ipda3.sofraapp.data.model.restaurantNewOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantNewOrders {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private RestaurantNewOrdersPagination data;

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

    public RestaurantNewOrdersPagination getData() {
        return data;
    }

    public void setData(RestaurantNewOrdersPagination data) {
        this.data = data;
    }

}
