package com.nfl.libraryoflibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.nfl.libraryoflibrary.constant.ApplicationContext;
import com.nfl.libraryoflibrary.constant.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import Decoder.BASE64Decoder;
import id.zelory.compressor.Compressor;

/**
 * Created by fuli.niu on 2017/3/23.
 */

public class ImageTools {

    /**
     * 将图片转换成 Base64
     *
     * @return
     */
    public static String bitmap2Base64(Bitmap bitmap) {
        if (null == bitmap) {
            return "";
        }
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
//        byte[] bytes = bos.toByteArray();
//        try {
//            bos.flush();
//            bos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return Base64.encodeToString(bytes, Base64.NO_WRAP);
        return FileTool.fileToBase64(ImageTools.compressToFile(bitmap, Constants.CRM_CAMERA, null));
//        return test(bitmap) ;

    }

    private static String test(Bitmap bitmap) {
        File fileTemp = compressToFile(bitmap, Constants.CRM_CAMERA, null);
        byte[] bytes = new byte[(int) fileTemp.length()];
        try {
            FileOutputStream fos = new FileOutputStream(fileTemp);
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            LogTool.i(ExceptionTool.getExceptionTraceString(e));
        }
        StringBuffer sb = new StringBuffer();
        Byte bTemp;
        for (byte b : bytes) {
            bTemp = b;
            sb.append(bTemp.toString());
            sb.append("|");
        }
        LogTool.i("转换成字符串后长度：" + sb.toString().length());
        return sb.toString();
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


    /**
     * 图片压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compress(Bitmap image) {
        if (ApplicationContext.applicationContext == null || image == null) {
            return image;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        try {
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            LogTool.i(ExceptionTool.getExceptionTraceString(e));
        }
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "BillOA" + File.separator + "crm";
        FileTool.makeDirs(filePath);
        File fileTemp = FileTool.createTempFile(filePath);

        if (null == fileTemp || !fileTemp.exists()) {
            LogTool.i("无法创建临时新文件 " + filePath);
            return null;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileTemp);
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            LogTool.i(ExceptionTool.getExceptionTraceString(e));
        } finally {
            if (null != fos) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogTool.i(ExceptionTool.getExceptionTraceString(e));
                }
            }
        }
        Compressor compressor = Compressor.getDefault(ApplicationContext.applicationContext);
        if (null == compressor) {
            LogTool.i("图片压缩工具初始化失败");
            return null;
        } else {
            return compressor.compressToBitmap(fileTemp);
        }
    }

    /**
     * 图片压缩
     *
     * @param image
     * @param fileDir  文件目录
     * @param fileName 当 fileName 为 null 时，会根据时间戳自动生成文件名，一般传 null 即可；
     * @return
     */
    public static File compressToFile(Bitmap image, String fileDir, String fileName) {
        if (ApplicationContext.applicationContext == null || image == null || TextUtils.isEmpty(fileDir)) {
            return null;
        }
        File fileTemp;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        try {
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            LogTool.i(ExceptionTool.getExceptionTraceString(e));
        }

        FileTool.makeDirs(fileDir);

        if (!fileDir.endsWith("/") || !fileDir.endsWith(File.separator)) {
            fileDir += File.separator;
        }

        if (TextUtils.isEmpty(fileName)) {
            fileTemp = FileTool.createTempFile(fileDir);
        } else {
            fileTemp = FileTool.createFile(fileDir + fileName);
        }

        if (null == fileTemp || !fileTemp.exists()) {
            LogTool.i("无法创建临时新文件");
            return null;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileTemp);
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            LogTool.i(ExceptionTool.getExceptionTraceString(e));
        } finally {
            if (null != fos) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogTool.i(ExceptionTool.getExceptionTraceString(e));
                }
            }
        }
        Compressor compressor = Compressor.getDefault(ApplicationContext.applicationContext);
        if (null == compressor) {
            LogTool.i("图片压缩工具初始化失败");
            return null;
        } else {
            LogTool.i("被压缩的文件：" + fileTemp.getName());
            return compressor.compressToFile(fileTemp);
        }
    }

    /**
     * 图片压缩
     *
     * @return
     */
    public static Bitmap compress(Context ctx, String imagePath) {
        if (ctx == null || imagePath == null) {
            return null;
        }

        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
            opts.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, opts);
            int w = opts.outWidth;
            int h = opts.outHeight;

            // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
            DisplayMetrics displayMetrics = PhoneInfoTool.getMetrics();
            float ww = displayMetrics.widthPixels;// 这里设置宽度为480f
            float hh = displayMetrics.heightPixels;// 这里设置高度为800f;

            // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int sampleSize = 1;// be=1表示不缩放
            if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
                sampleSize = (int) (opts.outWidth / ww);
            } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
                sampleSize = (int) (opts.outHeight / hh);
            }
            if (sampleSize <= 0)
                sampleSize = 1;

            // 设置缩放比例
            opts.inSampleSize = sampleSize;
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(imagePath, opts);
            return bitmap;// 压缩好比例大小后再进行质量压缩
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * 生成圆角图片，此方法采用bitmap的宽作为直径，生成原型图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getWidth());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth()));
            final float roundPx = bitmap.getWidth();
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Throwable t) {
            return bitmap;
        }
    }

    /**
     * 生成圆角图片，此方法采用bitmap的宽和高的对角线生成原型图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            final float roundPx = bitmap.getWidth();
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Throwable t) {
            return bitmap;
        }
    }


    public static Bitmap cropBitmap(Bitmap loadedImage) {
        Bitmap cropedBitmap = null;
        if (loadedImage != null) {
            int width = loadedImage.getWidth();
            int height = loadedImage.getHeight();

            if (width < height) {
                cropedBitmap = Bitmap.createBitmap(loadedImage, 0, 0, width,
                        width);
            } else {
                cropedBitmap = loadedImage;
            }
        }
        return cropedBitmap;
    }

    /**
     * @param drawable drawable ת Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // ȡ drawable �ĳ���
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // ȡ drawable ����ɫ��ʽ
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // ������Ӧ bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // ������Ӧ bitmap �Ļ���
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // �� drawable ���ݻ���������
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * @param bitmap
     * @param roundPx ��ȡԲ��ͼƬ
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * ����ͼƬ
     *
     * @param bitmap
     * @return
     */
    public static Bitmap zoom(Bitmap bitmap, float zf) {
        Matrix matrix = new Matrix();
        matrix.postScale(zf, zf);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    /**
     * ����ͼƬ
     *
     * @param bitmap
     * @return
     */
    public static Bitmap zoom(Bitmap bitmap, float wf, float hf) {
        Matrix matrix = new Matrix();
        matrix.postScale(wf, hf);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    /**
     * ͼƬԲ�Ǵ���
     *
     * @param bitmap
     * @param roundPX
     * @return
     */
    public static Bitmap getRCB(Bitmap bitmap, float roundPX) {
        // RCB means
        // Rounded
        // Corner Bitmap
        Bitmap dstbmp = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dstbmp);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return dstbmp;
    }
}
