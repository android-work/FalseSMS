package com.fake.sms.prank.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fake.sms.prank.App;
import com.fake.sms.prank.R;
import com.fake.sms.prank.adapter.SmsAdapter;
import com.fake.sms.prank.bean.SmsBean;
import com.fake.sms.prank.db.DbOperation;

import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * @author Mr.Liu
 */
public class Utils {

    public static void saveBoolean(Context context,String key, boolean value){
        SharedPreferences sms = context.getSharedPreferences("sms", 0);
        SharedPreferences.Editor edit = sms.edit();
        edit.putBoolean(key,value);
        edit.commit();
    }

    public static boolean getBoolean(Context context,String key,boolean defValue){
        SharedPreferences sms = context.getSharedPreferences("sms", 0);
        boolean aBoolean = sms.getBoolean(key, defValue);
        return aBoolean;
    }

    public static void saveLong(Context context,String key,long value){
        SharedPreferences sms = context.getSharedPreferences("sms", 0);
        SharedPreferences.Editor edit = sms.edit();
        edit.putLong(key,value);
        edit.commit();
    }

    public static long getLong(Context context,String key,long defValue){
        SharedPreferences sms = context.getSharedPreferences("sms", 0);
        long aBoolean = sms.getLong(key, defValue);
        return aBoolean;
    }

    public static void saveStr(Context context,String key,String value){
        SharedPreferences sms = context.getSharedPreferences("sms", 0);
        SharedPreferences.Editor edit = sms.edit();
        edit.putString(key,value);
        edit.commit();
    }

    public static String getStr(Context context,String key, String defValue){
        SharedPreferences sms = context.getSharedPreferences("sms", 0);
        String aBoolean = sms.getString(key, defValue);
        return aBoolean;
    }

    public static String getWeek(){
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK)-1;
        String[] weekArray = App.getApp().getResources().getStringArray(R.array.weeks);
        return weekArray[week];
    }

    public static String getDate(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        String[] monthArray = App.getApp().getResources().getStringArray(R.array.month);

        int date = calendar.get(Calendar.DATE);
        return monthArray[month] + " "+date +" . ";
    }

    public static String getTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String time = "";

        if (hour > 12){
            time = String.format("%02d",(hour - 12)) +" : "+String.format("%02d",minute) +" "+App.getApp().getString(R.string.pm);
        }else{
            time = String.format("%02d",(hour)) +" : "+String.format("%02d",minute)+" "+App.getApp().getString(R.string.am);
        }
        return getWeek()+ " "+getDate()+time;
    }

    public static int dp2px(float dp){
        final float scale = App.getApp().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static void requestForAccess(Activity context){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    200);

        }
    }

    public static void addSms(int isLeft, String content, RecyclerView smsRv){
        SmsBean smsBean = new SmsBean();
        smsBean.setLeft(isLeft);
        smsBean.setSmsContent(content);
        smsBean.setCreateTime(System.currentTimeMillis()+"");

        DbOperation.getInstance().insertDB(smsBean);

        SmsAdapter smsAdapter = (SmsAdapter) smsRv.getAdapter();
        smsAdapter.updata(smsBean,smsRv);
    }


    public static void navigationBar(Activity activity){
        if (checkDeviceHasNavigationBar(activity)) {
            //显示虚拟按键
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
                //低版本sdk
                View v = activity.getWindow().getDecorView();
                v.setSystemUiVisibility(View.VISIBLE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                View decorView = activity.getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }

    private static boolean checkDeviceHasNavigationBar(Activity activity) {
        boolean hasNavigationBar = false;
        Resources rs = activity.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

}
