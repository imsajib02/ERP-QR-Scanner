package com.b2gsoft.jamalpurqrscanner.Model;

import com.google.gson.annotations.SerializedName;

public class Customer {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Customer(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return name;
    }
}
