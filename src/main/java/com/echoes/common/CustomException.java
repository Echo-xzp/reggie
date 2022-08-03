package com.echoes.common;

/**
 * @Author : Xiaozp
 * @ClassName : CustoomException
 * @Description : 自定义业务异常
 * @create : 2022/7/27 13:56
 * @Version : v1.0
 * @Powered By Corner Lab
 */
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}
