package com.gh.utils.tools.glide.transform;

/**
 * @author: gh
 * @description:
 * @date: 2016/11/2 14:11.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by qly on 2016/6/22.
 * 将图片转化为圆角
 * 构造中第二个参数定义半径
 */
public class GlideRoundTransform extends BitmapTransformation {

    private static float radius = 0f;

    public GlideRoundTransform(Context context) {
        this(context, 4);
    }

    public GlideRoundTransform(Context context, int dp) {
        super(context);
        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
//        RectF rectF = new RectF(0f+2, 0f+2, source.getWidth()-2, source.getHeight()-2);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);

        /*新加*/
        //给图片添加边框
//        Paint paint2 = new Paint();
//        paint2.setStyle(Paint.Style.STROKE);
//        paint2.setAntiAlias(true);
//        // 设置画笔的颜色
//        paint2.setColor(Color.RED);
//        paint2.setStrokeWidth(2);
//        RectF rectF2 = new RectF(0f, 0f, source.getWidth(), source.getHeight());
//        canvas.drawRoundRect(rectF2, radius, radius, paint2);

        return result;
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }
}
