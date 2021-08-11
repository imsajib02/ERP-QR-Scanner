package com.b2gsoft.jamalpurqrscanner.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.text.TextUtils;

import com.b2gsoft.jamalpurqrscanner.Interface.Connectivity;
import com.b2gsoft.jamalpurqrscanner.Model.Customer;
import com.b2gsoft.jamalpurqrscanner.Model.Password;
import com.b2gsoft.jamalpurqrscanner.Model.ProductInfo;
import com.b2gsoft.jamalpurqrscanner.Model.Route;
import com.b2gsoft.jamalpurqrscanner.R;
import com.b2gsoft.jamalpurqrscanner.Repository.CouponSubmitRepository;
import com.b2gsoft.jamalpurqrscanner.Repository.LoginRepository;
import com.b2gsoft.jamalpurqrscanner.Repository.ScannerRepository;

import java.util.List;


public class CouponSubmitViewModel extends ViewModel {

    private Context context;
    private CouponSubmitRepository couponSubmitRepo = CouponSubmitRepository.getInstance();

    private LiveData<Boolean> isNetworkCallActive = new MutableLiveData<>();
    private LiveData<Boolean> isSubmitSuccess = new MutableLiveData<>();
    private LiveData<String> message = new MutableLiveData<>();
    private MutableLiveData<String> response = new MutableLiveData<>();

    private LiveData<List<Route>> routeData = new MutableLiveData<>();
    private LiveData<List<Customer>> customerData = new MutableLiveData<>();


    public void init(Context context, Connectivity connectivity) {
        this.context = context;
        couponSubmitRepo.init(context, connectivity);
        couponSubmitRepo.getRoutes();
        routeData = couponSubmitRepo.getRouteData();
        customerData = couponSubmitRepo.getCustomerData();
        isNetworkCallActive = couponSubmitRepo.getNetworkCallStatus();
        isSubmitSuccess = couponSubmitRepo.getSubmitStatus();
        message = couponSubmitRepo.getMessage();
    }


    public void validate(Route route, Customer customer, ProductInfo productInfo) {

        if(route == null) {
            response.setValue(context.getString(R.string.select_route));
        }
        else {

            if(customer == null) {
                response.setValue(context.getString(R.string.select_customer));
            }
            else {

                couponSubmitRepo.submitCoupon(customer, productInfo);
            }
        }
    }


    public void resetValues() {

        response = new MutableLiveData<>();
        couponSubmitRepo.resetValues();
    }


    public void getCustomers(int routeID) {

        couponSubmitRepo.getCustomers(routeID);
    }


    public LiveData<String> getResponse() {
        return response;
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
