package com.fu.community.provider;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.fu.community.exception.CustomizeErrorCode;
import com.fu.community.exception.CustomizeException;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * @author sang
 * @class:AliyunOSSClientUtil
 * @descript:java使用阿里云OSS存储对象上传图片
 * @date:2017年3月16日 下午5:58:08
 */
public class AliyunOSSClientUtil {
    //log日志
    private static Logger logger = Logger.getLogger(AliyunOSSClientUtil.class);
    //阿里云API的内或外网域名
    private static String ENDPOINT;
    //阿里云API的密钥Access Key ID
    private static String ACCESS_KEY_ID;
    //阿里云API的密钥Access Key Secret
    private static String ACCESS_KEY_SECRET;
    //阿里云API的bucket名称
    private static String BACKET_NAME;
    //阿里云API的文件夹名称
    private static String FOLDER;

    //初始化属性
    static {
        ENDPOINT = "oss-cn-beijing.aliyuncs.com";
        ACCESS_KEY_ID = "LTAI4Fg9Da23Ph3GJNffT3qz";
        ACCESS_KEY_SECRET = "LXISddKeD8tL1Hi1GAo004xOCoWTPA";
        BACKET_NAME = "mawe";
        FOLDER = "test/";
    }

    /**
     * 获取阿里云OSS客户端对象
     *
     * @return ossClient
     */
    public static OSSClient getOSSClient() {
        return new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 创建存储空间
     *
     * @param ossClient  OSS连接
     * @param bucketName 存储空间
     * @return
     */
    public static String createBucketName(OSSClient ossClient, String bucketName) {
        //存储空间
        final String bucketNames = bucketName;
        if (!ossClient.doesBucketExist(bucketName)) {
            //创建存储空间
            Bucket bucket = ossClient.createBucket(bucketName);
            logger.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketNames;
    }

    /**
     * 删除存储空间buckName
     *
     * @param ossClient  oss对象
     * @param bucketName 存储空间
     */
    public static void deleteBucket(OSSClient ossClient, String bucketName) {
        ossClient.deleteBucket(bucketName);
        logger.info("删除" + bucketName + "Bucket成功");
    }

    /**
     * 创建模拟文件夹
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名如"qj_nanjing/"
     * @return 文件夹名
     */
    public static String createFolder(OSSClient ossClient, String bucketName, String folder) {
        //文件夹名
        final String keySuffixWithSlash = folder;
        //判断文件夹是否存在，不存在则创建
        if (!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)) {
            //创建文件夹
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            logger.info("创建文件夹成功");
            //得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir = object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    /**
     * 根据key删除OSS服务器上的文件
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名 如"qj_nanjing/"
     * @param key        Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public static void deleteFile(OSSClient ossClient, String bucketName, String folder, String key) {
        ossClient.deleteObject(bucketName, folder + key);
        logger.info("删除" + bucketName + "下的文件" + folder + key + "成功");
    }


    public static String uploadObject2OSS(OSSClient ossClient, InputStream fileStream, String fileName, String bucketName) {

        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1) {
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        } else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }

        try {
            //以输入流的形式上传文件
            InputStream is = fileStream;

            //上传文件   (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, generatedFileName, is);
            // 设置URL过期时间为1年。
            Date expiration = new Date(new Date().getTime() + 1000*60*60*24*365);
            // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
            URL url = ossClient.generatePresignedUrl(bucketName, generatedFileName, expiration);
            ossClient.shutdown();
            return url.toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return null;
    }


    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public static String getContentType(String fileName) {
        //文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension) || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        //默认返回类型
        return "image/jpeg";
    }

}