package com.echoes.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author : Xiaozp
 * @ClassName : MybatisPlusConfig
 * @Description : MP配置类
 * @create : 2022/7/26 14:13
 * @Version : v1.0
 * @Powered By Corner Lab
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * @Name : mybatisPlusInterceptor
     * @description : MP添加分页插件
     * @createTime : 2022/7/26 14:16
     * @return : com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return  interceptor;
    }
}
