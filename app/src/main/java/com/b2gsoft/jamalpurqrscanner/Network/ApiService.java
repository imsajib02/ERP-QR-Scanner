package com.b2gsoft.jamalpurqrscanner.Network;

import com.b2gsoft.jamalpurqrscanner.Model.LoginResponse;
import com.b2gsoft.jamalpurqrscanner.Model.Password;
import com.b2gsoft.jamalpurqrscanner.Model.User;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("salesmen/login")
    Call<JsonObject> login(@Body User user);

    @Headers("Accept: application/json")
    @POST("salesmen/coupon-validation-check")
    Call<JsonObject> validateCode(@Header("Authorization") String token, @Query("qrcode") String code);

    @Headers("Accept: application/json")
    @POST("salesmen/change-password")
    Call<JsonObject> changePassword(@Header("Authorization") String token, @Body Password password);

    @Headers("Accept: application/json")
    @GET("salesmen/daily-route-list/{salesman_id}")
    Call<JsonObject> getRoutes(@Header("Authorization") String token, @Path(value = "salesman_id", encoded = true) String salesmanID);

    @Headers("Accept: application/json")
    @GET("salesmen/customer-list/{route_id}")
    Call<JsonObject> getCustomers(@Header("Authorization") String token, @Path(value = "route_id", encoded = true) String routeID);

    @Headers("Accept: application/json")
    @POST("salesmen/store/coupon")
    Call<JsonObject> submitCoupon(@Header("Authorization") String token, @Query("coupon_id") String couponID, @Query("coupon_code") String couponCode, @Query("customer_id") String customerID);
}