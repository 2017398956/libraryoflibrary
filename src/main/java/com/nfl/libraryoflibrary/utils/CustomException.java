package com.nfl.libraryoflibrary.utils;

/**
 * Created by fuli.niu on 2017/1/19.
 * 自定义异常
 */

public class CustomException extends Exception {

    public CustomException(String message) {
        super(message);
    }

    public CustomException() {
        super();
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
