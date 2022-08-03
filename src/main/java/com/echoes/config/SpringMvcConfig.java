package com.echoes.config;

import com.echoes.common.JacksonObjectMapper;
import com.echoes.interceptor.LoginInterceptor;
import com.echoes.interceptor.PowerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @Author : Xiaozp
 * @ClassName : SpringMvcConfig
 * @Description : TODO
 * @create : 2022/7/24 14:00
 * @Version : v1.0
 * @Powered By Corner Lab
 */

@Configuration
public class SpringMvcConfig extends WebMvcConfigurationSupport {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private PowerInterceptor powerInterceptor;


    /**
     * @Name : addResourceHandlers
     * @description : 添加静态资源访问根
     * @createTime : 2022/7/25 13:42
     * @param : registry
     * @return : void
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/backend/**")
                .addResourceLocations("classpath:/static/backend/");

        registry.addResourceHandler("/front/**")
                .addResourceLocations("classpath:/static/front/");

    }

    /**
     * @Name : addInterceptors
     * @description : 添加拦截器 ：登录拦截器
     * @createTime : 2022/7/25 13:43
     * @param : registry
     * @return : void
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
       // 拦截直接放行路径
        String [] urls = new String[]{
                "/",
                "/employee/login",
                "/backend/**",
                "/front/**",
                "/user/code/*",
                "/user/login",
                "/dishImg/*",
                "/error"
        };
        registry.addInterceptor(loginInterceptor).excludePathPatterns(urls);

//        registry.addInterceptor(powerInterceptor).addPathPatterns("/employee");
    }

    /**
     * @Name : extendMessageConverters
     * @description : 拓展MVC消息转换器
     * @createTime : 2022/7/26 15:18
     * @param : converters
     * @return : void
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创立消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置消息转换器，底层使用Jackson
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 上面转换器添加进mvc转换器序列中，排序为第一
        converters.add(0,messageConverter);
        return;
    }
}
