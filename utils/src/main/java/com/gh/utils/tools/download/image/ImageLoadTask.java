package com.gh.utils.tools.download.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.gh.utils.tools.data.FileUtil;
import com.gh.utils.tools.data.Md5;

import java.io.File;
import java.util.List;

/**
 * @author: gh
 * @description: 下载图片到本地Task
 * @date: 2016/9/14 14:13.
 */
@Deprecated
public class ImageLoadTask extends AsyncTask<List<String>, Integer, List<String>> {

    private Context mContext;
    private DownLoadBiz.OnComplateListener mOnComplateListener;

    private int successCount;
    private int errorCount;

    public ImageLoadTask(Context context, DownLoadBiz.OnComplateListener onComplateListener) {
        mContext = context;
        mOnComplateListener = onComplateListener;
    }

    @Override
    protected void onPreExecute() {
        successCount = 0;
        errorCount = 0;
    }

    @Override
    protected List<String> doInBackground(List<String>... items) {
        List<String> list = items[0];
        for (int i = 0; i < list.size(); i++) {
            String url = list.get(i);
            String image = null;
            Bitmap bitmap = null;
            try {
                bitmap = Glide.with(mContext)
                        .load(url)
                        .asBitmap()
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                if (bitmap != null) {
                    // 在这里执行图片保存方法
                    image = FileUtil.createFile(mContext, bitmap, Md5.getFileName(url) + ".jpg");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bitmap != null && !TextUtils.isEmpty(image)) {
                    successCount++;
                } else {
                    errorCount++;
                }
            }
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<String> list) {
        for (String url : list) {
            File file = new File(url);
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(uri);
            mContext.sendBroadcast(intent);
        }
        mOnComplateListener.complate(successCount, errorCount);
    }
}
