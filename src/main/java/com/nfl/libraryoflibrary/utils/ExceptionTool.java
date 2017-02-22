package com.nfl.libraryoflibrary.utils;

/**
 * Created by fuli.niu on 2017/1/3.
 */

public class ExceptionTool {

    public static String getExceptionTraceString(Exception e) {
        if (null == e) {
            return "ExceptionTool.getExceptionTraceString's parameter is null";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(e.toString());
        sb.append("\n");
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        if (null != stackTraceElements && stackTraceElements.length > 0) {
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                if (null != stackTraceElement) {
                    sb.append("\t");
                    sb.append(stackTraceElement.toString());
                    sb.append("\n") ;
                }
            }
        }
        return sb.toString();
    }

    public static String getExceptionTraceString(Exception e , boolean showInLogcat) {

        if(showInLogcat){
            if (null != e) {
                e.printStackTrace();
            }
        }
        return getExceptionTraceString(e) ;
    }
}
