package com.fake.sms.prank.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
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

import com.fake.sms.prank.App;
import com.fake.sms.prank.R;
import com.fake.sms.prank.bean.ThemeBean;
import com.fake.sms.prank.utils.Content;
import com.fake.sms.prank.utils.Utils;

import java.util.ArrayList;

/**
 * @author Mr.Liu
 */
public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ThemeBean> themeBeans;
    private RvItmeOnClickListener rvItmeOnClickListener;

    public ThemeAdapter(Context context, ArrayList<ThemeBean> themeBeans) {

        this.context = context;
        this.themeBeans = themeBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.theme_item_layout, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        GradientDrawable myGrad = (GradientDrawable) holder.themeTv.getBackground();
        myGrad.setColor(context.getResources().getColor(Content.sendBgs[position]));
        holder.themtRl.setLayoutParams(new RelativeLayout.LayoutParams(Utils.dp2px(100),Utils.dp2px(60)));
        holder.themeTv.setText("Theme"+(position+1));
        long aLong = Utils.getLong(context, App.THEME, 0);
        if (position == aLong){
            holder.themeOk.setVisibility(View.VISIBLE);
        }

        holder.themeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvItmeOnClickListener!=null){
                    rvItmeOnClickListener.itemClick(view,position);
                    holder.themeOk.setVisibility(View.VISIBLE);
                    Utils.saveLong(context, App.THEME,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return themeBeans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView themeTv;
        private final ImageView themeOk;
        private final RelativeLayout themtRl;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            themeTv = itemView.findViewById(R.id.theme_tv);
            themeOk = itemView.findViewById(R.id.theme_ok);
            themtRl = itemView.findViewById(R.id.theme_rl);
        }
    }

    public interface RvItmeOnClickListener{
        void itemClick(View view,int position);
    }

    public void setRvItmeOnClickListener(RvItmeOnClickListener rvItmeOnClickListener){
        this.rvItmeOnClickListener = rvItmeOnClickListener;
    }
}
