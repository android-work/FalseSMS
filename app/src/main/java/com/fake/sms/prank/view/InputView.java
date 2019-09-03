package com.fake.sms.prank.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fake.sms.prank.App;
import com.fake.sms.prank.R;
import com.fake.sms.prank.bean.SmsBean;
import com.fake.sms.prank.db.DbOperation;
import com.fake.sms.prank.utils.Utils;

/**
 * @author Mr.Liu
 */
public class InputView extends LinearLayout {

    private ImageView inputAdd;
    private EditText smsInputEdit;
    private TextView smsSend;
    private Context context;
    private RefreshRecyclerViewListener refreshRecyclerViewListener;
    private int resId;
    private ChosePhotoListener chosePhotoListener;

    public InputView(Context context) {
        this(context,null,0);
    }

    public InputView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public InputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.input_layout, this, true);

        initView();

        setListener();
    }

    public void setResId(int resId){

        this.resId = resId;
    }

    private void setListener() {
        smsSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sms = smsInputEdit.getText().toString();
                if (TextUtils.isEmpty(sms)){
                    Toast.makeText(context,context.getString(R.string.sms_toast),0).show();
                    return;
                }
                SendPopupWindow sendPopupWindow = new SendPopupWindow(context,resId);
                sendPopupWindow.show(InputView.this);

                sendPopupWindow.setSmsSendOrReceiverListener(new SendPopupWindow.SmsSendOrReceiverListener() {
                    @Override
                    public void smsMethod(int flag) {
                        //判断是否需要显示时间
                        isShowTime();
                        //获取输入信息，保存并发送
                        SmsBean smsBean = new SmsBean();
                        smsBean.setCreateTime(System.currentTimeMillis()+"");
                        smsBean.setSmsContent(sms);
                        smsBean.setLeft(flag);

                        DbOperation.getInstance().insertDB(smsBean);
                        //毁掉通知rv刷新数据，并清空输入框内容
                        if (refreshRecyclerViewListener!=null){
                            refreshRecyclerViewListener.refresh(smsBean);
                        }
                        smsInputEdit.setText("");
                    }
                });

            }
        });

        inputAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入相册
                if (chosePhotoListener!=null){
                    chosePhotoListener.chosePhoto();
                }
            }
        });
    }

    private void isShowTime() {
        long enterTime = Utils.getLong(context, App.ENTERTIME, System.currentTimeMillis());
        long intervalTime = System.currentTimeMillis() - enterTime;
        if (intervalTime > App.ONEHOUR){
            Log.e("tag","需要展现时间");
            SmsBean smsBean = new SmsBean();
            smsBean.setCreateTime(System.currentTimeMillis()+"");
            smsBean.setLeft(-1);
            smsBean.setSmsContent(Utils.getTime());

            DbOperation.getInstance().insertDB(smsBean);

            if (refreshRecyclerViewListener!=null){
                refreshRecyclerViewListener.refresh(smsBean);
            }
        }

        Utils.saveLong(context,App.ENTERTIME,System.currentTimeMillis());
    }

    private void initView() {
        inputAdd = findViewById(R.id.input_add);
        smsInputEdit = findViewById(R.id.sms_input_edit);
        smsSend = findViewById(R.id.sms_send);
    }

    /**发送内容接口*/
    public interface RefreshRecyclerViewListener{
        void refresh(SmsBean smsBean);
    }

    public void setRefreshRecyclerViewListener(RefreshRecyclerViewListener refreshRecyclerViewListener){
        this.refreshRecyclerViewListener = refreshRecyclerViewListener;
    }

    /**选择发送图片接口*/
    public interface ChosePhotoListener{
        void chosePhoto();
    }

    public void setChosePhotoListener(ChosePhotoListener chosePhotoListener){
        this.chosePhotoListener = chosePhotoListener;
    }
}
