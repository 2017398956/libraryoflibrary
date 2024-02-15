package com.nfl.libraryoflibrary.utils

import Decoder.BASE64Decoder
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Environment
import android.text.TextUtils
import com.nfl.libraryoflibrary.constant.ApplicationContext
import com.nfl.libraryoflibrary.constant.Constants
import okhttp3.internal.and
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.ref.SoftReference

/**
 * Created by fuli.niu on 2017/3/23.
 */
object ImageTools {
    /**
     * 将图片转换成 Base64
     *
     * @return
     */
    fun bitmap2Base64(bitmap: Bitmap?): String {
        return if (null == bitmap) {
            ""
        } else FileTool.fileToBase64(
            compressToFile(
                bitmap,
                Constants.CRM_CAMERA,
                null
            )
        )
    }

    private fun test(bitmap: Bitmap): String {
        val fileTemp = compressToFile(bitmap, Constants.CRM_CAMERA, null)
        val bytes = ByteArray(fileTemp!!.length().toInt())
        try {
            val fos = FileOutputStream(fileTemp)
            fos.write(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
            LogTool.i(ExceptionTool.getExceptionTraceString(e))
        }
        val sb = StringBuffer()
        var bTemp: Byte
        for (b in bytes) {
            bTemp = b
            sb.append(bTemp.toString())
            sb.append("|")
        }
        LogTool.i("转换成字符串后长度：" + sb.toString().length)
        return sb.toString()
    }

    /**
     * @param base64Str base64编码字符串
     * @param path   图片路径-具体到文件
     * @return ture 转换成功
     * @Description: 将base64编码字符串转换为图片
     */
    fun generateImage(base64Str: String?, path: String?): Boolean {
        if (TextUtils.isEmpty(base64Str)) {
            return false
        }
        val decoder = BASE64Decoder()
        return try {
            // 解密
            val b = decoder.decodeBuffer(base64Str)
            // 处理数据
            for (i in b.indices) {
                if (b[i] < 0) {
                    (b[i] and 256).toByte()
                }
            }
            val out: OutputStream = FileOutputStream(path)
            out.write(b)
            out.flush()
            out.close()
            true
        } catch (e: Exception) {
            LogTool.i("Base64 转文件失败")
            LogTool.i(ExceptionTool.getExceptionTraceString(e))
            false
        }
    }

    fun generateImage(base64Str: String?): Bitmap? {
        if (TextUtils.isEmpty(base64Str)) {
            LogTool.i("图片的 Base64 为 null")
            return null
        }
        val decoder = BASE64Decoder()
        return try {
            // 解密
            var b = decoder.decodeBuffer(base64Str)
            // 处理数据
            for (i in b!!.indices) {
                if (b[i] < 0) {
                    (b[i] and 256).toByte()
                }
            }
            var input: InputStream? = null
            var bitmap: Bitmap? = null
            val options = BitmapFactory.Options()
            options.inSampleSize = 16
            input = ByteArrayInputStream(b)
            val softRef: SoftReference<*> =
                SoftReference<Any?>(BitmapFactory.decodeStream(input, null, null))
            bitmap = softRef.get() as Bitmap?
            if (b != null) {
                b = null
            }
            if (input != null) {
                input.close()
            }
            bitmap
            // return BitmapFactory.decodeByteArray(b, 0, b.length);
        } catch (e: Exception) {
            LogTool.i("Base64 转文件失败")
            LogTool.i(ExceptionTool.getExceptionTraceString(e))
            null
        }
    }

    /**
     * @param base64Str
     * @param imageFile
     * @return
     */
    fun generateImage(base64Str: String?, imageFile: File): Boolean {
        return generateImage(base64Str, imageFile.absolutePath)
    }

    /**
     * 图片压缩
     *
     * @param image
     * @return
     */
    fun compress(image: Bitmap?): Bitmap? {
        if (ApplicationContext.applicationContext == null || image == null) {
            return image
        }
        val bos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos) //参数100表示不压缩
        val bytes = bos.toByteArray()
        try {
            bos.flush()
            bos.close()
        } catch (e: Exception) {
            e.printStackTrace()
            LogTool.i(ExceptionTool.getExceptionTraceString(e))
        }
        val filePath = (Environment.getExternalStorageDirectory().absolutePath
                + File.separator + "BillOA" + File.separator + "crm")
        FileTool.makeDirs(filePath)
        val fileTemp = FileTool.createTempFile(filePath)
        if (null == fileTemp || !fileTemp.exists()) {
            LogTool.i("无法创建临时新文件 $filePath")
            return null
        }
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(fileTemp)
            fos.write(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
            LogTool.i(ExceptionTool.getExceptionTraceString(e))
        } finally {
            if (null != fos) {
                try {
                    fos.flush()
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    LogTool.i(ExceptionTool.getExceptionTraceString(e))
                }
            }
        }

//        val compressor: Compressor = getDefault(ApplicationContext.applicationContext)
//        return if (null == compressor) {
//            LogTool.i("图片压缩工具初始化失败")
//            null
//        } else {
//            compressor.compressToBitmap(fileTemp)
//        }
        return null
    }

    /**
     * 图片压缩
     *
     * @param image
     * @param fileDir  文件目录
     * @param fileName 当 fileName 为 null 时，会根据时间戳自动生成文件名，一般传 null 即可；
     * @return
     */
    fun compressToFile(image: Bitmap?, fileDir: String, fileName: String?): File? {
        var fileDir = fileDir
        if (ApplicationContext.applicationContext == null || image == null || TextUtils.isEmpty(
                fileDir
            )
        ) {
            return null
        }
        val fileTemp: File?
        val bos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos) //参数100表示不压缩
        val bytes = bos.toByteArray()
        try {
            bos.flush()
            bos.close()
        } catch (e: Exception) {
            e.printStackTrace()
            LogTool.i(ExceptionTool.getExceptionTraceString(e))
        }
        FileTool.makeDirs(fileDir)
        if (!fileDir.endsWith("/") || !fileDir.endsWith(File.separator)) {
            fileDir += File.separator
        }
        fileTemp = if (TextUtils.isEmpty(fileName)) {
            FileTool.createTempFile(fileDir)
        } else {
            FileTool.createFile(fileDir + fileName)
        }
        if (null == fileTemp || !fileTemp.exists()) {
            LogTool.i("无法创建临时新文件")
            return null
        }
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(fileTemp)
            fos.write(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
            LogTool.i(ExceptionTool.getExceptionTraceString(e))
        } finally {
            if (null != fos) {
                try {
                    fos.flush()
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    LogTool.i(ExceptionTool.getExceptionTraceString(e))
                }
            }
        }
//        val compressor: Compressor = getDefault(ApplicationContext.applicationContext)
//        return if (null == compressor) {
//            LogTool.i("图片压缩工具初始化失败")
//            null
//        } else {
//            LogTool.i("被压缩的文件：" + fileTemp.name)
//            compressor.compressToFile(fileTemp)
//        }
        return null
    }

    /**
     * 图片压缩
     *
     * @return
     */
    fun compress(ctx: Context?, imagePath: String?): Bitmap? {
        return if (ctx == null || imagePath == null) {
            null
        } else try {
            val opts = BitmapFactory.Options()
            // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
            opts.inJustDecodeBounds = true
            var bitmap = BitmapFactory.decodeFile(imagePath, opts)
            val w = opts.outWidth
            val h = opts.outHeight

            // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
            val displayMetrics = PhoneInfoTool.getMetrics()
            val ww = displayMetrics.widthPixels.toFloat() // 这里设置宽度为480f
            val hh = displayMetrics.heightPixels.toFloat() // 这里设置高度为800f;

            // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            var sampleSize = 1 // be=1表示不缩放
            if (w > h && w > ww) { // 如果宽度大的话根据宽度固定大小缩放
                sampleSize = (opts.outWidth / ww).toInt()
            } else if (w < h && h > hh) { // 如果高度高的话根据宽度固定大小缩放
                sampleSize = (opts.outHeight / hh).toInt()
            }
            if (sampleSize <= 0) sampleSize = 1

            // 设置缩放比例
            opts.inSampleSize = sampleSize
            opts.inJustDecodeBounds = false
            bitmap = BitmapFactory.decodeFile(imagePath, opts)
            bitmap // 压缩好比例大小后再进行质量压缩
        } catch (t: Throwable) {
            null
        }
    }

    /**
     * 生成圆角图片，此方法采用bitmap的宽作为直径，生成原型图片
     *
     * @param bitmap
     * @return
     */
    fun getRoundedCornerBitmap(bitmap: Bitmap): Bitmap {
        return try {
            val output = Bitmap.createBitmap(bitmap.width, bitmap.width, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val paint = Paint()
            val rect = Rect(
                0, 0, bitmap.width,
                bitmap.width
            )
            val rectF = RectF(Rect(0, 0, bitmap.width, bitmap.width))
            val roundPx = bitmap.width.toFloat()
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = Color.BLACK
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
            paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
            val src = Rect(0, 0, bitmap.width, bitmap.width)
            canvas.drawBitmap(bitmap, src, rect, paint)
            output
        } catch (t: Throwable) {
            bitmap
        }
    }

    /**
     * 生成圆角图片，此方法采用bitmap的宽和高的对角线生成原型图片
     *
     * @param bitmap
     * @return
     */
    fun getCircleBitmap(bitmap: Bitmap): Bitmap {
        return try {
            val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val paint = Paint()
            val rect = Rect(
                0, 0, bitmap.width,
                bitmap.height
            )
            val rectF = RectF(
                Rect(
                    0, 0, bitmap.width,
                    bitmap.height
                )
            )
            val roundPx = bitmap.width.toFloat()
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = Color.BLACK
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
            paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
            val src = Rect(
                0, 0, bitmap.width,
                bitmap.height
            )
            canvas.drawBitmap(bitmap, src, rect, paint)
            output
        } catch (t: Throwable) {
            bitmap
        }
    }

    fun cropBitmap(loadedImage: Bitmap?): Bitmap? {
        var cropedBitmap: Bitmap? = null
        if (loadedImage != null) {
            val width = loadedImage.width
            val height = loadedImage.height
            cropedBitmap = if (width < height) {
                Bitmap.createBitmap(
                    loadedImage, 0, 0, width,
                    width
                )
            } else {
                loadedImage
            }
        }
        return cropedBitmap
    }

    /**
     * @param drawable drawable ת Bitmap
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        // ȡ drawable �ĳ���
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight

        // ȡ drawable ����ɫ��ʽ
        val config =
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        // ������Ӧ bitmap
        val bitmap = Bitmap.createBitmap(w, h, config)
        // ������Ӧ bitmap �Ļ���
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        // �� drawable ���ݻ���������
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * @param bitmap
     * @param roundPx ��ȡԲ��ͼƬ
     */
    fun getRoundedCornerBitmap(bitmap: Bitmap, roundPx: Float): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, w, h)
        val rectF = RectF(rect)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    /**
     * ����ͼƬ
     *
     * @param bitmap
     * @return
     */
    fun zoom(bitmap: Bitmap, zf: Float): Bitmap {
        val matrix = Matrix()
        matrix.postScale(zf, zf)
        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width,
            bitmap.height, matrix, true
        )
    }

    /**
     * ����ͼƬ
     *
     * @param bitmap
     * @return
     */
    fun zoom(bitmap: Bitmap, wf: Float, hf: Float): Bitmap {
        val matrix = Matrix()
        matrix.postScale(wf, hf)
        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width,
            bitmap.height, matrix, true
        )
    }

    /**
     * ͼƬԲ�Ǵ���
     *
     * @param bitmap
     * @param roundPX
     * @return
     */
    fun getRCB(bitmap: Bitmap, roundPX: Float): Bitmap {
        // RCB means
        // Rounded
        // Corner Bitmap
        val dstbmp = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(dstbmp)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPX, roundPX, paint)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return dstbmp
    }
}
