package com.fake.sms.prank.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fake.sms.prank.App;
import com.fake.sms.prank.R;
import com.fake.sms.prank.bean.SmsBean;
import com.fake.sms.prank.utils.Content;
import com.fake.sms.prank.utils.Utils;
import com.fake.sms.prank.view.CircleImageView;

import java.util.ArrayList;

/**
 * @author Mr.Liu
 */
public class SmsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<SmsBean> smsBeans;
    private final int widthPixels;
    private RvItemOnLongClickListener rvItemOnLongClickListener;
    private RvItemOnClickListener rvItemOnClickListener;

    public SmsAdapter(Context context, ArrayList<SmsBean> smsBeans) {

        this.context = context;
        this.smsBeans = smsBeans;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        widthPixels = displayMetrics.widthPixels;
    }

    @Override
    public int getItemViewType(int position) {
        if (smsBeans.get(position).isLeft() == -1){
            return -1;
        }else if (smsBeans.get(position).isLeft() == 0){
            return 0;
        }else if (smsBeans.get(position).isLeft() == 1) {
            return 1;
        }else if (smsBeans.get(position).isLeft() == 2) {
            return 2;
        }
        return 3;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == -1){
            View smsHeadItem = LayoutInflater.from(context).inflate(R.layout.sms_time_head_layout, null);
            return new TimeViewHolder(smsHeadItem);
        }else if (viewType == 0 || viewType == 1){
            View smsItem = LayoutInflater.from(context).inflate(R.layout.sms_item_layout, null);
            return new ViewHolder(smsItem);
        }else{
            View smsPhotoItme = LayoutInflater.from(context).inflate(R.layout.sms_item_photo_layout, null);
            return new PhotoViewHolder(smsPhotoItme);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (smsBeans==null || smsBeans.size()==0){
            return;
        }
        SmsBean smsBean = smsBeans.get(position);
        int resId = (int) Utils.getLong(context, App.THEME, 0);
        String uri = Utils.getStr(context, App.HEADURI, "");
        //处理文本消息
        if (getItemViewType(position) == 0 || getItemViewType(position) == 1) {
            if (getItemViewType(position) == 0) {
                ((ViewHolder) holder).smsLeftTv.setText(smsBean.getSmsContent());
                ((ViewHolder) holder).smsRightTv.setVisibility(View.GONE);
                Bitmap bit = BitmapFactory.decodeFile(uri);
                if (bit!=null){
                    ((ViewHolder) holder).smsHead.setBackground(bit);
                }

            } else if (getItemViewType(position) == 1) {
                ((ViewHolder) holder).smsRightTv.setText(smsBean.getSmsContent());
                ((ViewHolder) holder).smsLeftTv.setVisibility(View.GONE);
                ((ViewHolder) holder).smsHead.setVisibility(View.GONE);

                ((ViewHolder)holder).smsRightTv.setBackgroundResource(Content.drawableRes[resId]);
                ((ViewHolder) holder).smsRightTv.setTextColor(context.getResources().getColor(Content.sendTvBgs[resId]));
            }

            ((ViewHolder)holder).smsRl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (rvItemOnLongClickListener!=null){
                        rvItemOnLongClickListener.itemClick(view,position);
                    }
                    return true;
                }
            });

            //处理时间提醒
        }else if (getItemViewType(position) == -1){
            ((TimeViewHolder)holder).smsItemHead.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, Utils.dp2px(40)));
            ((TimeViewHolder)holder).smsItemHead.setGravity(Gravity.CENTER);
            ((TimeViewHolder)holder).smsItemHead.setText(smsBean.getSmsContent());

            //处理发送图片
        }else if (getItemViewType(position) == 2) {
            ((PhotoViewHolder) holder).leftIv.setVisibility(View.GONE);
            ((PhotoViewHolder) holder).circleImageView.setVisibility(View.GONE);
            Glide.with(context).load(smsBean.getSmsContent()).into(((PhotoViewHolder) holder).rightIv);
            ((PhotoViewHolder) holder).rightIv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (rvItemOnLongClickListener != null) {
                        rvItemOnLongClickListener.itemClick(view, position);
                    }
                    return true;
                }
            });
            ((PhotoViewHolder) holder).rightIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rvItemOnClickListener!=null){
                        rvItemOnClickListener.itemClick(view,position);
                    }
                }
            });
            ((PhotoViewHolder) holder).rightIv.setBackgroundResource(Content.drawableRes[resId]);

            //处理接受图片
        }else if (getItemViewType(position) == 3){
            Glide.with(context).load(smsBean.getSmsContent()).into(((PhotoViewHolder)holder).leftIv);
            ((PhotoViewHolder) holder).rightIv.setVisibility(View.GONE);
            ((PhotoViewHolder) holder).leftIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rvItemOnClickListener!=null){
                        rvItemOnClickListener.itemClick(view,position);
                    }
                }
            });
            Bitmap bit = BitmapFactory.decodeFile(uri);
            if (bit!=null){
                ((PhotoViewHolder) holder).circleImageView.setBackground(bit);
            }

        }
    }

    public void updata(SmsBean smsBean,RecyclerView recyclerView){
        smsBeans.add(smsBean);
        notifyDataSetChanged();
        if (smsBeans.size()!=0) {
            recyclerView.smoothScrollToPosition(smsBeans.size() - 1);
        }
    }

    public void updata(RecyclerView recyclerView){
        notifyDataSetChanged();
        if (smsBeans.size()!=0) {
            recyclerView.smoothScrollToPosition(smsBeans.size() - 1);
        }
    }

    @Override
    public int getItemCount() {
        return smsBeans.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{

        private final CircleImageView smsHead;
        private final TextView smsLeftTv;
        private final TextView smsRightTv;
        private final RelativeLayout smsRl;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            smsHead = itemView.findViewById(R.id.item_sms_head);
            smsLeftTv = itemView.findViewById(R.id.item_sms_left_tv);
            smsRightTv = itemView.findViewById(R.id.item_sms_right_tv);
            smsRl = itemView.findViewById(R.id.sms_rl);
        }
    }

    private static class TimeViewHolder extends RecyclerView.ViewHolder{

        private final TextView smsItemHead;

        private TimeViewHolder(@NonNull View itemView) {
            super(itemView);

            smsItemHead = itemView.findViewById(R.id.sms_item_head);
        }
    }

    private static class PhotoViewHolder extends RecyclerView.ViewHolder{

        private final ImageView rightIv;
        private final ImageView leftIv;
        private final CircleImageView circleImageView;

        private PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            rightIv = itemView.findViewById(R.id.item_sms_right_iv);
            leftIv = itemView.findViewById(R.id.item_sms_left_iv);
            circleImageView = itemView.findViewById(R.id.item_sms_head);
        }
    }

    public interface RvItemOnLongClickListener{
        /**长按条目事件回掉
         * @param view"控件view"
         * @param position"条目位置"*/
        void itemClick(View view , int position);
    }

    public void setRvItemOnClickListener(RvItemOnLongClickListener rvItemOnLongClickListener){
        this.rvItemOnLongClickListener = rvItemOnLongClickListener;
    }

    public interface RvItemOnClickListener{
        void itemClick(View view,int position);
    }

    public void setRvItemOnClickListener(RvItemOnClickListener rvItemOnClickListener){
        this.rvItemOnClickListener = rvItemOnClickListener;
    }
}
