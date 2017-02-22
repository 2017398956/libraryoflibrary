package com.nfl.libraryoflibrary.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fuli.niu on 2016/12/12.
 */

public class MD5Tool {

    private static MessageDigest sMd5MessageDigest;
    private static StringBuilder sStringBuilder;

    static {
        try {
            sMd5MessageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO Cyril: I'm quite sure about my "MD5" algorithm
            // but this is not a correct way to handle an exception ...
        }
        sStringBuilder = new StringBuilder();
    }

    /**
     * @param file
     * @return 单个文件的MD5值
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                sMd5MessageDigest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, sMd5MessageDigest.digest());
        return bigInt.toString(16);
    }

    /**
     * @param dir
     * @param listChild ;true递归子目录中的文件
     * @return 文件夹中文件的MD5值
     */
    public static Map<String, String> getDirMD5(File dir, boolean listChild) {
        if (!dir.isDirectory()) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        String md5;
        File files[] = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory() && listChild) {
                map.putAll(getDirMD5(f, listChild));
            } else {
                md5 = getFileMD5(f);
                if (md5 != null) {
                    map.put(f.getPath(), md5);
                }
            }
        }
        return map;
    }

    /**
     * 返回32位的MD5字符串
     */
    public static String md5(String s) {
        sMd5MessageDigest.reset();
        sMd5MessageDigest.update(s.getBytes());

        byte digest[] = sMd5MessageDigest.digest();

        sStringBuilder.setLength(0);
        for (int i = 0; i < digest.length; i++) {
            final int b = digest[i] & 255;
            if (b < 16) {
                sStringBuilder.append('0');
            }
            sStringBuilder.append(Integer.toHexString(b));
        }
        return sStringBuilder.toString();
    }

    /**
     * 返回16位的MD5字符串
     */
    public static String get16MD5(String originString) {
        if (originString != null) {
            try {
                // 创建具有指定算法名称的信息摘要
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md5.digest(originString.getBytes("UTF-8"));
                return new String(results);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String mD5Last8(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().substring(16, 24);
            //System.out.println("MD5(" + sourceStr + ",32) = " + result);
            //System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
        } catch (NoSuchAlgorithmException e) {
            //System.out.println(e);
        }
        return result;
    }
}
