package com.gh.tools.glide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.utils.tools.common.T;
import com.gh.utils.tools.glide.download.GlideCacheListener;
import com.gh.utils.tools.glide.download.GlideUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: gh
 * @description: 用Glide下载图片
 * @date: 2016/12/21 15:12
 * @note:
 */

public class GlideDownLoadActivity extends BaseActivity {

    @Bind(R.id.id_iv_showimage)
    ImageView id_iv_showimage;
    private String imageUrl = "http://desk.fd.zol-img.com.cn/t_s960x600c5/g4/M00/0D/01/Cg-4y1ULoXCII6fEAAeQFx3fsKgAAXCmAPjugYAB5Av166.jpg";

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GlideDownLoadActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glidedownload);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.id_btn_download, R.id.id_btn_havecache, R.id.id_btn_getcache, R.id.id_btn_showimageonlycache})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_download:
                GlideUtils.cacheImage(imageUrl, mContext, new MyGlideCacheListener());
                break;
            case R.id.id_btn_havecache:
                T.S(mActivity, GlideUtils.haveCache(mContext, imageUrl) ? "有缓存" : "无缓存");
                break;
            case R.id.id_btn_getcache:
                T.S(mActivity, GlideUtils.getCache(mContext, imageUrl) + "");
                break;
            case R.id.id_btn_showimageonlycache:
                Glide.with(mActivity).load(GlideUtils.getCache(mContext, imageUrl)).into(id_iv_showimage);
                break;
        }
    }

    private class MyGlideCacheListener implements GlideCacheListener {

        @Override
        public void success(String path) {
            T.S(mActivity, "缓存成功");
        }

        @Override
        public void error(Exception e) {
            T.S(mActivity, "缓存失败");
        }
    }
}
