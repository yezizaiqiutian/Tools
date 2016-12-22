package com.gh.utils.apkupload;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

/**
 * 下载工具类
 * Created by 1 on 2016/6/4.
 */
public class FileDownloadManager {
    private DownloadManager dm;
    private Context context;
    private static FileDownloadManager instance;

    private FileDownloadManager(Context context) {
        dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        this.context = context.getApplicationContext();
    }

    public static FileDownloadManager getInstance(Context context) {
        if (instance == null) {
            instance = new FileDownloadManager(context);
        }
        return instance;
    }
    /**
     * @param uri
     * @param title
     * @param description
     * @return download id
     */
    public long startDownload(String uri, String title, String description) {
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(uri));

        //表示下载允许的网络类型，默认在任何网络下都允许下载
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //req.setAllowedOverRoaming(false);//移动网络情况下是否允许漫游

        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //设置文件的保存的位置[三种方式]
        //第一种
        //file:///storage/emulated/0/Android/data/your-package/files/Download/LetsGo.apk
        req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "LetsGo.apk");
        //第二种
        //file:///storage/emulated/0/Download/LetsGo.apk
        //req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "LetsGo.apk");
        //第三种 自定义文件路径
        //req.setDestinationUri()


        // 设置一些基本显示信息
        req.setTitle(title);//设置下载中通知栏提示的标题
        req.setDescription(description);//设置下载中通知栏提示的介绍
        //表示下载进行中和下载完成的通知栏是否显示。默认只显示下载中通知。
//        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //因为下载管理Ui中点击某个已下载完成文件及下载完成点击通知栏提示都会根据mimeType去打开文件
        req.setMimeType("application/vnd.android.package-archive");
        return dm.enqueue(req);
    }
    /**
     * 获取文件保存的路径
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return file path
     * @see FileDownloadManager#getDownloadUri(long)
     */
    public String getDownloadPath(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = dm.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                }
            } finally {
                c.close();
            }
        }
        return null;
    }


    /**
     * 获取保存文件的地址
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @see FileDownloadManager#getDownloadPath(long)
     */
    public Uri getDownloadUri(long downloadId) {
        return dm.getUriForDownloadedFile(downloadId);
    }

    public DownloadManager getDm() {
        return dm;
    }


    /**
     * 获取下载状态
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return int
     * @see DownloadManager#STATUS_PENDING
     * @see DownloadManager#STATUS_PAUSED
     * @see DownloadManager#STATUS_RUNNING
     * @see DownloadManager#STATUS_SUCCESSFUL
     * @see DownloadManager#STATUS_FAILED
     */
    public int getDownloadStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = dm.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));

                }
            } finally {
                c.close();
            }
        }
        return -1;
    }
}
