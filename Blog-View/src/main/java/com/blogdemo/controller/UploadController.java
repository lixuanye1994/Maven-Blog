package com.blogdemo.controller;

import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.User;
import com.blogdemo.service.UploadService;
import com.blogdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult uploadHeadImg(MultipartFile img){
        return uploadService.uploadHeadImg(img);
    }
}