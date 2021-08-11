package com.b2gsoft.jamalpurqrscanner.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.b2gsoft.jamalpurqrscanner.Interface.Connectivity;
import com.b2gsoft.jamalpurqrscanner.Model.Customer;
import com.b2gsoft.jamalpurqrscanner.Model.Data;
import com.b2gsoft.jamalpurqrscanner.Model.Password;
import com.b2gsoft.jamalpurqrscanner.Model.ProductInfo;
import com.b2gsoft.jamalpurqrscanner.Model.Route;
import com.b2gsoft.jamalpurqrscanner.Network.ApiService;
import com.b2gsoft.jamalpurqrscanner.Network.RetrofitInstance;
import com.b2gsoft.jamalpurqrscanner.R;
import com.b2gsoft.jamalpurqrscanner.Utils.SharedPreference;
import com.b2gsoft.jamalpurqrscanner.Utils.StaticValue;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CouponSubmitRepository {

    private Context context;
    private Connectivity connectivity;

    private SharedPreference sharedPreference;
    private static CouponSubmitRepository instance;

    private MutableLiveData<List<Route>> routeData = new MutableLiveData<>();
    private MutableLiveData<List<Customer>> customerData = new MutableLiveData<>();

    private MutableLiveData<String> message = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNetworkCallActive = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSubmitSuccess = new MutableLiveData<>();

    private RetrofitInstance retrofitInstance = new RetrofitInstance();
    private ApiService service = retrofitInstance.getRetrofitInstance(retrofitInstance.BASE_URL).create(ApiService.class);


    public void init(Context context, Connectivity connectivity) {
        this.context = context;
        this.connectivity = connectivity;
        sharedPreference = new SharedPreference(context);
    }


    public static CouponSubmitRepository getInstance() {

        if(instance == null) {
            instance = new CouponSubmitRepository();
        }

        return instance;
    }


    public void getRoutes() {

        if(StaticValue.isConnected) {

            if(StaticValue.isConnectionActive) {

                isNetworkCallActive.setValue(true);

                Data data = sharedPreference.getCurrentUser();

                Log.e("Token ", data.getToken());

                Call<JsonObject> call = service.getRoutes("Bearer " +data.getToken(), String.valueOf(data.getUser().getId()));

                call.enqueue(new Callback<JsonObject>() {

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if(response.isSuccessful())
                        {
                            Log.e("Routes List ", new Gson().toJson(response.body()));

                            Boolean isSuccessful = response.body().get("status").getAsBoolean();

                            if(isSuccessful) {

                                Gson gson = new Gson();
                                List<Route> routeList = new ArrayList<>();

                                JsonArray array = response.body().get("data").getAsJsonArray();

                                for(int i=0; i<array.size(); i++) {

                                    Route route = gson.fromJson(array.get(i).getAsJsonObject().toString(), Route.class);
                                    routeList.add(route);
                                }

                                routeData.postValue(routeList);
                            }
                            else {

                                message.postValue(context.getString(R.string.failed_to_get_routes));
                            }
                        }
                        else
                        {
                            message.postValue(context.getString(R.string.failed_to_get_routes));
                            Log.e("RetrofitResponseError ", ""+response.errorBody());
                        }

                        isNetworkCallActive.postValue(false);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                        message.postValue(context.getString(R.string.unknown_error));
                        isNetworkCallActive.postValue(false);
                        Log.e("RetrofitFailure ", ""+t.getMessage());
                    }
                });
            }
            else {

                connectivity.noActiveConnection();
            }
        }
        else {

            connectivity.notConnected();
        }
    }


    public void getCustomers(int routeID) {

        if(StaticValue.isConnected) {

            if(StaticValue.isConnectionActive) {

                isNetworkCallActive.setValue(true);

                Data data = sharedPreference.getCurrentUser();

                Log.e("Token ", data.getToken());

                Call<JsonObject> call = service.getCustomers("Bearer " +data.getToken(), String.valueOf(routeID));

                call.enqueue(new Callback<JsonObject>() {

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if(response.isSuccessful())
                        {
                            Log.e("Customer List ", new Gson().toJson(response.body()));

                            Boolean isSuccessful = response.body().get("status").getAsBoolean();

                            if(isSuccessful) {

                                Gson gson = new Gson();
                                List<Customer> customerList = new ArrayList<>();

                                JsonArray array = response.body().get("data").getAsJsonArray();

                                for(int i=0; i<array.size(); i++) {

                                    Customer customer = gson.fromJson(array.get(i).toString(), Customer.class);
                                    customerList.add(customer);
                                }

                                customerData.postValue(customerList);
                            }
                            else {

                                message.postValue(context.getString(R.string.failed_to_get_customers));
                            }
                        }
                        else
                        {
                            message.postValue(context.getString(R.string.failed_to_get_customers));
                            Log.e("RetrofitResponseError ", ""+response.errorBody());
                        }

                        isNetworkCallActive.postValue(false);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                        message.postValue(context.getString(R.string.unknown_error));
                        isNetworkCallActive.postValue(false);
                        Log.e("RetrofitFailure ", ""+t.getMessage());
                    }
                });
            }
            else {

                connectivity.noActiveConnection();
            }
        }
        else {

            connectivity.notConnected();
        }
    }


    public void submitCoupon(Customer customer, ProductInfo productInfo) {

        if(StaticValue.isConnected) {

            if(StaticValue.isConnectionActive) {

                isNetworkCallActive.setValue(true);
                isSubmitSuccess.setValue(false);

                Data data = sharedPreference.getCurrentUser();

                Log.e("Token ", data.getToken());

                Call<JsonObject> call = service.submitCoupon("Bearer " +data.getToken(), productInfo.getCouponID(), productInfo.getCouponCode(), String.valueOf(customer.getId()));

                call.enqueue(new Callback<JsonObject>() {

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if(response.isSuccessful())
                        {
                            Log.e("Submit Coupon ", new Gson().toJson(response.body()));

                            Boolean isSuccessful = response.body().get("status").getAsBoolean();

                            if(isSuccessful) {

                                isSubmitSuccess.postValue(true);
                            }
                            else {

                                isSubmitSuccess.postValue(false);
                                message.postValue(context.getString(R.string.coupon_submit_failed));
                            }
                        }
                        else
                        {
                            isSubmitSuccess.postValue(false);
                            message.postValue(context.getString(R.string.coupon_submit_failed));
                            Log.e("RetrofitResponseError ", ""+response.errorBody());
                        }

                        isNetworkCallActive.postValue(false);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                        message.postValue(context.getString(R.string.unknown_error));
                        isNetworkCallActive.postValue(false);
                        isSubmitSuccess.postValue(false);
                        Log.e("RetrofitFailure ", ""+t.getMessage());
                    }
                });
            }
            else {

                connectivity.noActiveConnection();
            }
        }
        else {

            connectivity.notConnected();
        }
    }


    public void resetValues() {

        routeData = new MutableLiveData<>();
        customerData = new MutableLiveData<>();
        message = new MutableLiveData<>();
    }


    public LiveData<Boolean> getNetworkCallStatus() {
        return isNetworkCallActive;
    }

    public LiveData<Boolean> getSubmitStatus() {
        return isSubmitSuccess;
    }

    public LiveData<List<Route>> getRouteData() {
        return routeData;
    }

    public LiveData<List<Customer>> getCustomerData() {
        return customerData;
    }

    public LiveData<String> getMessage() {
        return message;
    }
}
