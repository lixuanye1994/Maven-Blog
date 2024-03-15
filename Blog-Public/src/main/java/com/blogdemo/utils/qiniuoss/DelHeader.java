package com.blogdemo.utils.qiniuoss;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.util.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 用户每次操作头像结束后，保存操作时，自动删除其他头像，来节省云空间
 * 测试中，暂时没用
 */
@Component

public class DelHeader {
    @Autowired
    private OssKey ossKey;

    public void del(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释


        Auth auth = Auth.create(ossKey.getAccessKey(), ossKey.getSecretKey());
        BucketManager bucketManager = null;

        try {
            //单次批量请求的文件数量不得超过1000
            String[] keyList = new String[]{
                    "qiniu.jpg",
                    "qiniu.mp4",
                    "qiniu.png",
            };
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addStatOps(ossKey.getBucket(), keyList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);

            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keyList[i];
                System.out.print(key+"\t");
                if (status.code == 200) {
                    //文件存在
                    System.out.println(status.data.hash);
                    System.out.println(status.data.mimeType);
                    System.out.println(status.data.fsize);
                    System.out.println(status.data.putTime);
                } else {
                    System.out.println(status.data.error);
                }
            }
        } catch (
                QiniuException ex) {
            System.err.println(ex.response.toString());
        }
    }



}
