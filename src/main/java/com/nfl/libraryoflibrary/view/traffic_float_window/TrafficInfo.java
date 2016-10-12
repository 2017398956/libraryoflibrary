package com.nfl.libraryoflibrary.view.traffic_float_window;

import android.net.TrafficStats;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;

/**
 * 应用的流量信息
 */
public class TrafficInfo {

    private static final int UNSUPPORTED = -1;

    private static TrafficInfo instance;

    static int uid;
    private static long preRxBytes = 0;

    /**
     * 更新频率（每几秒更新一次,至少1秒）
     */
    private final int UPDATE_FREQUENCY = 1;
    private int times = 1;

    public TrafficInfo(int uid) {
        this.uid = uid;
    }

    private TrafficInfo() {
    }

    /**
     * 获取总流量
     * @return
     */
    public long getTrafficInfo() {
        long rcvTraffic = UNSUPPORTED; // 下载流量
        long sndTraffic = UNSUPPORTED; // 上传流量
        rcvTraffic = getRcvTraffic();
        sndTraffic = getSndTraffic();
        if (rcvTraffic == UNSUPPORTED || sndTraffic == UNSUPPORTED)
            return UNSUPPORTED;
        else
            return rcvTraffic + sndTraffic;
    }

    /**
     * 获取下载流量
     * 某个应用的网络流量数据保存在系统的/proc/uid_stat/$UID/tcp_rcv | tcp_snd文件中
     *
     * @return
     */
    public long getRcvTraffic() {
        long rcvTraffic = UNSUPPORTED; // 下载流量
        rcvTraffic = TrafficStats.getUidRxBytes(uid);
        if (rcvTraffic == UNSUPPORTED) { // 不支持的查询
            return UNSUPPORTED;
        }

        RandomAccessFile rafRcv = null, rafSnd = null; // 用于访问数据记录文件
        String rcvPath = "/proc/uid_stat/" + uid + "/tcp_rcv";
        try {
            rafRcv = new RandomAccessFile(rcvPath, "r");
            rcvTraffic = Long.parseLong(rafRcv.readLine()); // 读取流量统计
        } catch (FileNotFoundException e) {
            rcvTraffic = UNSUPPORTED;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rafRcv != null)
                    rafRcv.close();
                if (rafSnd != null)
                    rafSnd.close();
            } catch (IOException e) {
            }
        }
        return rcvTraffic;
    }

    /**
     * 获取上传流量
     *
     * @return
     */
    public long getSndTraffic() {
        long sndTraffic = UNSUPPORTED; // 上传流量
        sndTraffic = TrafficStats.getUidTxBytes(uid);
        if (sndTraffic == UNSUPPORTED) { // 不支持的查询
            return UNSUPPORTED;
        }

        RandomAccessFile rafRcv = null, rafSnd = null; // 用于访问数据记录文件
        String sndPath = "/proc/uid_stat/" + uid + "/tcp_snd";
        try {
            rafSnd = new RandomAccessFile(sndPath, "r");
            sndTraffic = Long.parseLong(rafSnd.readLine());
        } catch (FileNotFoundException e) {
            sndTraffic = UNSUPPORTED;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rafRcv != null)
                    rafRcv.close();
                if (rafSnd != null)
                    rafSnd.close();
            } catch (IOException e) {
            }
        }
        return sndTraffic;
    }

    /**
     * 获取当前下载流量总和
     *
     * @return
     */
    public static long getNetworkRxBytes() {
        return TrafficStats.getTotalRxBytes();
    }

    /**
     * 获取当前上传流量总和
     *
     * @return
     */
    public static long getNetworkTxBytes() {
        return TrafficStats.getTotalTxBytes();
    }

    /**
     * 获取当前网速
     * @return
     */
    public static double getNetSpeed() {
        long curRxBytes = getNetworkRxBytes();
        if (preRxBytes == 0)
            preRxBytes = curRxBytes;
        long bytes = curRxBytes - preRxBytes;
        preRxBytes = curRxBytes;
        //int kb = (int) Math.floor(bytes / 1024 + 0.5);
        double kb = (double) bytes / (double) 1024;
        BigDecimal bd = new BigDecimal(kb);
        return bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取当前网速带单位
     * @return
     */
    public static String getNetSpeed2String() {
        long curRxBytes = getNetworkRxBytes();
        if (preRxBytes == 0)
            preRxBytes = curRxBytes;
        long bytes = curRxBytes - preRxBytes;
        preRxBytes = curRxBytes;
        String postfix = "b/s" ;
        double showTrafficNumber = 0d ;
        if(bytes < 1000){
            showTrafficNumber = (double) bytes ;
        }else if(bytes > 1000 && bytes < 1000 * 1000){
            showTrafficNumber = (double) bytes / 1024 ;
            postfix = "kb/s" ;
        }else if(bytes > 1000 * 1000){
            showTrafficNumber = (double) bytes / ( 1024 * 1024) ;
            postfix = "mb/s" ;
        }
        BigDecimal bd = new BigDecimal(showTrafficNumber);
        return bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + postfix ;
    }

    /**
     * 获取当前应用uid
     *
     * @return
     */
    public int getUid() {
//		try {
//			PackageManager pm = mContext.getPackageManager();
//			ApplicationInfo ai = pm.getApplicationInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
//			return ai.uid;
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
        return -1;
    }

    /**
     * static long getMobileRxBytes()//获取通过Mobile连接收到的字节总数，但不包含WiFi static long
     * getMobileRxPackets()//获取Mobile连接收到的数据包总数 static long
     * getMobileTxBytes()//Mobile发送的总字节数 static long
     * getMobileTxPackets()//Mobile发送的总数据包数 static long
     * getTotalRxBytes()//获取总的接受字节数，包含Mobile和WiFi等 static long
     * getTotalRxPackets()//总的接受数据包数，包含Mobile和WiFi等 static long
     * getTotalTxBytes()//总的发送字节数，包含Mobile和WiFi等 static long
     * getTotalTxPackets()//发送的总数据包数，包含Mobile和WiFi等 static long
     * getUidRxBytes(int uid)//获取某个网络UID的接受字节数 static long getUidTxBytes(int
     * uid) //获取某个网络UID的发送字节数
     */

}
