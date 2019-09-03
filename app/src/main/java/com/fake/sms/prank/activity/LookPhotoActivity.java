package com.fake.sms.prank.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fake.sms.prank.R;

/**
 * @author Mr.Liu
 */
public class LookPhotoActivity extends AppCompatActivity {

    private ImageView lookBigIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String photo = intent.getStringExtra("photo");
        setContentView(R.layout.look_photo_layout);

        lookBigIcon = findViewById(R.id.look_big_ic);

        if (photo == null){
            finish();
        }else {
            Glide.with(this).load(photo).into(lookBigIcon);
        }

        lookBigIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
