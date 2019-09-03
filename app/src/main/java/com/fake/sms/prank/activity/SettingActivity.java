package com.fake.sms.prank.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fake.sms.prank.R;
import com.fake.sms.prank.adapter.ThemeAdapter;
import com.fake.sms.prank.bean.ThemeBean;
import com.fake.sms.prank.utils.Content;
import com.fake.sms.prank.utils.Utils;
import com.fake.sms.prank.view.MyItemDecoration;
import com.work.load.scoreguide.FeedbackActivity;
import com.work.load.scoreguide.SccorePopupWindow;

import java.util.ArrayList;

/**
 * @author Mr.Liu
 */
public class SettingActivity extends AppCompatActivity {

    private RecyclerView themeRv;
    private ImageView setBack;
    private LinearLayout setEvaluation;
    private LinearLayout setFeedback;
    private TextView setVersion;
    private LinearLayout aboutAdNative;
    private LinearLayout aboutAdBanner;
    private ArrayList<ThemeBean> themeBeans = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.set_layout);

        initView();

        initDate();

        setListener();
    }

    private void setListener() {
        setEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SccorePopupWindow scorePopupWindow = new SccorePopupWindow(SettingActivity.this);
                scorePopupWindow.show(setEvaluation,getString(R.string.app_name));
            }
        });

        setFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        setBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initDate() {

        for (int i = 0 ; i < Content.sendBgs.length; i ++){

            ThemeBean themeBean = new ThemeBean();
            themeBean.setBg(Content.sendBgs[i]);
            themeBean.setColor(Content.sendTvBgs[i]);
            themeBean.setContent("THEME"+(i+1));

            themeBeans.add(themeBean);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        themeRv.setLayoutManager(linearLayoutManager);
        ThemeAdapter themeAdapter = new ThemeAdapter(this, themeBeans);
        themeRv.setAdapter(themeAdapter);
        themeRv.addItemDecoration(new MyItemDecoration(0,0, Utils.dp2px(10),0));

        themeAdapter.setRvItmeOnClickListener(new ThemeAdapter.RvItmeOnClickListener() {
            @Override
            public void itemClick(View view, int position) {

                Intent intent = new Intent();
                intent.putExtra("position",position);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setVersion.setText(packageInfo.versionName);
    }

    private void initView() {
        themeRv = findViewById(R.id.theme_rv);
        setBack = findViewById(R.id.set_back);
        setEvaluation = findViewById(R.id.set_evaluation);
        setFeedback = findViewById(R.id.set_feedback);
        setVersion = findViewById(R.id.set_version);

        aboutAdNative = findViewById(R.id.about_ad_native);
        aboutAdBanner = findViewById(R.id.about_ad_banner);
    }
}
