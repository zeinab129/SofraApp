package com.ipda3.sofraapp.data.model.clientReview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ipda3.sofraapp.data.model.restaurantDetails.RestaurantDetailsData;
import com.ipda3.sofraapp.data.model.user.User;
import com.ipda3.sofraapp.data.model.user.UserFullData;

public class Review {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("restaurant_id")
    @Expose
    private String restaurantId;
    @SerializedName("client_id")
    @Expose
    private int clientId;
    @SerializedName("client")
    @Expose
    private UserFullData client;
    @SerializedName("restaurant")
    @Expose
    private RestaurantDetailsData restaurant;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public UserFullData getClient() {
        return client;
    }

    public void setClient(UserFullData client) {
        this.client = client;
    }

    public RestaurantDetailsData getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantDetailsData restaurant) {
        this.restaurant = restaurant;
    }
}
