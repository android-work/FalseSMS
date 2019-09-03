package com.fake.sms.prank.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fake.sms.prank.R;
import com.fake.sms.prank.bean.SmsBean;
import com.fake.sms.prank.db.DbOperation;

import java.util.ArrayList;

/**
 * @author Mr.Liu
 */
public class DeletePopupWindow extends PopupWindow {

    private TextView title;
    private RemoveItemListenr removeItemListener;
    private ClearAllItmeListener clearAllItemListener;
    private Context context;
    private int position;
    private ArrayList<SmsBean> smsBeans;

    public DeletePopupWindow(Context context, int position, ArrayList<SmsBean> smsBeans){
        this.context = context;
        this.position = position;
        this.smsBeans = smsBeans;
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_layout,null);

        initView(inflate);

    }

    private void initView(View inflate) {

        TextView clearAll = inflate.findViewById(R.id.dialog_clear_all);
        TextView remove = inflate.findViewById(R.id.dialog_remove);
        title = inflate.findViewById(R.id.dialog_title);

        this.setContentView(inflate);
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DbOperation.getInstance().deleteDB(smsBeans.get(position).getCreateTime());
                smsBeans.remove(position);

                if (position != 0 && smsBeans.get(position-1).isLeft() == -1){
                    DbOperation.getInstance().deleteDB(smsBeans.get(position-1).getCreateTime());
                    smsBeans.remove(position-1);
                }

                if (removeItemListener!=null){
                    removeItemListener.remove();
                }
                DeletePopupWindow.this.dismiss();
            }
        });

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbOperation.getInstance().deleteAll();

                if (clearAllItemListener!=null){
                    clearAllItemListener.clearAll();
                }
                DeletePopupWindow.this.dismiss();
            }
        });


    }

    public void show(View view,String content){
        title.setText(content);
        this.showAtLocation(view, Gravity.CENTER,0,0);
    }

    public interface RemoveItemListenr{
         void remove();
    }

    public void setRemoveItemListener(RemoveItemListenr removeItemListener){
        this.removeItemListener = removeItemListener;
    }

    public interface ClearAllItmeListener{
         void clearAll();
    }

    public void setClearAllItemListener(ClearAllItmeListener clearAllItemListener){
        this.clearAllItemListener = clearAllItemListener;
    }
}
