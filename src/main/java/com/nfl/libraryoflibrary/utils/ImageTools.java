package com.nfl.libraryoflibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import Decoder.BASE64Decoder;

/**
 * Created by fuli.niu on 2017/3/23.
 */

public class ImageTools {

    /**
     * 将图片转换成 Base64
     *
     * @param bit
     * @return
     */
    public static String bitmap2Base64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        try {
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    /**
     * @param base64Str base64编码字符串
     * @param path   图片路径-具体到文件
     * @return ture 转换成功
     * @Description: 将base64编码字符串转换为图片
     */
    public static boolean generateImage(String base64Str , String path) {
        if (TextUtils.isEmpty(base64Str)){
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(base64Str);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            LogTool.i("Base64 转文件失败") ;
            LogTool.i(ExceptionTool.getExceptionTraceString(e)) ;
            return false;
        }
    }

    public static Bitmap generateImage(String base64Str) {
        if (TextUtils.isEmpty(base64Str)) {
            LogTool.i("图片的 Base64 为 null") ;
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(base64Str);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } catch (Exception e) {
            LogTool.i("Base64 转文件失败");
            LogTool.i(ExceptionTool.getExceptionTraceString(e));
            return null;
        }
    }

    /**
     * @param base64Str
     * @param imageFile
     * @return
     */
    public static boolean generateImage(String base64Str , File imageFile) {
        return generateImage(base64Str , imageFile.getAbsolutePath()) ;
    }
}
