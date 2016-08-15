package com.nfl.libraryoflibrary.utils.net;

/**
 * Created by fuli.niu on 2016/7/8.
 */
public interface CustomCallBackInterface2<T> {

     void failure() ;
     void success(T result) ;
}
