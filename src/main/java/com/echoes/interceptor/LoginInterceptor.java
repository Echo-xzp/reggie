package com.echoes.interceptor;

import com.alibaba.fastjson.JSON;
import com.echoes.common.R;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @Author : Xiaozp
 * @ClassName : LoginInterceptor
 * @Description : TODO
 * @create : 2022/7/25 13:02
 * @Version : v1.0
 * @Powered By Corner Lab
 */

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


    /**
     * @Name : preHandle
     * @description : 登录拦截器，根据登录状态确定拦截状态，被拦截返回未登录码给前端
     * @createTime : 2022/7/25 13:20
     * @param : request
     * @param : response
     * @param : handler
     * @return : boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String requestURI = request.getRequestURI();
//        log.info("本次拦截到"+requestURI);

        // 检查浏览器缓存，确定登录状态
        Object employee = request.getSession().getAttribute("employee");
        Object user = request.getSession().getAttribute("user");
//        log.info(employee);
        if (employee == null && user == null) {
//            log.info("未登录");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(R.error("NOTLOGIN")));   // 返回未登录JSON数据给前端
            writer.flush();
            writer.close();
            return false;   // 拦截请求
        }
        return  true;

    }
}
