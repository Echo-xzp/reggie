package com.echoes.common;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @Author : Xiaozp
 * @ClassName : CurrentEmpId
 * @Description : 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 * @create : 2022/7/26 17:39
 * @Version : v1.0
 * @Powered By Corner Lab
 */

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
