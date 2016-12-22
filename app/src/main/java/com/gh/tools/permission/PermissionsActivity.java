package com.gh.tools.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;
import com.gh.utils.permission.Acp;
import com.gh.utils.permission.AcpListener;
import com.gh.utils.permission.AcpOptions;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: gh
 * @description: 6.0权限管理
 * @date: 2016/11/21 15:49.
 * @from: https://github.com/mylhyl/AndroidAcp
 */

public class PermissionsActivity extends BaseActivity {

    /**
     * 启动PermissionsActivity
     *
     * @param context context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PermissionsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button, R.id.button4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                requestAll();
                break;
            case R.id.button2:
                requestSD();
                break;
            case R.id.button3:
                requestIMEI();
                break;
            case R.id.button4:
                requestPHONE();
                break;
            case R.id.button:
                requestFragment();
                break;
        }
    }

    private void requestAll() {
        Acp.getInstance(this).request(
                new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_PHONE_STATE
                                , Manifest.permission.SEND_SMS)
                        .setDeniedMessage("拒绝框提示语")
                        .setRationalMessage("申请权限理由框提示语")
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        writeSD();
                        getIMEI();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        makeText(permissions.toString() + "权限拒绝");
                    }
                });
    }

    private void requestSD() {
        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        writeSD();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        makeText(permissions.toString() + "权限拒绝");
                    }
                });
    }

    private void requestIMEI() {
        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.READ_PHONE_STATE)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        getIMEI();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        makeText(permissions.toString() + "权限拒绝");
                    }
                });
    }

    private void requestPHONE() {
        Acp.getInstance(this).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CALL_PHONE).build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        //注意：不用用带参的构造方法 否则 android studio 环境出错，提示要你检查授权
/*
                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:13800138000"));
                        intentCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentCall);
*/

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:13800138000"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        makeText(permissions.toString() + "权限拒绝");
                    }
                });
    }

    private void requestFragment() {
        BlankActivity.actionStart(this);
    }

    private void writeSD() {
        File acpDir = getCacheDir("acp", this);
        if (acpDir != null)
            makeText("写SD成功：" + acpDir.getAbsolutePath());
    }

    private void getIMEI() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (tm != null)
            makeText("读imei成功：" + tm.getDeviceId());
    }

    public static File getCacheDir(String dirName, Context context) {
        File result;
        if (existsSdcard()) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir == null) {
                result = new File(Environment.getExternalStorageDirectory(),
                        "Android/data/" + context.getPackageName() + "/cache/" + dirName);
            } else {
                result = new File(cacheDir, dirName);
            }
        } else {
            result = new File(context.getCacheDir(), dirName);
        }
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return null;
        }
    }

    public static Boolean existsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private void makeText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
