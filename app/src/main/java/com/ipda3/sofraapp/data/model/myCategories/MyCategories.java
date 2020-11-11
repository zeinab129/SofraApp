
package com.ipda3.sofraapp.data.model.myCategories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyCategories {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private MyCategoriesPagination data;

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

    public MyCategoriesPagination getData() {
        return data;
    }

    public void setData(MyCategoriesPagination data) {
        this.data = data;
    }

}
