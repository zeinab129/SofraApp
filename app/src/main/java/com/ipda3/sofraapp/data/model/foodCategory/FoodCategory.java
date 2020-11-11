package com.ipda3.sofraapp.data.model.foodCategory;

import java.util.List;

public class FoodCategory {
    private int status;

    private String msg;

    private List<FoodCategoryData> data;

    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setData(List<FoodCategoryData> data){
        this.data = data;
    }
    public List<FoodCategoryData> getData(){
        return this.data;
    }
}
