package com.echoes.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author : Xiaozp
 * @ClassName : RedirectController
 * @Description : 控制网站重定向
 * @create : 2022/8/1 14:56
 * @Version : v1.0
 * @Powered By Corner Lab
 */
@Controller
@Slf4j
public class RedirectController {

    /**
     * @Name : redirectHome
     * @description : 根据访问区分电脑端与手机端，并将其重定向到相应登录首页
     * @createTime : 2022/8/1 14:59
     * @return : void
     */
    @RequestMapping("/")
    @GetMapping
    public void redirectHome(HttpServletRequest request, HttpServletResponse response){

        String userAgent = request.getHeader("user-agent");
        log.info(userAgent);
        try {
            if (userAgent.contains("Windows")) {
                response.sendRedirect("/backend/page/login/login.html");
            }else {
                response.sendRedirect("/front/page/login.html");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return;
    }

}
