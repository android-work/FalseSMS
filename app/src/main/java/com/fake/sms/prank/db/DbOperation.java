package com.fake.sms.prank.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fake.sms.prank.App;
import com.fake.sms.prank.bean.SmsBean;

import java.util.ArrayList;

/**
 * @author Mr.Liu
 */
public class DbOperation {

    private static DbOperation mDBOperation;
    private final DbHelper sms;

    private DbOperation(){
        sms = new DbHelper(App.getApp(), "SMS", null, 1);
    }

    public static DbOperation getInstance(){
        if (mDBOperation == null){
            synchronized (DbOperation.class){
                mDBOperation = new DbOperation();
            }
        }
        return mDBOperation;
    }

    public void insertDB(SmsBean smsBean){
        SQLiteDatabase db = sms.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("smsContent",smsBean.getSmsContent());
        contentValues.put("isLeft",smsBean.isLeft());
        contentValues.put("createTime",smsBean.getCreateTime());

        Log.e("tag","插入结果:"+smsBean.getSmsContent()+":"+smsBean.isLeft()+":"+smsBean.getCreateTime());

        db.insert(DbHelper.TABLES,null,contentValues);
    }

    public void deleteDB(String createTime){
        SQLiteDatabase db = sms.getReadableDatabase();
        db.delete(DbHelper.TABLES, "createTime = ?", new String[]{createTime});
    }

    public void deleteAll(){
        SQLiteDatabase db = sms.getReadableDatabase();
        db.delete(DbHelper.TABLES,null,null);
    }

    public ArrayList<SmsBean> queryDB(){
        SQLiteDatabase db = sms.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLES, null, null, null, null, null, null);
        ArrayList<SmsBean> smsBeans = new ArrayList<>();
        if (cursor!=null){

            while (cursor.moveToNext()){
                String smsContent = cursor.getString(0);
                int isLeft = cursor.getInt(1);
                String createTime = cursor.getString(2);

                Log.e("tag","查询结果:"+smsContent+":"+isLeft+":"+createTime);

                SmsBean smsBean = new SmsBean();
                smsBean.setSmsContent(smsContent);
                smsBean.setLeft(isLeft);
                smsBean.setCreateTime(createTime);

                smsBeans.add(smsBean);
            }
        }
        return smsBeans;
    }
}
