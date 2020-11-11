
package com.ipda3.sofraapp.data.model.commissions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommissionsData {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("commissions")
    @Expose
    private String commissions;
    @SerializedName("payments")
    @Expose
    private Integer payments;
    @SerializedName("net_commissions")
    @Expose
    private Double netCommissions;
    @SerializedName("commission")
    @Expose
    private String commission;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCommissions() {
        return commissions;
    }

    public void setCommissions(String commissions) {
        this.commissions = commissions;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public Double getNetCommissions() {
        return netCommissions;
    }

    public void setNetCommissions(Double netCommissions) {
        this.netCommissions = netCommissions;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

}
