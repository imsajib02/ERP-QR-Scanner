package com.b2gsoft.jamalpurqrscanner.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.b2gsoft.jamalpurqrscanner.Interface.Connectivity;
import com.b2gsoft.jamalpurqrscanner.Model.Data;
import com.b2gsoft.jamalpurqrscanner.Model.User;
import com.b2gsoft.jamalpurqrscanner.Network.ApiService;
import com.b2gsoft.jamalpurqrscanner.Network.RetrofitInstance;
import com.b2gsoft.jamalpurqrscanner.R;
import com.b2gsoft.jamalpurqrscanner.Utils.CurrentDateTime;
import com.b2gsoft.jamalpurqrscanner.Utils.SharedPreference;
import com.b2gsoft.jamalpurqrscanner.Utils.StaticValue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginRepository {

    private Context context;
    private Connectivity connectivity;
    private static LoginRepository instance;
    private SharedPreference sharedPreference;
    private CurrentDateTime currentDateTime = new CurrentDateTime();
    private MutableLiveData<Data> currentUserData = new MutableLiveData<>();
    private MutableLiveData<String> message = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLogging = new MutableLiveData<>();
    private RetrofitInstance retrofitInstance = new RetrofitInstance();
    private ApiService service = retrofitInstance.getRetrofitInstance(retrofitInstance.BASE_URL).create(ApiService.class);


    public void init(Context context, Connectivity connectivity) {
        this.context = context;
        this.connectivity = connectivity;
        sharedPreference = new SharedPreference(context);
    }


    public static LoginRepository getInstance() {

        if(instance == null) {
            instance = new LoginRepository();
        }

        return instance;
    }


    public void doLogin(final User user) {

        if(StaticValue.isConnected) {

            if(StaticValue.isConnectionActive) {

                isLogging.setValue(true);

                Call<JsonObject> call = service.login(user);

                call.enqueue(new Callback<JsonObject>() {

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if(response.isSuccessful())
                        {
                            Log.e("Login ", new Gson().toJson(response.body()));

                            try {

                                Boolean success = response.body().get("status").getAsBoolean();

                                if(success) {

                                    Gson gson = new Gson();
                                    Data data = gson.fromJson(response.body().get("data").toString(), Data.class);

                                    if(TextUtils.equals(data.getUser().getStatus(), StaticValue.AuthorizedUserStatus)) {

                                        String time = currentDateTime.getDateTime();

                                        Log.e("LoginTime ", time);

                                        sharedPreference.setLoginTime(time);
                                        sharedPreference.saveUser(data);
                                        currentUserData.postValue(data);
                                    }
                                    else {

                                        message.postValue(context.getString(R.string.unauthorized_user));
                                        isLogging.postValue(false);
                                    }
                                }
                                else {

                                    String error = response.body().get("errors").getAsJsonObject().get("login").getAsString();

                                    if(TextUtils.equals(error, StaticValue.InvalidUser)) {

                                        message.postValue(context.getString(R.string.invalid_user));
                                    }
                                    else {

                                        onLoginFailed();
                                    }
                                }
                            }
                            catch (Exception e) {

                                e.printStackTrace();
                                isLogging.postValue(false);
                            }
                        }
                        else
                        {
                            onLoginFailed();
                            Log.e("RetrofitResponseError ", ""+response.errorBody());
                        }

                        isLogging.postValue(false);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                        message.postValue(context.getString(R.string.unknown_error));
                        isLogging.postValue(false);
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


    private void onLoginFailed() {

        message.postValue(context.getString(R.string.login_failed));
        isLogging.postValue(false);
    }


    public boolean isLoggedIn() {

        boolean result = false;

        Data data = sharedPreference.getCurrentUser();

        if(data != null && data.getToken() != null) {

            String loginTime = sharedPreference.getLoginTime();
            String currentTime = currentDateTime.getDateTime();

            Log.e("Login Time ", loginTime);
            Log.e("Current Time ", currentTime);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            try {
                Date startDate = sdf.parse(currentTime);
                Date endDate = sdf.parse(loginTime);

                long millis = startDate.getTime() - endDate.getTime();
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);

                Log.e("Session Log ", minutes + " minutes");

                if(minutes <= Long.parseLong(data.getValidity())) {

                    result = true;
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    public void resetValues() {

        currentUserData = new MutableLiveData<>();
        message = new MutableLiveData<>();
    }


    public LiveData<Data> getUserData() {
        return currentUserData;
    }

    public LiveData<Boolean> getLoggingStatus() {
        return isLogging;
    }

    public LiveData<String> getMessage() {
        return message;
    }
}
