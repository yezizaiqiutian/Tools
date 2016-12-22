package com.gh.utils.views.clipphoto;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ClipImageBorderView extends View {

    private int mHorizontalPadding;

    private int mBorderWidth = 2;

    private Paint mPaint;

    private Canvas mCanvas;

    private Paint paint;

    private Bitmap fgBitmap;

    private boolean isCircle = false;

    public ClipImageBorderView(Context context) {
        this(context, null);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mBorderWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
                        .getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        // 实例化画笔并开启其抗锯齿和抗抖动
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        // 设置画笔透明度为0是关键！我们要让绘制的路径是透明的，然后让该路径与前景的底色混合“抠”出绘制路径
        paint.setARGB(128, 255, 0, 0);

        // 设置混合模式为DST_IN
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        // 设置画笔风格为描边
        paint.setStyle(Style.FILL);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 生成Bitmap
        fgBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(fgBitmap);
        mCanvas.drawColor(0x7F000000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设置绘制边框画笔
        mPaint.setColor(Color.parseColor("#eaeaea"));
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Style.STROKE);

        if (isCircle) {//圆形边框
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mHorizontalPadding, mPaint);
            // 绘制画布背景为中性灰
            canvas.drawBitmap(fgBitmap, 0, 0, null);
            mCanvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mHorizontalPadding, paint);
        } else {//方形边框
            int mVerticalPadding = getHeight() / 2 - getWidth() / 2 + mHorizontalPadding;
            canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth() - mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);
            // 绘制画布背景为中性灰
            canvas.drawBitmap(fgBitmap, 0, 0, null);
            mCanvas.drawRect(mHorizontalPadding, getHeight() / 2 - getWidth() / 2 + mHorizontalPadding, getWidth() - mHorizontalPadding, getHeight() / 2 + getWidth() / 2 - mHorizontalPadding, paint);
        }
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    public void setCircle(boolean circle) {
        isCircle = circle;
    }
}
