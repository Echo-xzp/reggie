package com.echoes;

import com.echoes.controller.CommonController;
import com.echoes.utils.Email;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

@SpringBootTest
class ReggieApplicationTests {
    @Autowired
    private CommonController commonController;


    @Test
    void contextLoads() throws MessagingException {

//        Email.sendEmail("2838985911@qq.com",1234);
    }

}
