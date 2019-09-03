package com.fake.sms.prank;

import android.app.Application;
import android.util.Log;

import com.fake.sms.prank.bean.SmsBean;
import com.fake.sms.prank.db.DbOperation;
import com.fake.sms.prank.utils.Utils;

/**
 * @author Mr.Liu
 */
public class App extends Application {
    public static final String CONTACTNAME = "contact_name";
    public static final String CONTACTNUMBER = "contact_number";
    public static final String ISFIRST = "is_first";
    public static final String ENTERTIME = "enter_time";
    public static final long ONEHOUR = 60 * 1000 * 60;
    public static final String HEADURI = "head_uri";
    public static final String THEME = "theme";
    public static final String SENDPHOTO = "send_photo";
    private static App app;

    public static App getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        boolean aBoolean = Utils.getBoolean(app, App.ISFIRST, true);
        if (aBoolean) {

            String[] smses = {app.getString(R.string.str1),app.getString(R.string.str2),app.getString(R.string.str3),
                    app.getString(R.string.str4),app.getString(R.string.str5)};

            //保存第一次进入的时间
            Utils.saveLong(app,App.ENTERTIME,System.currentTimeMillis());
            //添加时间信息
            SmsBean smsBeanHead = new SmsBean();
            smsBeanHead.setSmsContent(Utils.getTime());
            smsBeanHead.setLeft(-1);
            smsBeanHead.setCreateTime(System.currentTimeMillis()+"");
            DbOperation.getInstance().insertDB(smsBeanHead);

            //添加默认信息
            for (int i = 0; i < smses.length; i++) {
                SmsBean smsBean = new SmsBean();
                if (i == 0 || i == 2 || i == 3) {
                    smsBean.setLeft(1);
                } else if (i == 1 || i == 4) {
                    smsBean.setLeft(0);
                }
                smsBean.setSmsContent(smses[i]);
                smsBean.setCreateTime(System.currentTimeMillis()+"");
                Log.e("tag", smsBean.isLeft()+":sms:" + smsBean.getSmsContent());
                DbOperation.getInstance().insertDB(smsBean);
            }
        }
    }
}
