package com.gh.utils.tools.phone;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * 这种方式是通过在程序安装后第一次运行后生成一个ID实现的，但该方式跟设备唯一标识不一样，
 * 它会因为不同的应用程序而产生不同的ID，而不是设备唯一ID。因此经常用来标识在某个应用中
 * 的唯一ID（即Installtion ID），或者跟踪应用的安装数量。
 * 很幸运的，Google Developer Blog提供了这样的一个框架：
 *
 * @author: gh
 * @description:
 * @date: 2016/10/14 11:52.
 */
public class InStallation {

    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";

    public synchronized static String id(Context context) {
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}
