package com.nfl.libraryoflibrary.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.nfl.libraryoflibrary.constant.ApplicationContext;

import java.util.List;

/**
 * Created by fuli.niu on 2017/8/7.
 */

public class TelephonyInfoTool {

    private static final String TAG = "TelephonyInfoTool";

    /**
     * 功能描述：通过手机信号获取基站信息
     * # 通过TelephonyManager 获取lac:mcc:mnc:cell-id
     * # MCC，Mobile Country Code，移动国家代码（中国的为460）；
     * # MNC，Mobile Network Code，移动网络号码（中国移动为0，中国联通为1，中国电信为2）；
     * # LAC，Location Area Code，位置区域码；
     * # CID，Cell Identity，基站编号；
     * # BSSS，Base station signal strength，基站信号强度。
     *
     * @author android_ls
     */
    public static void get() {
        TelephonyManager mTelephonyManager = (TelephonyManager) ApplicationContext.applicationContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        // 返回值MCC + MNC
        String operator = mTelephonyManager.getNetworkOperator();
        int mcc = Integer.parseInt(operator.substring(0, 3));
        int mnc = Integer.parseInt(operator.substring(3));

        // 中国移动和中国联通获取LAC、CID的方式
        GsmCellLocation location = (GsmCellLocation) mTelephonyManager.getCellLocation();
        int lac = location.getLac();
        int cellId = location.getCid();

        Log.i(TAG, " MCC = " + mcc + "\t MNC = " + mnc + "\t LAC = " + lac + "\t CID = " + cellId);

        // 中国电信获取LAC、CID的方式
                /*CdmaCellLocation location1 = (CdmaCellLocation) mTelephonyManager.getCellLocation();
                lac = location1.getNetworkId();
                cellId = location1.getBaseStationId();
                cellId /= 16;*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            List<CellInfo> infos = mTelephonyManager.getAllCellInfo();
            StringBuffer sb = new StringBuffer("总数 : " + infos.size() + "\n");
            for (CellInfo info1 : infos) { // 根据邻区总数进行循环
                sb.append(" info : " + info1 + "\n"); // 获取邻区基站信号强度
            }
            Log.i(TAG, " 获取邻区基站信息:" + sb);
        }
//        // 获取邻区基站信息
//        List<NeighboringCellInfo> infos = mTelephonyManager.getNeighboringCellInfo();
//
//        StringBuffer sb = new StringBuffer("总数 : " + infos.size() + "\n");
//        for (NeighboringCellInfo info1 : infos) { // 根据邻区总数进行循环
//            sb.append(" LAC : " + info1.getLac()); // 取出当前邻区的LAC
//            sb.append(" CID : " + info1.getCid()); // 取出当前邻区的CID
//            sb.append(" BSSS : " + (-113 + 2 * info1.getRssi()) + "\n"); // 获取邻区基站信号强度
//        }
//        Log.i(TAG, " 获取邻区基站信息:" + sb.toString());
    }
}
