package com.fake.sms.prank.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fake.sms.prank.R;
import com.fake.sms.prank.utils.Content;
import com.fake.sms.prank.utils.Utils;

/**
 * @author Mr.Liu
 */
public class SendPopupWindow extends PopupWindow {

    private Context context;
    private final View inflate;
    private int resId;
    private SmsSendOrReceiverListener smsSendOrReceiverListener;
    private SmsSendOrReceiverPhotoListener smsSendOrReceiverPhotoListener;

    public SendPopupWindow(Context context,int resId){

        this.context = context;

        inflate = LayoutInflater.from(context).inflate(R.layout.send_popup_layout, null);
        this.resId = resId;

        initView(inflate);

        this.setContentView(inflate);
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    private void initView(View inflate) {
        LinearLayout send = inflate.findViewById(R.id.send);
        LinearLayout receiver = inflate.findViewById(R.id.receiver);
        TextView tvSend = inflate.findViewById(R.id.tv_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (smsSendOrReceiverListener!=null){
                    smsSendOrReceiverListener.smsMethod(1);
                }
                if (smsSendOrReceiverPhotoListener!=null){
                    smsSendOrReceiverPhotoListener.smsMethod(2);
                    Log.e("tag","==============");
                }
                dismiss();
            }
        });

        receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (smsSendOrReceiverListener!=null){
                    smsSendOrReceiverListener.smsMethod(0);
                }
                if (smsSendOrReceiverPhotoListener!=null){
                    smsSendOrReceiverPhotoListener.smsMethod(3);
                    Log.e("tag","==============");
                }
                dismiss();
            }
        });

        send.setBackgroundResource(Content.drawableRes[resId]);
        tvSend.setTextColor(context.getResources().getColor(Content.sendTvBgs[resId]));
        tvSend.setText(context.getString(R.string.save));
    }



    public void show(View view){
        this.showAtLocation(view, Gravity.BOTTOM | Gravity.RIGHT,0,Utils.dp2px(50));
    }

    public interface SmsSendOrReceiverListener{
        void smsMethod(int flag);
    }

    public void setSmsSendOrReceiverListener(SmsSendOrReceiverListener smsSendOrReceiverListener){
        this.smsSendOrReceiverListener = smsSendOrReceiverListener;
    }

    public interface SmsSendOrReceiverPhotoListener{
        void smsMethod(int flag);
    }

    public void setSmsSendOrReceiverPhotoListener(SmsSendOrReceiverPhotoListener smsSendOrReceiverPhotoListener){
        this.smsSendOrReceiverPhotoListener = smsSendOrReceiverPhotoListener;
    }
}
