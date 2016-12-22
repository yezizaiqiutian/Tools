package com.gh.tools.clip;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gh.tools.LoadingDialog;
import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.tools.base.Constant;
import com.gh.utils.tools.data.FileUtil;
import com.gh.utils.tools.data.GetTimestamp;
import com.gh.utils.views.clipphoto.ClipImageLayout;
import com.gh.utils.views.clipphoto.ImageTools;

import java.io.File;

import static com.gh.tools.base.Constant.PATH_NAME;

/**
 * @author: gh
 * @description: 头像裁剪
 * @date: 2016/10/18 17:08.
 */

public class ClipActivity extends BaseActivity {

    private ClipImageLayout mClipImageLayout;
    private TextView id_tv_action_clip;
    private LoadingDialog loadingDialog = new LoadingDialog();
    private TextView id_tv_action_cancle;

    /**
     * 启动ClipActivity
     *
     * @param context context
     */
    public static void actionStart(Activity context, String path) {
        Intent intent = new Intent(context, ClipActivity.class);
        intent.putExtra("path", path);
        context.startActivityForResult(intent, Constant.REQUEST_CROP_PHOTO);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip);
    }

    @Override
    protected void initView() {
        /*初始化裁剪图片资源和裁剪形状*/
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        /*返回按钮*/
        id_tv_action_cancle = (TextView) findViewById(R.id.id_tv_action_cancle);
        id_tv_action_clip = (TextView) findViewById(R.id.id_tv_action_clip);
    }

    @Override
    protected void initData() {

        String path = getIntent().getStringExtra("path");
        if (path == null) {
            return;
        }
        Bitmap bitmap = FileUtil.pathToBitmap(this, path);
        if (bitmap == null) {
            Toast.makeText(this, "选择图片出错", Toast.LENGTH_SHORT).show();
        }
        mClipImageLayout.setBitmap(bitmap);
        mClipImageLayout.setIsCircle(true);
    }

    @Override
    protected void initListener() {
        id_tv_action_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        id_tv_action_clip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadingDialog.show(false, getFragmentManager());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = mClipImageLayout.clip();
                        /*裁剪图片并保存的缓存目录*/
                        String path = getCacheDir() + File.separator + GetTimestamp.getTimestamp() + "tmp.png";
                        ImageTools.savePhotoToSDCard(bitmap, path);

                        Intent intent = new Intent();
                        intent.putExtra(PATH_NAME, path);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }).start();
            }
        });
    }

    /**
     * Try to return the absolute file path from the given Uri  兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    //图片缩放、移动操作矩阵
    private Matrix matrix = new Matrix();

    /**
     * 初始化图片
     * step 1: decode 出 720*1280 左右的照片  因为原图可能比较大 直接加载出来会OOM
     * step 2: 将图片缩放 移动到imageView 中间
     */
    public Bitmap initSrcPic(Uri uri) {
        if (uri == null) {
            return null;
        }

        String path = getRealFilePathFromUri(this, uri);
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        //原图可能很大，现在手机照出来都3000*2000左右了，直接加载可能会OOM
        //这里decode出720*1280 左右的照片
        Bitmap bitmap = decodeSampledBitmap(path, 720, 1280);
        if (bitmap == null) {
            return null;
        }

        //竖屏拍照的照片，直接使用的话，会旋转90度，下面代码把角度旋转过来
        int rotation = 0; //查询旋转角度
        Matrix m = new Matrix();
        m.setRotate(rotation);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

        //图片的缩放比
        float scale;
        if (bitmap.getWidth() >= bitmap.getHeight()) {//宽图
            scale = (float) getScreenWidth(this) / bitmap.getWidth();
        } else {//高图
            //高的缩放比
            scale = (float) getScreenWidth(this) / bitmap.getHeight();
        }

        // 缩放
        matrix.postScale(scale, scale);
        // 平移,将缩放后的图片平移到imageview的中心
        //imageView的中心x
        int midX = getScreenWidth(this) / 2;
        //imageView的中心y
        int midY = getScreenWidth(this) / 2;
        //bitmap的中心x
        int imageMidX = (int) (bitmap.getWidth() * scale / 2);
        //bitmap的中心y
        int imageMidY = (int) (bitmap.getHeight() * scale / 2);
//        matrix.postTranslate(midX - imageMidX, midY - imageMidY);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 图片等比例压缩
     *
     * @param filePath
     * @param reqWidth  期望的宽
     * @param reqHeight 期望的高
     * @return
     */
    public static Bitmap decodeSampledBitmap(String filePath, int reqWidth,
                                             int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算InSampleSize
     * 宽的压缩比和高的压缩比的较小值  取接近的2的次幂的值
     * 比如宽的压缩比是3 高的压缩比是5 取较小值3  而InSampleSize必须是2的次幂，取接近的2的次幂4
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            int ratio = heightRatio < widthRatio ? heightRatio : widthRatio;
            // inSampleSize只能是2的次幂  将ratio就近取2的次幂的值
            if (ratio < 3)
                inSampleSize = ratio;
            else if (ratio < 6.5)
                inSampleSize = 4;
            else if (ratio < 8)
                inSampleSize = 8;
            else
                inSampleSize = ratio;
        }
        return inSampleSize;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return px
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels - dp2px(context, 5 * 2);
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
