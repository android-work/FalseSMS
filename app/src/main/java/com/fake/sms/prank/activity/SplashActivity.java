package com.fake.sms.prank.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fake.sms.prank.R;
import com.fake.sms.prank.utils.Utils;

/**
 * @author Mr.Liu
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_layout);

        findViewById(R.id.splash_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        });

        TextView splashTitle = findViewById(R.id.splash_title);
        splashTitle.setText(getString(R.string.app_name));

        Utils.requestForAccess(this);
    }


}
