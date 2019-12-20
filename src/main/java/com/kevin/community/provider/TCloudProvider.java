package com.kevin.community.provider;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Random;

@Service
public class TCloudProvider {
//    tCloud.secret.id=AKIDgEcv58veCcqtqmg8XovqZAbu0wlQIAGC
//    tCloud.secret.key=udAZptoeKrGTZ3OXZS9e3LWXP9lCB4qQ
//    @Value("${tCloud.secret.id}")
    private String secretId = "AKIDgEcv58veCcqtqmg8XovqZAbu0wlQIAGC";
//    @Value("${tCloud.secret.key}")
    private String secretKey ="udAZptoeKrGTZ3OXZS9e3LWXP9lCB4qQ";
    private static final String bucketName = "yinwk-1258077417";

    private COSClient cOSClient;

    COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
    Region region = new Region("ap-guangzhou");
    ClientConfig clientConfig = new ClientConfig(region);
//    CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
    // 3 生成 cos 客户端。
    COSClient cosClient = new COSClient(cred, clientConfig);

    public TCloudProvider() {
        cOSClient = new COSClient(cred, clientConfig);
    }
//
//    public String upload(InputStream fileStream, String fileName) {
//
//        String generatedFileName = "";
//        String[] filePaths = fileName.split(",");
//        if (filePaths.length > 1) {
//            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
//
//        } else {
//            return null;
//        }
//        try {
//
//            PutObjectRequest putObjectRequest = new PutObjectRequest(createBucketRequest, generatedFileName, fileStream);
//
//            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
//        } catch (CosServiceException serverException) {
//            serverException.printStackTrace();
//        } catch (CosClientException clientException) {
//            clientException.printStackTrace();
//        }
//        return generatedFileName;
//    }

//    // 1 初始化用户身份信息(secretId, secretKey)
//    private static final COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
//    // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
//    private static final ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
//    // 3 生成cos客户端
//    private static final COSClient cosClient = new COSClient(cred, clientConfig);
    // 文件存储目录
    //private String filedir = "blog/";
//    private COSClient cOSClient;



    /**
     * 销毁
     */
    public void destory() {
        cOSClient.shutdown();
    }

    /**
     * 上传图片
     *
     * @param url
     */
    public void uploadImg2Cos(String url) throws Exception {
        File fileOnServer = new File(url);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fileOnServer);
            String[] split = url.split("/");
            this.uploadFile2Cos(fin, split[split.length - 1]);
        } catch (FileNotFoundException e) {
            throw new Exception("图片上传失败");
        }
    }

    public String uploadFile2Cos(MultipartFile file) throws Exception {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new Exception("上传图片大小不能超过10M！");
        }
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2Cos(inputStream, name);
            return name;
        } catch (Exception e) {
            throw new Exception("图片上传失败");
        }
    }

    /**
     * 获得图片路径
     *
     * @param fileUrl
     * @return
     */
    public String getImgUrl(String fileUrl) {
        return getUrl(fileUrl);
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年 3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = cosClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * 上传到COS服务器 如果同名文件会覆盖服务器上的
     *
     * @param instream
     *            文件流
     * @param fileName
     *            文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFile2Cos(InputStream instream, String fileName) {
        String ret = "";
        try {
            // 创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            // 上传文件
            PutObjectResult putResult = cosClient.putObject(bucketName,  fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Description: 判断Cos服务文件上传时文件的contentType
     *
     * @param filenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String filenameExtension) {
        if (filenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (filenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")
                || filenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (filenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (filenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (filenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (filenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }

}
