package com.b2gsoft.jamalpurqrscanner.Model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("username")
    private String username;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("asm_id")
    private int asmID;

    @SerializedName("district_id")
    private int districtID;

    @SerializedName("upazila_id")
    private int upazillaID;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("status")
    private String status;

    @SerializedName("deletable")
    private String deletable;

    @SerializedName("address")
    private String address;

    public User(int id, String name, String username, String phone, String email, String password, String avatar, String status, String deletable, String address) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.status = status;
        this.deletable = deletable;
        this.address = address;
    }

    public User(int id, String name, String username, String phone, String email, String password, int asmID, int districtID, int upazillaID, String avatar,
                String status, String deletable, String address) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.asmID = asmID;
        this.districtID = districtID;
        this.upazillaID = upazillaID;
        this.avatar = avatar;
        this.status = status;
        this.deletable = deletable;
        this.address = address;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAsmID() {
        return asmID;
    }

    public void setAsmID(int asmID) {
        this.asmID = asmID;
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    public int getUpazillaID() {
        return upazillaID;
    }

    public void setUpazillaID(int upazillaID) {
        this.upazillaID = upazillaID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeletable() {
        return deletable;
    }

    public void setDeletable(String deletable) {
        this.deletable = deletable;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
