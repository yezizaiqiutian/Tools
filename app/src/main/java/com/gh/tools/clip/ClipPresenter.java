package com.gh.tools.clip;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;

import com.gh.tools.base.BasePresenter;
import com.gh.tools.base.Constant;
import com.gh.utils.tools.data.FileUtil;

import java.io.File;

/**
 * @author: gh
 * @description:
 * @date: 2016/10/18 17:25.
 */

public class ClipPresenter implements BasePresenter {

    private ClipView mView;
    private Activity mActivity;
    private File mFile;

    public ClipPresenter(ClipView view) {
        this.mView = view;
        this.mActivity = (Activity) view;
        mFile = FileUtil.getTempFile(mActivity, FileUtil.FileType.IMG);
    }

    /**
     * 显示Pop
     *
     * @param view
     */
    public void showPop(View view) {
        new SelectPhotoPop(mActivity, this).showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 跳转到相机
     */
    public void toCamera() {
        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
        mActivity.startActivityForResult(intent, Constant.REQUEST_CAPTURE);
    }

    /**
     * 跳转到相册
     */
    public void toAlbum() {
        Intent intent = null;
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mActivity.startActivityForResult(Intent.createChooser(intent, "请选择图片"), Constant.REQUEST_PICK);
    }

    public File getmFile() {
        return mFile;
    }

    @Override
    public void recycle() {
        mView = null;
        mActivity = null;
        mFile = null;
    }
}
