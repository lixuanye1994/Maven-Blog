package com.blogdemo.service;

import com.blogdemo.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseResult uploadHeadImg(MultipartFile img);
}
