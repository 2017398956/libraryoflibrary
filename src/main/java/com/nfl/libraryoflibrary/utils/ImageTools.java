package com.nfl.libraryoflibrary.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import Decoder.BASE64Decoder;

/**
 * Created by fuli.niu on 2017/3/23.
 */

public class ImageTools {
    /**
     * @param base64Str base64编码字符串
     * @param path   图片路径-具体到文件
     * @Description: 将base64编码字符串转换为图片
     * @return ture 转换成功
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

    /**
     * @param base64Str
     * @param imageFile
     * @return
     */
    public static boolean generateImage(String base64Str , File imageFile) {
        return generateImage(base64Str , imageFile.getAbsolutePath()) ;
    }
}
