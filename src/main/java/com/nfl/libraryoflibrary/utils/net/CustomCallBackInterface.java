package com.nfl.libraryoflibrary.utils.net;

/**
 * Created by fuli.niu on 2016/7/8.
 */
public interface CustomCallBackInterface<T> {

     void failure() ;
     void failureOnMainThread() ;
     void success(String result) ;
     void successOnMainThread(String result) ;
     void runOnMainThread(T t) ;
     void runOnSelfThread(T t) ;
     void finallyOnMainThread() ;// 不管成功或失败只要相应就调用该方法
     void cancel() ;
}
