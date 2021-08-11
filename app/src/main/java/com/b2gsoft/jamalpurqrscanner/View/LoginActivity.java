package com.b2gsoft.jamalpurqrscanner.View;

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
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.b2gsoft.jamalpurqrscanner.Interface.Connectivity;
import com.b2gsoft.jamalpurqrscanner.Model.Data;
import com.b2gsoft.jamalpurqrscanner.Model.User;
import com.b2gsoft.jamalpurqrscanner.R;
import com.b2gsoft.jamalpurqrscanner.Utils.ConnectivityStatus;
import com.b2gsoft.jamalpurqrscanner.ViewModel.LoginViewModel;

public class LoginActivity extends AppCompatActivity implements Connectivity {

    private EditText etUsername, etPassword;
    private TextView tvLogin;
    private RelativeLayout rooView;

    private LoginViewModel loginViewModel;
    private ProgressDialog progressDialog;
    private User user;
    private Snackbar snack;

    private ConnectivityStatus connectivityStatus;
    private Connectivity connectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        connectivityStatus = new ConnectivityStatus();
        connectivity = this;

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.init(this, connectivity);

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);

        rooView = (RelativeLayout) findViewById(R.id.root_view);

        tvLogin = (TextView) findViewById(R.id.tv_login);


        loginViewModel.getResponse().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String response) {

                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        });


        loginViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String response) {

                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
            }
        });


        loginViewModel.getUserData().observe(this, new Observer<Data>() {
            @Override
            public void onChanged(@Nullable Data currentUserData) {

                if(currentUserData != null && currentUserData.getToken() != null) {

                    loginViewModel.resetValues();

                    Intent intent = new Intent(LoginActivity.this, ScannerActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        loginViewModel.getLoggingStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLogging) {

                if(isLogging != null) {

                    if(isLogging) {
                        showProgressDialog();
                    }
                    else {
                        dismissProgressDialog();
                    }
                }
            }
        });


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = new User(etUsername.getText().toString(), etPassword.getText().toString());
                loginViewModel.validateInput(user);
            }
        });
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


    private void showProgressDialog() {

        try {
            progressDialog.setMessage(getString(R.string.please_wait));
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
    public void onBackPressed() {

        finishAffinity();
        super.onBackPressed();
    }


    @Override
    protected void onPause() {

        unregisterReceiver(connectivityStatus);
        super.onPause();
    }


    @Override
    protected void onResume() {

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityStatus, intentFilter);
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
}
