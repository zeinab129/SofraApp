package com.ipda3.sofraapp.data.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cart {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo
    String path;
    @ColumnInfo
    String name;

    public Cart(int id, String path, String name, String desc, String price, String specialOrder, String quantity) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.specialOrder = specialOrder;
        this.quantity = quantity;
    }

    @ColumnInfo
    String desc;
    @ColumnInfo
    String price;
    @ColumnInfo
    String specialOrder;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpecialOrder() {
        return specialOrder;
    }

    public void setSpecialOrder(String specialOrder) {
        this.specialOrder = specialOrder;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @ColumnInfo
    String quantity;

}
