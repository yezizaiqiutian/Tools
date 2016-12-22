package com.gh.utils.tools.data;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: gh
 * @description: 文件工具类
 * @date: 2016/10/14 15:37.
 * @see: 根据腾讯IM改造
 */
public class FileUtil {

    private static final String TAG = "FileUtil";
    private static String pathDiv = "/";
//    private static File cacheDir = !isExternalStorageWritable()?MyApplication.getContext().getFilesDir(): MyApplication.getContext().getExternalCacheDir();

    private FileUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static File getCacheDir(Context context) {
        return !isExternalStorageWritable() ? context.getFilesDir() : context.getExternalCacheDir();
    }

    /**
     * 创建临时文件
     *
     * @param type 文件类型
     */
    public static File getTempFile(Context context, FileType type) {
        try {
            File file = File.createTempFile(type.toString(), null, getCacheDir(context));
            file.deleteOnExit();
            return file;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取缓存文件地址
     */
    public static String getCacheFilePath(Context context, String fileName) {
        return getCacheDir(context).getAbsolutePath() + pathDiv + fileName;
    }

    /**
     * 判断缓存文件是否存在
     */
    public static boolean isCacheFileExist(Context context, String fileName) {
        File file = new File(getCacheFilePath(context, fileName));
        return file.exists();
    }

    /**
     * 将图片存储为文件
     *
     * @param bitmap 图片
     */
    public static String createFile(Context context, Bitmap bitmap, String filename) {
        File f = new File(getCacheDir(context), filename);
        try {
            if (f.createNewFile()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //设为0图片会压缩
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "create bitmap file error" + e);
        }
        if (f.exists()) {
            return f.getAbsolutePath();
        }
        return null;
    }

    /**
     * 将数据存储为文件
     *
     * @param data 数据
     */
    public static void createFile(Context context, byte[] data, String filename) {
        File f = new File(getCacheDir(context), filename);
        try {
            if (f.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(data);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "create file error" + e);
        }
    }

    /**
     * 将数据存储为文件
     *
     * @param data     数据
     * @param fileName 文件名
     * @param type     文件类型
     */
    public static boolean createFile(Context context, byte[] data, String fileName, String type) {
        if (isExternalStorageWritable()) {
            File dir = context.getExternalFilesDir(type);
            if (dir != null) {
                File f = new File(dir, fileName);
                try {
                    if (f.createNewFile()) {
                        FileOutputStream fos = new FileOutputStream(f);
                        fos.write(data);
                        fos.flush();
                        fos.close();
                        return true;
                    }
                } catch (IOException e) {
                    Log.e(TAG, "create file error" + e);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 从URI获取图片文件地址     方法1
     * ClipActivity中方法
     * Try to return the absolute file path from the given Uri  兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 从URI获取图片文件地址     方法2
     *
     * @param context 上下文
     * @param uri     文件uri
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getImageFilePath(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String path = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            if (!isMediaDocument(uri)) {
                try {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                } catch (IllegalArgumentException e) {
                    path = null;
                }
            }
        }
        if (path == null) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = ((Activity) context).managedQuery(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }

            path = null;
        }
        return path;
    }

    /**
     * 最好用的
     * 从URI获取文件地址
     *
     * @param context 上下文
     * @param uri     文件uri
     */
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @TargetApi(19)
    public static String getFilePath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 判断外部存储是否可用
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        Log.e(TAG, "ExternalStorage not mounted");
        return false;
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public enum FileType {
        IMG,
        AUDIO,
        VIDEO,
        FILE,
    }

    /**
     * 获取bitmap
     *
     * @param path
     * @return 如果获取失败, 则返回null
     */
    public static Bitmap pathToBitmap(Activity activity, String path) {
        Bitmap bitmap = null;
        if (path.equals("")) return null;
        File file = new File(path);
        if (!file.exists() || file.length() <= 0) {
            return null;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int reqWidth, reqHeight, width = options.outWidth, height = options.outHeight;
        if (width > height) {
            reqWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
            reqHeight = (reqWidth * height) / width;
        } else {
            reqHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
            reqWidth = (width * reqHeight) / height;
        }
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        float scaleX = (float) reqWidth / (float) (width / inSampleSize);
        float scaleY = (float) reqHeight / (float) (height / inSampleSize);
        Matrix mat = new Matrix();
        mat.postScale(scaleX, scaleY);
        bitmap = BitmapFactory.decodeFile(path, options);
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(path);
        } catch (IOException e) {
            return bitmap;
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                mat.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                mat.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                mat.postRotate(270);
        }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        return bitmap;
    }
}
