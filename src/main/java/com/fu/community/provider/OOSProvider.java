package com.fu.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

/**
 * @description:
 * @author: FuMaoDong
 * @time: 2019/10/13 14:11
 */

@Service
public class OOSProvider {

    // Endpoint以杭州为例，其它Region请按实际情况填写。

    @Value("oss-cn-beijing.aliyuncs.com")
    private String endpoint ;
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    @Value("aliyun.oos.access-key-id")
    private String accessKeyId ;
    @Value("aliyun.oos.access-key-secret")
    private String accessKeySecret ;
    @Value("aliyun.oos.bucketName")
    private String bucketName ;


    public String upload(){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
        String content = "Hello OSS";
        ossClient.putObject(bucketName, "1", new ByteArrayInputStream(content.getBytes()));

// 关闭OSSClient。
        ossClient.shutdown();
        return null;
    }


}
