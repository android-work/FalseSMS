package com.fake.sms.prank.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fake.sms.prank.App;
import com.fake.sms.prank.R;
import com.fake.sms.prank.adapter.SmsAdapter;
import com.fake.sms.prank.bean.SmsBean;
import com.fake.sms.prank.db.DbOperation;
import com.fake.sms.prank.utils.Content;
import com.fake.sms.prank.utils.PhotoPath;
import com.fake.sms.prank.utils.Utils;
import com.fake.sms.prank.view.DeletePopupWindow;
import com.fake.sms.prank.view.InputView;
import com.fake.sms.prank.view.MyItemDecoration;
import com.fake.sms.prank.view.SendPopupWindow;
import com.work.load.scoreguide.SccorePopupWindow;
import java.util.ArrayList;

/**
 * @author Mr.Liu
 */
public class MainActivity extends AppCompatActivity {

    private static final int THEME_CODE = 0;
    private static final int HEAD_PHOTO_CODE = 1;
    private static final int SEND_PHOTO_CODE = 100;
    private RelativeLayout actionbarBg;
    private TextView contactName;
    private TextView contactNumber;
    private ImageView contactMenu;
    private RecyclerView smsRv;
    private InputView inputView;
    private LinearLayout contact;
    private ArrayList<SmsBean> smsBeans = new ArrayList<>();
    private boolean isExit = true;
    private ImageView noMessageIc;
    private LinearLayout noMessageLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();

        setListener();

        int aLong = (int) Utils.getLong(this, App.THEME, 0);
        actionbarBg.setBackgroundResource(Content.sendBgs[aLong]);

        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取姓名号码
        String name = Utils.getStr(this, App.CONTACTNAME, "John Doe");
        String number = Utils.getStr(this, App.CONTACTNUMBER, "");
        contactName.setText(name);
        contactNumber.setText(number);
    }

    private void initData() {

        String number = contactNumber.getText().toString().trim();
        if (TextUtils.isEmpty(number)){
            createAnimator(0,Utils.dp2px(15));
        }else{
            createAnimator(Utils.dp2px(15),Utils.dp2px(5));
        }

        //获取聊天信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                smsBeans = DbOperation.getInstance().queryDB();
                smsRv.post(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter();
                    }
                });
            }
        }).start();
    }

    /**联系人信息的动画*/
    private void createAnimator(float s,float e){
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, s, e);
        translateAnimation.setDuration(100);
        translateAnimation.setFillAfter(true);
        contactName.startAnimation(translateAnimation);
    }

    /**设置聊天信息的适配器*/
    private void setAdapter() {
        final SmsAdapter smsAdapter = new SmsAdapter(this, smsBeans);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        smsRv.setLayoutManager(linearLayoutManager);
        smsRv.setAdapter(smsAdapter);
        smsRv.addItemDecoration(new MyItemDecoration(0,0,0,Utils.dp2px(15)));

        if (smsBeans!=null && smsBeans.size()!=0){
            noMessageLl.setVisibility(View.GONE);
            smsRv.smoothScrollToPosition(smsBeans.size()-1);
        }else {
            noMessageLl.setVisibility(View.VISIBLE);
        }


        /**聊天信息长按事件*/
        smsAdapter.setRvItemOnClickListener(new SmsAdapter.RvItemOnLongClickListener() {
            @Override
            public void itemClick(View view, final int position) {
                //弹窗提示删除
                DeletePopupWindow deletePopupWindow = new DeletePopupWindow(MainActivity.this,position,smsBeans);
                deletePopupWindow.show(smsRv,smsBeans.get(position).getSmsContent());

                deletePopupWindow.setRemoveItemListener(new DeletePopupWindow.RemoveItemListenr() {
                    @Override
                    public void remove() {
                        smsAdapter.updata(smsRv);
                        if (smsBeans.size() == 0){
                            noMessageLl.setVisibility(View.VISIBLE);
                        }else{
                            noMessageLl.setVisibility(View.GONE);
                        }
                    }
                });

                deletePopupWindow.setClearAllItemListener(new DeletePopupWindow.ClearAllItmeListener() {
                    @Override
                    public void clearAll() {
                        smsAdapter.updata(smsRv);
                        smsBeans.clear();
                        noMessageLl.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        /**图片点击查看大图*/
        smsAdapter.setRvItemOnClickListener(new SmsAdapter.RvItemOnClickListener() {
            @Override
            public void itemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, LookPhotoActivity.class);
                intent.putExtra("photo", smsBeans.get(position).getSmsContent());
                startActivity(intent);
            }
        });
    }

    private void setListener() {
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ContactEditActivity.class);
                intent.putExtra(App.CONTACTNAME, contactName.getText().toString().trim());
                intent.putExtra(App.CONTACTNUMBER, contactNumber.getText().toString().trim());
                startActivityForResult(intent,1);
            }
        });

        contactMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent,0);
            }
        });

        /**消息发送后，刷新列表*/
        inputView.setRefreshRecyclerViewListener(new InputView.RefreshRecyclerViewListener() {
            @Override
            public void refresh(SmsBean smsBean) {
                SmsAdapter smsAdapter = (SmsAdapter) smsRv.getAdapter();
                smsAdapter.updata(smsBean,smsRv);

                if (smsBeans.size() == 0){
                    noMessageLl.setVisibility(View.VISIBLE);
                }else{
                    noMessageLl.setVisibility(View.GONE);
                }
            }
        });

        /**发送图片*/
        inputView.setChosePhotoListener(new InputView.ChosePhotoListener() {
            @Override
            public void chosePhoto() {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });
    }

    private void initView() {
        actionbarBg = findViewById(R.id.contact_actionbar_rl);
        contact = findViewById(R.id.contact_ll);
        contactName = findViewById(R.id.contact_name);
        contactNumber = findViewById(R.id.contact_number);
        contactMenu = findViewById(R.id.contact_menu);
        smsRv = findViewById(R.id.contact_sms_rv);
        inputView = findViewById(R.id.input_view);
        noMessageIc = findViewById(R.id.no_message_ic);
        noMessageLl = findViewById(R.id.no_message_ll);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            //主题刷新
            if (requestCode == THEME_CODE && data!=null){
                int position = data.getIntExtra("position",0);
                actionbarBg.setBackgroundResource(Content.sendBgs[position]);
                noMessageIc.setImageResource(Content.imagRes[position]);
                SmsAdapter smsAdapter = (SmsAdapter) smsRv.getAdapter();
                smsAdapter.updata(smsRv);
                inputView.setResId(position);

                //头像的刷新
            }else if (requestCode == HEAD_PHOTO_CODE && data!=null){
                SmsAdapter smsAdapter = (SmsAdapter) smsRv.getAdapter();
                if (smsAdapter!=null) {
                    smsAdapter.updata(smsRv);
                }

                //发送图片刷新
            }else if(requestCode == SEND_PHOTO_CODE && data!=null){
                try {
                    Uri uri = data.getData();
                    final String realPathFromUri = PhotoPath.getRealPathFromUri(this, uri);
                    Utils.saveStr(this,App.SENDPHOTO,realPathFromUri);
                    SendPopupWindow sendPopupWindow = new SendPopupWindow(this, (int) Utils.getLong(this, App.THEME, 0));
                    sendPopupWindow.show(smsRv);
                    /**发送图片消息回掉刷新*/
                    sendPopupWindow.setSmsSendOrReceiverPhotoListener(new SendPopupWindow.SmsSendOrReceiverPhotoListener() {
                        @Override
                        public void smsMethod(int flag) {
                            //保存并刷新发送图片
                            Utils.addSms(flag,realPathFromUri,smsRv);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isExit){
            isExit = false;
            boolean aBoolean = Utils.getBoolean(this, App.ISFIRST, true);
            if (aBoolean){
                Utils.saveBoolean(this,App.ISFIRST,false);
                SccorePopupWindow scorePopupWindow = new SccorePopupWindow(this);
                scorePopupWindow.show(smsRv,getString(R.string.app_name));
            }
        }else{
            finish();
        }
    }

}
