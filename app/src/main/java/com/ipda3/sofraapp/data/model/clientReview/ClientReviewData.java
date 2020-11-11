package com.ipda3.sofraapp.data.model.clientReview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientReviewData {
    @SerializedName("review")
    @Expose
    private Review review;

    public void setReview(Review review){
        this.review = review;
    }
    public Review getReview(){
        return this.review;
    }
}
