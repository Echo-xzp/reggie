package com.echoes.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.echoes.common.BaseContext;
import com.echoes.common.R;
import com.echoes.dto.UserDto;
import com.echoes.entity.User;
import com.echoes.service.UserService;
import com.echoes.utils.Email;
import com.echoes.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.security.sasl.SaslServer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-29
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @Name : login
     * @description : 用户登录
     * @createTime : 2022/8/1 16:08
     * @param : userDto
     * @param : session
     * @return : com.echoes.common.R<com.echoes.entity.User>
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody UserDto userDto, HttpSession session){
        log.info("User : {}",userDto);

        String code = (String)session.getAttribute("code");

        String userCode = userDto.getCode().toString();
        String md5UserCode = DigestUtils.md5DigestAsHex(userCode.getBytes(StandardCharsets.UTF_8));

        if (code.equals(md5UserCode)){
            String email = userDto.getEmail();
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getEmail,email);
            User user = userService.getOne(queryWrapper);
            Long id ;
            if(user == null){
                userService.save(userDto);
                id = userDto.getId();
            }else {
                if (!user.getPhone().equals(userDto.getPhone())){
                    return  R.error("手机号与邮箱不匹配!");
                }

                id = user.getId();
            }

            session.setAttribute("user",id);
            session.removeAttribute("code");
            BaseContext.setName("user");
            return R.success(userDto);
        }else {
            return R.error("验证码错误!");
        }

    }

    /**
     * @Name : logout
     * @description : 用户登出
     * @createTime : 2022/8/1 16:08
     * @param : session
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PostMapping("/logout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("user");

        return R.success("退出登录成功!");
    }

    /**
     * @Name : getCode
     * @description : 用户获取邮箱验证码
     * @createTime : 2022/8/1 16:08
     * @param : email
     * @param : session
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PostMapping("/code/{email}")
    public R<String> getCode(@PathVariable String email, HttpSession session){

        if (email == null){
            return R.error("邮箱号不能为空!");
        }else if (session.getAttribute("email") != email){
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            log.info(code);
            String md5Code = DigestUtils.md5DigestAsHex(code.getBytes(StandardCharsets.UTF_8));
//            log.info(md5Code);
            session.setAttribute("code",md5Code);
            session.setAttribute("email",email);

            try {
                Email.sendEmail(email,code);
            } catch (MessagingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return R.error("获取验证码成功！");
        }else {
            return R.error("已发生验证码，请您注意查收邮件!");
        }


    }

}

