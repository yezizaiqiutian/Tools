package com.gh.tools.apkupload;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.utils.apkupload.ApkUpdateUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: gh
 * @description: Apk更新
 * @date: 2016/12/9 11:22
 * @note: 需要注册ApkInstallReceiver；
 */

public class ApkUploadActivity extends BaseActivity {

    @Bind(R.id.id_et_download)
    EditText id_et_download;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ApkUploadActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apkupload);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        super.initView();
        id_et_download.setText("http://releases.b0.upaiyun.com/hoolay.apk");

    }

    @Override
    protected void initData() {
        super.initData();
        //如果没有停用,先去停用,然后点击下载按钮. 测试用户关闭下载服务
        showDownloadSetting();
    }

    @OnClick(R.id.id_btn_toload)
    public void onClick() {
        if (!canDownloadState()) {
            Toast.makeText(this, "下载服务不用,请您启用", Toast.LENGTH_SHORT).show();
            showDownloadSetting();
            return;
        }
        String url;
        if (TextUtils.isEmpty(id_et_download.getText().toString())) {
            url = "http://releases.b0.upaiyun.com/hoolay.apk";
        } else {
            url = id_et_download.getText().toString();
        }
        ApkUpdateUtils.download(this, url, getResources().getString(R.string.app_name));
    }

    /**
     * 显示下载管理程序的设置界面
     */
    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            startActivity(intent);
        }
    }

    /**
     * 查勘是否有下载管理程序
     * @param intent
     * @return
     */
    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 检查下载管理程序是否启用
     * @return
     */
    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
