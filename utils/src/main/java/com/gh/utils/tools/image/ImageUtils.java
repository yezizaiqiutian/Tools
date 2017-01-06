package com.gh.utils.tools.image;

import android.media.ExifInterface;
import android.text.TextUtils;

import java.lang.reflect.Field;

/**
 * @author: gh
 * @description: Image工具类
 * @date: 2016/12/27 11:04
 * @note:
 */

public class ImageUtils {

    /**
    * 把原图的Exif信息一项一项的读出来再设置进压缩后的图片文件中
    * @param oldFilePath
    * @param newFilePath
    * @throws Exception
    */
    public static void saveExif(String oldFilePath, String newFilePath) throws Exception {
        ExifInterface oldExif=new ExifInterface(oldFilePath);
        ExifInterface newExif=new ExifInterface(newFilePath);
        Class<ExifInterface> cls = ExifInterface.class;
        Field[] fields = cls.getFields();
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            if (!TextUtils.isEmpty(fieldName) && fieldName.startsWith("TAG")) {
                String fieldValue = fields[i].get(cls).toString();
                String attribute = oldExif.getAttribute(fieldValue);
                if (attribute != null) {
                    newExif.setAttribute(fieldValue, attribute);
                }
            }
        }
        newExif.saveAttributes();
    }

}
