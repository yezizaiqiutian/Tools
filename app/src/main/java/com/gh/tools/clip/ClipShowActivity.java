package com.gh.tools.clip;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.tools.base.Constant;
import com.gh.utils.tools.data.FileUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: gh
 * @description: 头像裁剪后显示
 * @date: 2016/10/18 17:08.
 */

public class ClipShowActivity extends BaseActivity implements ClipView {

    @Bind(R.id.id_iv_show)
    ImageView id_iv_show;
    @Bind(R.id.id_ll_root)
    LinearLayout id_ll_root;

    private ClipPresenter mPresenter;

    /**
     * 启动ClipShowActivity
     *
     * @param context context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ClipShowActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        mPresenter = new ClipPresenter(this);
        setContentView(R.layout.activity_clip_show);
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case Constant.REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    ClipActivity.actionStart(this, mPresenter.getmFile().getAbsolutePath());
                }
                break;
            case Constant.REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    ClipActivity.actionStart(this, FileUtil.getFilePath(this, intent.getData()));
                }
                break;
            case Constant.REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    String path = intent.getStringExtra(Constant.PATH_NAME);
                    if (path != null) {
                        Glide.with(this).load(path).diskCacheStrategy(DiskCacheStrategy.ALL)/*.placeholder(R.mipmap.default_personal_avatar)*//*.transform(new GlideCircleTransform(this))*/.into(id_iv_show);
                    }
                }
                break;
        }
    }

    @OnClick(R.id.id_btn_select)
    public void onClick() {
        mPresenter.showPop(id_ll_root);
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

    @Override
    protected void onDestroy() {
        mPresenter.recycle();
        super.onDestroy();
    }
}
