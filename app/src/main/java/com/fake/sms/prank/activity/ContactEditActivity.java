package com.fake.sms.prank.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fake.sms.prank.App;
import com.fake.sms.prank.utils.PhotoPath;
import com.fake.sms.prank.utils.Utils;
import com.fake.sms.prank.view.CircleImageView;
import com.fake.sms.prank.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.Runtime.getRuntime;

/**
 * @author Mr.Liu
 */
public class ContactEditActivity extends AppCompatActivity {

    private ImageView back;
    private CircleImageView contactHead;
    private EditText editName;
    private EditText editNumber;
    private TextView save;
    private String realPathFromUri;
    private TextView contactNameTv;
    private TextView contactNumberTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String contactName = intent.getStringExtra(App.CONTACTNAME);
        String contactNumber = intent.getStringExtra(App.CONTACTNUMBER);

        setContentView(R.layout.contact_edit_layout);

        initView();

        editName.setText(contactName);
        editNumber.setText(contactNumber);

        setListener();

        String uri = Utils.getStr(this, App.HEADURI, "");
        if (TextUtils.isEmpty(uri)){
            return;
        }
        Bitmap bit = BitmapFactory.decodeFile(uri);
        contactHead.setBackground(bit);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        contactHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入相册
                choosePhoto();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存信息
                String name = editName.getText().toString().trim();
                String number = editNumber.getText().toString().trim();
                Utils.saveStr(App.getApp(),App.CONTACTNAME,name);
                Utils.saveStr(App.getApp(),App.CONTACTNUMBER,number);

                Utils.saveStr(App.getApp(),App.HEADURI, realPathFromUri);

                setResult(RESULT_OK);
                finish();
            }
        });

        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    editName.setBackgroundResource(R.drawable.edit_select_bg);
                    editNumber.setBackgroundResource(R.drawable.edit_normal_bg);
                    contactNameTv.setVisibility(View.VISIBLE);
                    contactNumberTv.setVisibility(View.GONE);
                }
            }
        });

        editNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    editNumber.setBackgroundResource(R.drawable.edit_select_bg);
                    editName.setBackgroundResource(R.drawable.edit_normal_bg);
                    contactNumberTv.setVisibility(View.VISIBLE);
                    contactNameTv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initView() {
        back = findViewById(R.id.set_contact_back);
        contactHead = findViewById(R.id.contact_head);
        editName = findViewById(R.id.edit_contact_name);
        editNumber = findViewById(R.id.edit_contact_number);
        save = findViewById(R.id.contact_save);
        contactNameTv = findViewById(R.id.contact_name_tv);
        contactNumberTv = findViewById(R.id.contact_number_tv);
    }



    public void choosePhoto(){
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);

    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {
        if (res == RESULT_OK && data!=null) {
            try {
                /**
                 * 该uri是上一个Activity返回的
                 */
                Uri uri = data.getData();
                Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                contactHead.setBackground(bit);
                realPathFromUri = PhotoPath.getRealPathFromUri(this, uri);
                Log.e("tag",uri+":设置图片:"+ realPathFromUri);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("tag",e.getMessage());
                Toast.makeText(this,"程序崩溃",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Log.i("liang", "失败");
        }
    }


}
