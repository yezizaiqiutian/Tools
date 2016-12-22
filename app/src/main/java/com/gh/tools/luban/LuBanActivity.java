package com.gh.tools.luban;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.LinearLayout;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.tools.base.Constant;
import com.gh.tools.photo.PhotoPresenter;
import com.gh.tools.photo.PhotoView;
import com.gh.utils.luban.Luban;
import com.gh.utils.tools.data.FileUtil;
import com.gh.utils.tools.data.GetTimestamp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author: gh
 * @description: LuBan压缩工具用法
 * @date: 2016/12/8 09:36
 * @note: https://github.com/shaohui10086/AdvancedLuban
 * https://github.com/Curzibn/Luban
 */

public class LuBanActivity extends BaseActivity implements PhotoView {

    @Bind(R.id.id_ll_root)
    LinearLayout id_ll_root;
    private PhotoPresenter mPresenter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LuBanActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luban);
        ButterKnife.bind(this);
        mPresenter = new PhotoPresenter(this);
    }

    @OnClick(R.id.id_btn_selectimg)
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
                    compressImage(mPresenter.getFileUri().getPath());
                }
                break;
            case Constant.REQUEST_PICK:
                if (data != null) {
                    compressImage(FileUtil.getFilePath(this, data.getData()));
                }
                break;
        }
    }

    /**
     * 执行压缩操作
     */
    private long start = 0;
    private void compressImage(String path) {
        File file = new File(path);

            Log.d("gh图片压缩", "圆图大小" + file.length()/1024);
        Log.d("gh图片压缩", "圆图大小" + file.length());

        int size = 500;
        Luban.get(this)
                .load(file)
                .setMaxSize(size)
                .setMaxHeight(1920)
                .setMaxWidth(1080)
                .putGear(Luban.CUSTOM_GEAR)
                .asObservable()
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        //开始压缩
                        start = System.currentTimeMillis();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        Log.d("gh图片压缩", "压缩后大小" + file.length()/1024);
                        Log.i("TAG:result",Formatter.formatFileSize(LuBanActivity.this, file.length()));
                        Log.i("TAG:result",
                                "运行时间:" + (System.currentTimeMillis() - start) / 1000f + "s");
                        copyFile(file.getAbsolutePath(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/#gh/"+ GetTimestamp.getTimestamp()+"text.png");
                    }
                });
    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newfile = new File(newPath);
            if (!newfile.exists()) {
                newfile.createNewFile();
            }
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
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
