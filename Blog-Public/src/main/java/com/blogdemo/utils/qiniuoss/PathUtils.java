package com.blogdemo.utils.qiniuoss;
import java.util.UUID;


/**
 *
 * 上传头像获取当前用户id作为名字-路径工具类
 * 暂时未实现每一个用户id为名字，实现要求：要前端传token回后端，暂时不会
 */
public class PathUtils {

    public static String generateFilePath(String fileName,String userName){
        //根据用户名生成文件夹  username/uuid.png
        String user = userName+'/';
        //uuid作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //后缀和文件后缀一致
        int index = fileName.lastIndexOf(".");
        // test.jpg -> .jpg
        String fileType = fileName.substring(index);
        return new StringBuilder().append(user).append(uuid).append(fileType).toString();
    }
}
