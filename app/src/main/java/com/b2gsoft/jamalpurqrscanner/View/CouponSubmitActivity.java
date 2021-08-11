package com.b2gsoft.jamalpurqrscanner.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.b2gsoft.jamalpurqrscanner.Interface.Connectivity;
import com.b2gsoft.jamalpurqrscanner.Interface.Session;
import com.b2gsoft.jamalpurqrscanner.Model.Customer;
import com.b2gsoft.jamalpurqrscanner.Model.Data;
import com.b2gsoft.jamalpurqrscanner.Model.ProductInfo;
import com.b2gsoft.jamalpurqrscanner.Model.Route;
import com.b2gsoft.jamalpurqrscanner.R;
import com.b2gsoft.jamalpurqrscanner.Utils.ConnectivityStatus;
import com.b2gsoft.jamalpurqrscanner.Utils.SessionValidation;
import com.b2gsoft.jamalpurqrscanner.Utils.SharedPreference;
import com.b2gsoft.jamalpurqrscanner.Utils.StaticValue;
import com.b2gsoft.jamalpurqrscanner.ViewModel.CouponSubmitViewModel;
import com.b2gsoft.jamalpurqrscanner.ViewModel.ScannerViewModel;

import java.util.List;

public class CouponSubmitActivity extends AppCompatActivity implements Connectivity, Session {

    private RelativeLayout rooView;
    private Spinner routeSpinner, customerSpinner;
    private TextView submit;

    private ProductInfo couponInfo;
    private SharedPreference sharedPreference;

    private CouponSubmitViewModel submitViewModel;
    private ProgressDialog progressDialog;

    private Snackbar snack;

    private ConnectivityStatus connectivityStatus;
    private Connectivity connectivity;

    private SessionValidation sessionValidation;
    private Session session;

    private Route selectedRoute;
    private Customer selectedCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_submit);

        couponInfo = (ProductInfo) getIntent().getSerializableExtra(StaticValue.CouponInfo);

        sharedPreference = new SharedPreference(this);
        progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);

        connectivity = this;
        session = this;

        connectivityStatus = new ConnectivityStatus();
        sessionValidation = new SessionValidation(this, session);

        rooView = (RelativeLayout) findViewById(R.id.root_view);

        routeSpinner = (Spinner) findViewById(R.id.sp_route);
        customerSpinner = (Spinner) findViewById(R.id.sp_customer);

        submit = (TextView) findViewById(R.id.tv_submit);

        submitViewModel = ViewModelProviders.of(this).get(CouponSubmitViewModel.class);
        submitViewModel.init(this, connectivity);


        submitViewModel.getNetworkCallStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isNetworkCallActive) {

                if(isNetworkCallActive != null) {

                    if(isNetworkCallActive) {
                        showProgressDialog(getString(R.string.please_wait));
                    }
                    else {
                        dismissProgressDialog();
                    }
                }
            }
        });


        submitViewModel.getSubmitStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isSuccess) {

                if(isSuccess != null && isSuccess) {

                    routeSpinner.setClickable(false);
                    customerSpinner.setClickable(false);
                    submit.setVisibility(View.GONE);

                    Launcher launcher = new Launcher();
                    launcher.start();
                }
            }
        });


        submitViewModel.getResponse().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String response) {

                Toast.makeText(CouponSubmitActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        });


        submitViewModel.getRouteData().observe(this, new Observer<List<Route>>() {
            @Override
            public void onChanged(@Nullable List<Route> routes) {

                if(routes != null) {

                    routes.add(0, new Route(getString(R.string.select_here)));

                    ArrayAdapter<Route> routeAdapter = new ArrayAdapter<Route>(CouponSubmitActivity.this, R.layout.spinner_layout, R.id.tv_name, routes) {

                        @Override
                        public boolean isEnabled(int position){

                            if(position == 0)
                            {
                                return false;
                            }
                            else
                            {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {

                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view.findViewById(R.id.tv_name);

                            if(position == 0)
                            {
                                tv.setTextColor(Color.RED);
                            }
                            else
                            {
                                tv.setTextColor(Color.WHITE);
                            }

                            return view;
                        }
                    };

                    routeSpinner.setAdapter(routeAdapter);
                    routeSpinner.setClickable(true);
                }
            }
        });


        routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position != 0) {

                    selectedRoute = (Route) parent.getSelectedItem();
                    submitViewModel.getCustomers(selectedRoute.getId());
                }
                else {

                    selectedRoute = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submitViewModel.getCustomerData().observe(this, new Observer<List<Customer>>() {
            @Override
            public void onChanged(@Nullable List<Customer> customers) {

                if(customers != null) {

                    customers.add(0, new Customer(getString(R.string.select_here)));

                    ArrayAdapter<Customer> customerAdapter = new ArrayAdapter<Customer>(CouponSubmitActivity.this, R.layout.spinner_layout, R.id.tv_name, customers) {

                        @Override
                        public boolean isEnabled(int position){

                            if(position == 0)
                            {
                                return false;
                            }
                            else
                            {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {

                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view.findViewById(R.id.tv_name);

                            if(position == 0)
                            {
                                tv.setTextColor(Color.RED);
                            }
                            else
                            {
                                tv.setTextColor(Color.WHITE);
                            }

                            return view;
                        }
                    };

                    customerSpinner.setAdapter(customerAdapter);
                    customerSpinner.setClickable(true);
                }
            }
        });


        customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position != 0) {

                    selectedCustomer = (Customer) parent.getSelectedItem();
                }
                else {

                    selectedCustomer = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitViewModel.validate(selectedRoute, selectedCustomer, couponInfo);
            }
        });
    }


    private void showProgressDialog(String message) {

        try {
            progressDialog.setMessage(message);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        catch (Exception e) {}
    }


    private void dismissProgressDialog() {

        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    protected void onPause() {

        unregisterReceiver(connectivityStatus);
        unregisterReceiver(sessionValidation);
        super.onPause();
    }


    @Override
    protected void onResume() {

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityStatus, intentFilter);
        registerReceiver(sessionValidation, intentFilter);
        super.onResume();
    }


    @Override
    public void onDestroy() {

        dismissProgressDialog();
        super.onDestroy();
    }


    @Override
    public void notConnected() {

        showSnackBar(getString(R.string.not_connected));
    }


    @Override
    public void noActiveConnection() {

        showSnackBar(getString(R.string.no_active_connection));
    }


    private void showSnackBar(String message) {

        snack = Snackbar.make(rooView, message, 3000);

        View view = snack.getView();
        view.setBackgroundColor(getResources().getColor(R.color.white));

        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(15);
        tv.setTypeface(null, Typeface.BOLD);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        else
        {
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        snack.show();
    }


    @Override
    public void onBackPressed() {

        submitViewModel.resetValues();
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.logout:

                sharedPreference.clearUserData();

                Intent intent = new Intent(CouponSubmitActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSessionEnd() {

        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.session_end);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button okay = (Button) dialog.findViewById(R.id.btn_okay);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                openLoginScreen();
            }
        });

        dialog.show();
    }


    private void openLoginScreen() {

        sharedPreference.clearUserData();
        submitViewModel.resetValues();

        Intent intent = new Intent(CouponSubmitActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private class Launcher extends Thread {

        public void run() {

            showSnackBar(getString(R.string.coupon_submit_success));

            try{
                sleep(2500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            finish();
        }
    }
}