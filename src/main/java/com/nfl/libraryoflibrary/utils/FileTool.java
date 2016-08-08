package com.nfl.libraryoflibrary.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by fuli.niu on 2016/8/5.
 */
public class FileTool {
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
}
