package com.blogdemo.service.impl;

import com.blogdemo.domain.ResponseResult;
import com.blogdemo.domain.entity.User;
import com.blogdemo.utils.SecurityUtils;
import com.blogdemo.utils.qiniuoss.OssKey;
import com.blogdemo.domain.enums.AppHttpCodeEnum;
import com.blogdemo.exception.SystemException;
import com.blogdemo.service.UploadService;
import com.blogdemo.utils.qiniuoss.PathUtils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 *
 */
@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    private OssKey ossKey;


    @Override
    public ResponseResult uploadHeadImg(MultipartFile img) {
        //判断文件类型
        // MultipartFile中自带一个获取原始文件名方法
        String originalFilename = img.getOriginalFilename();
        // 判断文件是否为.png
        if(!originalFilename.endsWith(".png")){
            throw new SystemException(AppHttpCodeEnum.PNG_ERROR);
        }
        // 用自己封装的工具类获取路径,传入当前用户账号
        // TODO 需要修改前端回传token,才可以实现用当前用户名当作文件名

        //生成路径 ，在七牛云暂时统一叫user文件夹存放用户头像
        String filePath = PathUtils.generateFilePath(originalFilename,"user");
        String url = uploadOss(img,filePath);

        return ResponseResult.okResult(url); 
    }


    public String uploadOss(MultipartFile img,String filePath){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        try {
            InputStream inputStream = img.getInputStream();
            Auth auth = Auth.create(ossKey.getAccessKey(), ossKey.getSecretKey());
            String upToken = auth.uploadToken(ossKey.getBucket());
            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return ossKey.getOpenurl()+"/"+filePath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "好像失败了";
    }
}
