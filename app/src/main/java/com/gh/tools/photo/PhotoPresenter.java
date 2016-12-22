package com.gh.tools.photo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.gh.tools.base.BasePresenter;
import com.gh.tools.base.Constant;

import java.io.File;

/**
 * @author: gh
 * @description:
 * @date: 2016/10/26 11:16.
 */

public class PhotoPresenter implements BasePresenter{

    private PhotoView mView;
    private Activity mActivity;
    private Uri fileUri;

    public PhotoPresenter(PhotoView view) {
        this.mView = view;
        this.mActivity = (Activity) view;
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
        Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent_photo.resolveActivity(mActivity.getPackageManager()) != null) {
            //拍照传的路径uri为腾讯sdkdemo中的方式，这样会导致小米手机无法获取到图片file.length=0
//            File tempFile = FileUtil.getTempFile(mActivity,FileUtil.FileType.IMG);
            File tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/letsGo/temp/"),
                    System.currentTimeMillis() + ".jpg");
            if (tempFile != null) {
                fileUri = Uri.fromFile(tempFile);
            }
            intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            mActivity.startActivityForResult(intent_photo, Constant.REQUEST_CAPTURE);
        }
    }

    /**
     * 跳转到相册
     */
    public void toAlbum() {
//        Intent intent = null;
//        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        mActivity.startActivityForResult(Intent.createChooser(intent, "请选择图片"), Constant.REQUEST_PICK);

        Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
        intent_album.setType("image/*");
        mActivity.startActivityForResult(intent_album, Constant.REQUEST_PICK);
//        mActivity.startActivityForResult(Intent.createChooser(intent_album, "请选择图片"), Constant.REQUEST_PICK);
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    @Override
    public void recycle() {

    }
}
