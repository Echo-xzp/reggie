package com.echoes.common;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @Author : Xiaozp
 * @ClassName : CurrentEmpId
 * @Description : 利用RequestContextHolder获取当前会话中存取的属性
 * @create : 2022/7/26 17:39
 * @Version : v1.0
 * @Powered By Corner Lab
 */
public class BaseContext {

    static private String name = new String();
    static public Long getCurrentId(){
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        Long empId = (Long)attributes.getAttribute(name,RequestAttributes.SCOPE_SESSION);
        return empId;
    }

    static public void setName(String s){
        name = s;
    }
}
