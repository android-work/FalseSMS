package com.fake.sms.prank.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.fake.sms.prank.R;


/**
 * @author Mr.Liu
 */
public class CircleImageView extends ImageView {

    private Paint circlePaint;
    private Path circlePath;
    private Bitmap bitmap = null;
    private Bitmap scaledBitmap = null;
    private int mWidth;
    private int mHeight;
    private int raido = 200;

    public void setRadio(int radio){
        this.raido = radio;
    }

    public void setBackground(Bitmap bitmap) {

        this.bitmap = bitmap;
        invalidate();
    }

    public CircleImageView(Context context) {
        this(context,null,0);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {

        //初始化圆形画笔
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);

        circlePath = new Path();

        Drawable background = getBackground();
        if (background==null){
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_default);
            Log.e("tag","没有背景");
        }else{
            if (background instanceof BitmapDrawable){
                bitmap = ((BitmapDrawable)background).getBitmap();
            }else{
                bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                background.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
                background.draw(canvas);
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getSize(raido,widthMeasureSpec);
        mHeight = getSize(raido, heightMeasureSpec);
        setMeasuredDimension(mWidth,mHeight);

    }

    private int getSize(int defaultSize, int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = 0;
        switch (mode){
            case MeasureSpec.UNSPECIFIED:
                size = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
                size = defaultSize;
                break;
            case MeasureSpec.EXACTLY:
                size = MeasureSpec.getSize(measureSpec);
                break;
                default:
        }
        return size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = (float)Math.min(width, height)/mWidth;
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(width / scaleWidth), (int)(height / scaleWidth),false);
        Log.e("tag",Math.min(width, height)+"::bitmap:::"+width+"::::"+height+"::"+scaleWidth+":::"+bitmap);

        //将坐标移至画布中心
        canvas.translate(mWidth/2,mHeight/2);

        //将画布裁剪圆形
        circlePath.addCircle(0,0,mWidth/2, Path.Direction.CW);
        canvas.clipPath(circlePath);

        //将头像与背景取交
        circlePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap,-mWidth/2,-mWidth/2,circlePaint);

    }
}
