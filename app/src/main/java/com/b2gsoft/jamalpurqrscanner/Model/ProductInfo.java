package com.b2gsoft.jamalpurqrscanner.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ProductInfo implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("product_name")
    private String name;

    @SerializedName("product_image")
    private String image;

    @SerializedName("batch_no")
    private String batchNo;

    @SerializedName("coupon_id")
    private String couponID;

    @SerializedName("coupon_code")
    private String couponCode;

    @SerializedName("coupon_amount")
    private String couponAmount;

    @SerializedName("coupon_exp_date")
    private String expiryDate;

    public ProductInfo(String name, String image, String batchNo, String couponID, String couponCode, String couponAmount, String expiryDate) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.couponID = couponID;
        this.batchNo = batchNo;
        this.couponCode = couponCode;
        this.couponAmount = couponAmount;
        this.expiryDate = expiryDate;
    }

    public ProductInfo() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getCouponID() {
        return couponID;
    }

    public void setCouponID(String couponID) {
        this.couponID = couponID;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
