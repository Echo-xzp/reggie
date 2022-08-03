package com.echoes.controller;

import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.echoes.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author : Xiaozp
 * @ClassName : CommonController
 * @Description : 文件的上传与下载
 * @create : 2022/7/27 14:38
 * @Version : v1.0
 * @Powered By Corner Lab
 */

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    // 图片路径
    static private String basePath = (ClassUtils.getDefaultClassLoader()
                                .getResource("static").getPath() +"/dishImg/");
//    static private String basePath;
//
//    @Value("${reggie.path}")
//    public void setBasePath(String path){
//        basePath = path;
//    }

    /**
     * @Name : upload
     * @description : 文件上传
     * @createTime : 2022/7/27 15:09
     * @param : file
     * @return : com.echoes.common.R<java.lang.String>
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        String originalFileName = file.getOriginalFilename();
        int i = originalFileName.lastIndexOf(".");
        String suffix = originalFileName.substring(i);

        // 防止文件名重复,使用UUID重新创立名称
        String fileName = UUID.randomUUID().toString() + suffix;

        log.info(basePath);
        File dir = new File(basePath);

        if(!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return R.success(fileName);
    }

    @GetMapping("/download")
    public void  download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));

            // 输出流，写回浏览器，实现图片回显
            ServletOutputStream outputStream = response.getOutputStream();

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            fileInputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

        return;
    }

}
