//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.pasc.business.ewallet.picture.util;

import android.os.Environment;

import java.io.File;

public class FileUtils {
    private static final String TAG = "FileUtils";

    public FileUtils() {
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = "mounted".equals(Environment.getExternalStorageState());
        if(sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        }
        return "";
    }

    public static String getImgFolderPath() {
        String path = getSDPath() + "/" + "smt/" + "img/";
        File file = new File(path);
        if(!file.exists()) {
            file.mkdirs();
        }

        return path;
    }
}
