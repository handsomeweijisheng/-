package com.wjs.takeout.controller;

import com.wjs.takeout.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @author wjs
 * @createTime 2022-11-06 15:43
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    /**
     * 图片上传
     */
    @Value("${reggie.path}")
    private String uploadLocation;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        String fileName = null;
        //得到上传的文件
        try {
            //log.info("{}",file);
            //截取文件后缀
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            //生成UUID
            String randomUUID = UUID.randomUUID().toString();
            //新的文件名字
            fileName = randomUUID + suffix;
            //如果上传文件夹不存在创建新的文件夹
            File fileUploadLocation = new File(uploadLocation);
            if (!fileUploadLocation.exists()) {
                fileUploadLocation.mkdirs();
            }
            file.transferTo(new File(uploadLocation + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success(fileName);
    }

    /**
     * 图片下载
     *
     * @param name         图片名字
     * @param
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        FileInputStream fileInputStream=null;
        ServletOutputStream servletOutputStream=null;
        //把刚刚存的文件读取到内存中，准备回显
        try {
             fileInputStream = new FileInputStream(new File(uploadLocation + name));
            //把读取到内存中的图片用输出流写入Servlet响应对象里
             servletOutputStream = response.getOutputStream();
            //可选项，选择响应类型
            response.setContentType("image/jpeg");
            //    用byte数组读取并响应
            byte[] bytes = new byte[1024 * 5];
            int length = 0;
            while ((length = fileInputStream.read(bytes)) != -1) {
                //写入响应流，从0开始，写入到数组末尾长度
                servletOutputStream.write(bytes, 0, length);
                servletOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(!StringUtils.isEmpty(fileInputStream)){
                fileInputStream.close();
            }
            if(!StringUtils.isEmpty(servletOutputStream)){
                servletOutputStream.close();
            }
        }
    }
}
