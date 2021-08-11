package com.b2gsoft.jamalpurqrscanner.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.b2gsoft.jamalpurqrscanner.Interface.Connectivity;
import com.b2gsoft.jamalpurqrscanner.Model.Data;
import com.b2gsoft.jamalpurqrscanner.Model.Password;
import com.b2gsoft.jamalpurqrscanner.Model.ProductInfo;
import com.b2gsoft.jamalpurqrscanner.Network.ApiService;
import com.b2gsoft.jamalpurqrscanner.Network.RetrofitInstance;
import com.b2gsoft.jamalpurqrscanner.R;
import com.b2gsoft.jamalpurqrscanner.Utils.SharedPreference;
import com.b2gsoft.jamalpurqrscanner.Utils.StaticValue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ScannerRepository {

    private Context context;
    private Connectivity connectivity;

    private SharedPreference sharedPreference;
    private static ScannerRepository instance;

    private MutableLiveData<String> message = new MutableLiveData<>();
    private MutableLiveData<ProductInfo> couponData = new MutableLiveData<>();

    private MutableLiveData<Boolean> isValidating = new MutableLiveData<>();
    private MutableLiveData<Boolean> isChanging = new MutableLiveData<>();

    private RetrofitInstance retrofitInstance = new RetrofitInstance();
    private ApiService service = retrofitInstance.getRetrofitInstance(retrofitInstance.BASE_URL).create(ApiService.class);


    public void init(Context context, Connectivity connectivity) {
        this.context = context;
        this.connectivity = connectivity;
        sharedPreference = new SharedPreference(context);
    }


    public static ScannerRepository getInstance() {

        if(instance == null) {
            instance = new ScannerRepository();
        }

        return instance;
    }


    public void validateCode(final String code) {

        if(StaticValue.isConnected) {

            if(StaticValue.isConnectionActive) {

                isValidating.setValue(true);

                Data data = sharedPreference.getCurrentUser();

                Log.e("Token ", data.getToken());

                Call<JsonObject> call = service.validateCode("Bearer " +data.getToken(), code);

                call.enqueue(new Callback<JsonObject>() {

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if(response.isSuccessful())
                        {
                            Log.e("Validation ", new Gson().toJson(response.body()));

                            Boolean isValid = response.body().get("status").getAsBoolean();

                            if(isValid) {

                                Gson gson = new Gson();
                                ProductInfo productInfo = gson.fromJson(response.body().get("data").toString(), ProductInfo.class);

                                couponData.postValue(productInfo);
                            }
                            else {

                                String validationMessage = response.body().get("message").getAsString();

                                if(TextUtils.equals(validationMessage, StaticValue.CodeExpired)) {

                                    message.postValue(context.getString(R.string.code_expired));
                                }
                                else if(TextUtils.equals(validationMessage, StaticValue.CodeAlreadyUsed)) {

                                    message.postValue(context.getString(R.string.code_used));
                                }
                                else if(TextUtils.equals(validationMessage, StaticValue.InvalidCode)) {

                                    message.postValue(context.getString(R.string.invalid_code));
                                }
                            }
                        }
                        else
                        {
                            if(response.code() == 500) {

                                message.postValue(context.getString(R.string.invalid_code_format));
                            }
                            else {

                                message.postValue(context.getString(R.string.validation_failed));
                            }

                            Log.e("RetrofitResponseError ", ""+response.raw());
                        }

                        isValidating.postValue(false);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                        message.postValue(context.getString(R.string.unknown_error));
                        isValidating.postValue(false);
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


    public void changePassword(Password password) {

        if(StaticValue.isConnected) {

            if(StaticValue.isConnectionActive) {

                isChanging.setValue(true);

                Data data = sharedPreference.getCurrentUser();

                Log.e("Token ", data.getToken());

                Call<JsonObject> call = service.changePassword("Bearer " +data.getToken(), password);

                call.enqueue(new Callback<JsonObject>() {

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if(response.isSuccessful())
                        {
                            Log.e("Password Change ", new Gson().toJson(response.body()));

                            Boolean isSuccessful = response.body().get("status").getAsBoolean();

                            if(isSuccessful) {

                                message.postValue(context.getString(R.string.password_change_success));
                            }
                            else {

                                String validationMessage = response.body().get("message").getAsString();

                                if(TextUtils.equals(validationMessage, StaticValue.CurrentPasswordDoesNotMatch)) {

                                    message.postValue(context.getString(R.string.current_password_wrong));
                                }
                                else if(TextUtils.equals(validationMessage, StaticValue.InvalidCode)) {

                                    message.postValue(context.getString(R.string.password_change_failed));
                                }
                            }
                        }
                        else
                        {
                            message.postValue(context.getString(R.string.password_change_failed));
                            Log.e("RetrofitResponseError ", ""+response.errorBody());
                        }

                        isChanging.postValue(false);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                        message.postValue(context.getString(R.string.unknown_error));
                        isChanging.postValue(false);
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

        couponData = new MutableLiveData<>();
        message = new MutableLiveData<>();
    }


    public LiveData<Boolean> getPasswordChangeCallStatus() {
        return isChanging;
    }

    public LiveData<Boolean> getValidationCallStatus() {
        return isValidating;
    }

    public LiveData<ProductInfo> getCouponData() {
        return couponData;
    }

    public LiveData<String> getMessage() {
        return message;
    }
}
