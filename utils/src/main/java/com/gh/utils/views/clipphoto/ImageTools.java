package com.gh.utils.views.clipphoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Create by feifei on 2016/7/27 13:40
 */
public class ImageTools {

    public static Bitmap convertToBitmap(String path,int width,int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(path,options);
        int w = options.outWidth;
        int h = options.outHeight;
        float scaleWidth = 0.f,scaleHeitht = 0.f;
        if (width > 2 || height > h){
            scaleWidth = ((float) width)/w;
            scaleHeitht = ((float) height)/h;
        }
        options.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth,scaleHeitht);
        options.inSampleSize = (int) scale;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        WeakReference<Bitmap> weakReference = new WeakReference<Bitmap>(bitmap);
        return  Bitmap.createScaledBitmap(weakReference.get(),width,height,true);
    }

    public static void savePhotoToSDCard(Bitmap bitmap,String path){
        File f = new File(path);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("fly", "savePhotoToSDCard: 保存图片出错了！");
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
