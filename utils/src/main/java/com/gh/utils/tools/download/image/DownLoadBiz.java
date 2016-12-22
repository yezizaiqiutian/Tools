package com.gh.utils.tools.download.image;

import android.content.Context;

import java.util.List;

/**
 * @author: gh
 * @description: 下载图片到本地
 * 下载后图片与原图大小差距很大3M的图片下载完成14M,与保存的格式也有关系(JPEG/PNG)
 * @date: 2016/10/14 15:29.
 */
@Deprecated
public class DownLoadBiz {

    private Context mContext;
    private OnComplateListener mOnComplateListener;

    public DownLoadBiz(Context context) {
        this.mContext = context;
    }

    /**
     * 启动图片下载线程
     */
    public void downLoad(List<String> list) {
        new ImageLoadTask(mContext, mOnComplateListener).execute(list);
    }

    public void setOnComplateListener(OnComplateListener onComplateListener) {
        mOnComplateListener = onComplateListener;
    }

    public interface OnComplateListener {
        void complate(int successCount, int errorCount);
    }
}
