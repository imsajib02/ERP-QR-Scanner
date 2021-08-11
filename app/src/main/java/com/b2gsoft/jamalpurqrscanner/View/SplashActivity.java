package com.b2gsoft.jamalpurqrscanner.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.b2gsoft.jamalpurqrscanner.Interface.Connectivity;
import com.b2gsoft.jamalpurqrscanner.R;
import com.b2gsoft.jamalpurqrscanner.Repository.LoginRepository;

public class SplashActivity extends AppCompatActivity implements Connectivity {

    private ImageView logo;
    private LoginRepository loginRepo = LoginRepository.getInstance();
    private Intent intent;
    private Connectivity connectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        connectivity = this;

        loginRepo.init(this, connectivity);

        logo = (ImageView) findViewById(R.id.splash_logo);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        logo.setAnimation(animation);

        Launcher launcher = new Launcher();
        launcher.start();
    }

    @Override
    public void notConnected() {

    }

    @Override
    public void noActiveConnection() {

    }

    private class Launcher extends Thread {

        public void run() {

            try{
                sleep(3000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            if(loginRepo.isLoggedIn()) {

                intent = new Intent(SplashActivity.this, ScannerActivity.class);
            }
            else {

                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }
    }
}