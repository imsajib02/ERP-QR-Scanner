package com.b2gsoft.jamalpurqrscanner.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.b2gsoft.jamalpurqrscanner.Model.ProductInfo;
import com.b2gsoft.jamalpurqrscanner.R;
import com.b2gsoft.jamalpurqrscanner.Utils.SharedPreference;
import com.b2gsoft.jamalpurqrscanner.Utils.StaticValue;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import pl.droidsonroids.gif.GifImageView;

public class CouponInfoActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, batch, couponCode, expiryDate, amount, submit;
    private GifImageView loadingView;
    private ScrollView infoLayout;

    private ProductInfo couponInfo;
    private SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_info);

        sharedPreference = new SharedPreference(this);

        couponInfo = (ProductInfo) getIntent().getSerializableExtra(StaticValue.CouponInfo);

        productImage = (ImageView) findViewById(R.id.product_img);

        loadingView = (GifImageView) findViewById(R.id.loading_view);

        infoLayout = (ScrollView) findViewById(R.id.info_layout);

        productName = (TextView) findViewById(R.id.product_name);
        batch = (TextView) findViewById(R.id.product_batch);
        couponCode = (TextView) findViewById(R.id.coupon_code);
        expiryDate = (TextView) findViewById(R.id.expiry_date);
        amount = (TextView) findViewById(R.id.amount);
        submit = (TextView) findViewById(R.id.tv_submit);

        Picasso.with(this).load(couponInfo.getImage()).into(productImage, new Callback.EmptyCallback() {
            @Override public void onSuccess() {

                loadingView.setVisibility(View.GONE);
                infoLayout.setVisibility(View.VISIBLE);
            }
        });

        productName.setText(couponInfo.getName());
        batch.setText(couponInfo.getBatchNo());
        couponCode.setText(couponInfo.getCouponCode());
        expiryDate.setText(couponInfo.getExpiryDate());
        amount.setText(getString(R.string.bdt) + " " + couponInfo.getCouponAmount());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CouponInfoActivity.this, CouponSubmitActivity.class);
                intent.putExtra(StaticValue.CouponInfo, couponInfo);
                startActivity(intent);
                finish();
            }
        });
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

                Intent intent = new Intent(CouponInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
