package com.echoes.interceptor;

import com.alibaba.fastjson.JSON;
import com.echoes.common.R;
import com.echoes.entity.Employee;
import com.echoes.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @Author : Xiaozp
 * @ClassName : PowerInterceptor
 * @Description : TODO
 * @create : 2022/7/26 16:59
 * @Version : v1.0
 * @Powered By Corner Lab
 */
@Component
public class PowerInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long empId= (Long)request.getSession().getAttribute("employee");
        if( empId != 1) {       // 超管ID为1
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(R.error("非超级管理员admin无法修改信息！")));   // 返回未登录JSON数据给前端
            writer.flush();
            writer.close();
            return false;
        }
        return true;
    }

}
