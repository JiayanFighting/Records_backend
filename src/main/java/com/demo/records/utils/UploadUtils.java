package com.demo.records.utils;

import java.util.Random;
import java.util.UUID;

public class UploadUtils {

    public static String getRealName(String name) {
        //get last "/"
        int index = name.lastIndexOf("\\");
        return name.substring(index + 1);
    }

    public static String getUUIDName(String realName) {
        int index = realName.lastIndexOf(".");
        if (index == -1) {
            return UUID.randomUUID().toString().replace("-", "").toUpperCase();
        } else {
            return UUID.randomUUID().toString().replace("-", "").toUpperCase() + realName.substring(index);
        }
    }

    public static String getDir() {
        String s = "0123456789ABCDEF";
        Random r = new Random();
        // /A/A
        return "/" + s.charAt(r.nextInt(16)) + "/" + s.charAt(r.nextInt(16));
    }
}
