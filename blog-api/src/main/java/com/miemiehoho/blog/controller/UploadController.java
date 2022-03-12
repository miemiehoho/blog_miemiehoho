package com.miemiehoho.blog.controller;

import com.miemiehoho.blog.utils.QiniuUtils;
import com.miemiehoho.blog.vo.ErrorCode;
import com.miemiehoho.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author miemiehoho
 * @date 2022/1/10 15:33
 */
@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private QiniuUtils qiniuUtils;


    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file) {
        // 原始文件名,例：aa.png
        String originalFilename = file.getOriginalFilename();
        // 获取文件后缀，生成唯一的文件名
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
        // 上传文件 上传到文件服务器，文件服务器（qiniuyun）速度快，会把图片发放到离用户最近的文件服务器上
        // 降低应用服务器的带宽消耗
        boolean upload = qiniuUtils.upload(file, fileName);
        return upload ? Result.success(QiniuUtils.url + fileName) : Result.fail(ErrorCode.UPLOAD_ERROR.getCode(), ErrorCode.UPLOAD_ERROR.getMsg());
    }
}
