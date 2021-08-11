package com.b2gsoft.jamalpurqrscanner.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.b2gsoft.jamalpurqrscanner.Interface.Connectivity;
import com.b2gsoft.jamalpurqrscanner.Model.Data;
import com.b2gsoft.jamalpurqrscanner.Model.User;
import com.b2gsoft.jamalpurqrscanner.R;
import com.b2gsoft.jamalpurqrscanner.Repository.LoginRepository;


public class LoginViewModel extends ViewModel {

    private Context context;
    private LoginRepository loginRepo = LoginRepository.getInstance();

    private MutableLiveData<String> response = new MutableLiveData<>();
    private LiveData<String> message = new MutableLiveData<>();

    private LiveData<Boolean> isLogging = new MutableLiveData<>();

    private LiveData<Data> currentUserData = new MutableLiveData<>();


    public void init(Context context, Connectivity connectivity) {
        this.context = context;
        loginRepo.init(context, connectivity);
        currentUserData = loginRepo.getUserData();
        isLogging = loginRepo.getLoggingStatus();
        message = loginRepo.getMessage();
    }


    public void validateInput(User user) {

        if(user.getUsername().isEmpty()) {
            response.setValue(context.getString(R.string.give_username_));
        }
        else {

            if(user.getPassword().isEmpty()) {
                response.setValue(context.getString(R.string.give_password));
            }
            else {

                if(user.getPassword().length() < 8) {
                    response.setValue(context.getString(R.string.password_length_short));
                }
                else {
                    login(user);
                }
            }
        }
    }


    public void login(User user) {

        loginRepo.doLogin(user);
    }


    public void resetValues() {

        response = new MutableLiveData<>();
    }


    public LiveData<String> getResponse() {
        return response;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public LiveData<Data> getUserData() {
        return currentUserData;
    }

    public LiveData<Boolean> getLoggingStatus() {
        return isLogging;
    }
}
