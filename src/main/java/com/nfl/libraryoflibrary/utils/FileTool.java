package com.nfl.libraryoflibrary.utils;

import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fuli.niu on 2016/8/5.
 */
public class FileTool {

    private static String SDPATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/";

    /**
     * 判断SD卡状态
     *
     * @return
     */
    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 返回SD卡的剩余空间
     *
     * @return
     */
    public static long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }

    /**
     * 创建目录
     */
    public static boolean makeDirs(File file) {
        if (null == file) {
            LogTool.i("FileTool.makeDirs 参数不能为空");
        } else {
            if (!file.exists()) {
                return file.mkdirs();
            }
        }
        return false;
    }

    /**
     * 创建目录
     */
    public static boolean makeDirs(String dirPath) {
        File dir = null;
        if (TextUtils.isEmpty(dirPath)) {
            LogTool.i("FileTool.makeDirs 参数不能为空");
        } else {
            dir = new File(dirPath);
            if (!dir.exists()) {
                return dir.mkdirs();
            }
        }
        return false;
    }

    /**
     * 判断文件是否是图片
     */
    public static boolean isPicture(File file) {
        if (null == file) {
            return false;
        }

        // 这里应该判断文件的大小为0时返回 false ;

        String lowerName = file.getName().toLowerCase();
        if (lowerName.endsWith(".jpg") ||
                lowerName.endsWith(".jpeg") ||
                lowerName.endsWith(".png") ||
                lowerName.endsWith(".bmp")
                ) {
            return true;
        }
        return false;
    }

    /**
     * 创建新文件
     *
     * @param filePath 文件完整路径
     * @return
     */
    public static File createFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        } else {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                makeDirs(file.getParentFile());
            }
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogTool.i(ExceptionTool.getExceptionTraceString(e));
                }
            }
            return file;
        }
    }

    /**
     * 创建临时新文件，无扩展名
     *
     * @param dirPath
     * @return
     */
    public static File createTempFile(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return null;
        } else {
            makeDirs(dirPath);
            File file = new File(dirPath + File.separator + DateTool.getTimeStamp());
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogTool.i(ExceptionTool.getExceptionTraceString(e));
                }
            }
            return file;
        }
    }

    /**
     * 创建临时新文件
     *
     * @param dirPath
     * @param expandedName 扩展名，不需要加 “.”
     * @return
     */
    public static File createTempFile(String dirPath, String expandedName) {
        if (TextUtils.isEmpty(dirPath)) {
            return null;
        } else {
            expandedName = TextUtils.isEmpty(expandedName) ? "" : "." + expandedName;
            makeDirs(dirPath);
            File file = new File(dirPath + File.separator + DateTool.getTimeStamp() + expandedName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogTool.i(ExceptionTool.getExceptionTraceString(e));
                }
            }
            return file;
        }
    }

    public static String fileToBase64(File file) {
        if (null == file) {
            return "";
        } else {
            FileInputStream fis = null;
            byte[] buffer = null;
            try {
                fis = new FileInputStream(file);
                buffer = new byte[(int) file.length()];
                fis.read(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != fis) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        LogTool.i(ExceptionTool.getExceptionTraceString(e));
                    }
                }
            }
            return null == buffer ? "" : Base64.encodeToString(buffer, Base64.NO_WRAP | Base64.NO_PADDING);
        }
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public static File creatSDFile(String saveFileName) throws IOException {
        File file = new File(SDPATH + saveFileName);
        file.createNewFile();
        return file;
    }

    /**
     * 删除文件
     *
     * @param path2
     */
    public static void DeleteFileName(String path2) {
        String path = SDPATH + path2 + "/";
        File file = new File(path);
        if (file != null)
            deleteFile(file);
    }

    /**
     * 删除文件
     *
     * @param file
     */
    private static void deleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) { // 是文件
                file.delete();// 删除文件
                return;
            } else if (file.isDirectory()) { // 是目录
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();// 删除本身
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();// 删除本身
            }
        }
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     */
    public static File creatSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        return file.exists();
    }

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public static Uri getUri(String fileName) {
        File file = new File(SDPATH + fileName);
        return Uri.fromFile(file);
    }

    /**
     * 由于得到一个InputStream对象是所有文件处理前必须的操作，所以将这个操作封装成了一个方法
     *
     * @param urlStr
     * @return
     * @throws IOException
     */
    public static InputStream getInputStream(String urlStr) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            is = urlConn.getInputStream();
            int fileSize = urlConn.getContentLength();// 根据响应获取文件大小
            if (fileSize <= 0)
                throw new RuntimeException("无法获知文件大小 ");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return is;
    }
}
