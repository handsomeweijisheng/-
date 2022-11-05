package com.wjs.takeout.common;

/**
 * @author wjs
 * @createTime 2022-11-06 0:29
 */
//业务异常
public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
