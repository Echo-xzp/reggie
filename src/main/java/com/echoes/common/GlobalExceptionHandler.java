package com.echoes.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author : Xiaozp
 * @ClassName : GlobalExceptionHandler
 * @Description : 全局异常处理器
 * @create : 2022/7/26 13:41
 * @Version : v1.0
 * @Powered By Corner Lab
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler{

    /**
     * @Name : sqlExceptionHandle
     * @description : 拦截并处理处理SQLIntegrityConstraintViolationException/SQL唯一约束报错
     * @createTime : 2022/7/26 13:48
     * @param : exception
     * @return : com.echoes.common.R<java.lang.String>
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> sqlExceptionHandle(SQLIntegrityConstraintViolationException exception){
        String message = exception.getMessage();
        log.info(message);

        if(message.contains("Duplicate")){
            String[] split = message.split(" ");
            return R.error(split[2]+"已存在！");       // 返回已存在用户名错误信息给前端
        }

        return R.error("未知错误！");
    }

    /**
     * @Name : customExceptionHandle
     * @description : 拦截并处理处理CustomException
     * @createTime : 2022/7/27 14:02
     * @param : exception
     * @return : com.echoes.common.R<java.lang.String>
     */
    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandle(CustomException exception){
        String message = exception.getMessage();
        log.info(message);

        return R.error(message);
    }

    /**
     * @Name : messagingExceptionHandle
     * @description : 拦截并处理邮件发送失败的异常
     * @createTime : 2022/7/30 14:20
     * @param : exception
     * @return : com.echoes.common.R<java.lang.String>
     */
    @ExceptionHandler(MessagingException.class)
    public R<String> messagingExceptionHandle(MessagingException exception){
        String message = exception.getMessage();
        log.info(message);

        return R.error("发送邮件验证码失败!");
    }

}
