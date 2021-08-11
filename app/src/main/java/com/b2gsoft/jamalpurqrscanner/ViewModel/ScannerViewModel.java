package com.b2gsoft.jamalpurqrscanner.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.text.TextUtils;

import com.b2gsoft.jamalpurqrscanner.Interface.Connectivity;
import com.b2gsoft.jamalpurqrscanner.Model.Password;
import com.b2gsoft.jamalpurqrscanner.Model.ProductInfo;
import com.b2gsoft.jamalpurqrscanner.R;
import com.b2gsoft.jamalpurqrscanner.Repository.LoginRepository;
import com.b2gsoft.jamalpurqrscanner.Repository.ScannerRepository;


public class ScannerViewModel extends ViewModel {

    private Context context;
    private ScannerRepository scannerRepo = ScannerRepository.getInstance();
    private LoginRepository loginRepo = LoginRepository.getInstance();

    private MutableLiveData<String> response = new MutableLiveData<>();
    private LiveData<String> message = new MutableLiveData<>();

    private LiveData<ProductInfo> couponData = new MutableLiveData<>();

    private LiveData<Boolean> isValidating = new MutableLiveData<>();
    private LiveData<Boolean> isChanging = new MutableLiveData<>();
    private MutableLiveData<Boolean> isInputValid = new MutableLiveData<>();


    public void init(Context context, Connectivity connectivity) {
        this.context = context;
        loginRepo.resetValues();
        scannerRepo.init(context, connectivity);
        couponData = scannerRepo.getCouponData();
        isValidating = scannerRepo.getValidationCallStatus();
        isChanging = scannerRepo.getPasswordChangeCallStatus();
        message = scannerRepo.getMessage();
    }


    public void validateCode(String code) {
        scannerRepo.validateCode(code);
    }


    public void validateInput(Password password) {

        isInputValid.setValue(false);

        if(password.getCurrentPassword().isEmpty()) {
            response.setValue(context.getString(R.string.give_current_password));
        }
        else {

            if(password.getCurrentPassword().length() < 8) {
                response.setValue(context.getString(R.string.current_password_length_short));
            }
            else {

                if(password.getNewPassword().isEmpty()) {
                    response.setValue(context.getString(R.string.give_password));
                }
                else {

                    if(password.getNewPassword().length() < 8) {
                        response.setValue(context.getString(R.string.new_password_length_short));
                    }
                    else {

                        if(password.getCurrentPassword().isEmpty()) {
                            response.setValue(context.getString(R.string.confirm_password));
                        }
                        else {

                            if(password.getCurrentPassword().length() < 8) {
                                response.setValue(context.getString(R.string.confirm_password_length_short));
                            }
                            else {

                                if(!TextUtils.equals(password.getNewPassword(), password.getConfirmPassword())) {
                                    response.setValue(context.getString(R.string.password_do_not_match));
                                }
                                else {

                                    scannerRepo.changePassword(password);
                                    isInputValid.postValue(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public void resetValues() {

        response = new MutableLiveData<>();
        scannerRepo.resetValues();
    }


    public LiveData<String> getResponse() {
        return response;
    }

    public LiveData<Boolean> getValidationCallStatus() {
        return isValidating;
    }

    public LiveData<Boolean> getPasswordChangeCallStatus() {
        return isChanging;
    }

    public LiveData<Boolean> getInputValidationStatus() {
        return isInputValid;
    }

    public LiveData<ProductInfo> getCouponData() {
        return couponData;
    }

    public LiveData<String> getMessage() {
        return message;
    }
}
