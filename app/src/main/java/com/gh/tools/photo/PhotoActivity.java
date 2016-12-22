package com.gh.tools.photo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.tools.base.Constant;
import com.gh.utils.tools.data.FileUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: gh
 * @description: 相册拍照
 * @date: 2016/10/26 11:08.
 * @note:
 */

public class PhotoActivity extends BaseActivity implements PhotoView {

    @Bind(R.id.id_iv_show)
    ImageView id_iv_show;
    @Bind(R.id.id_ll_root)
    LinearLayout id_ll_root;

    private PhotoPresenter mPresenter;

    /**
     * 启动PhotoActivity
     *
     * @param context context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PhotoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        mPresenter = new PhotoPresenter(this);
    }

    @OnClick(R.id.id_btn)
    public void onClick() {
        mPresenter.showPop(id_ll_root);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUEST_CAPTURE:
                if (mPresenter.getFileUri() != null) {
                    showImage(mPresenter.getFileUri().getPath());
                }
                break;
            case Constant.REQUEST_PICK:
                if (data != null) {
                    showImage(FileUtil.getFilePath(this, data.getData()));
                }
                break;
        }
    }

    private void showImage(String filePath) {
        Glide.with(this).load(filePath).placeholder(R.mipmap.ic_avatar).into(id_iv_show);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showToast(int msg) {

    }
}
