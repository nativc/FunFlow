package com.nativc.funflow.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.nativc.funflow.common.Code;
import com.nativc.funflow.config.OssConfig;
import com.nativc.funflow.exception.BusinessException;
import com.nativc.funflow.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * OSS 文件上传服务实现类
 * 提供通用的文件上传功能
 */
@Service
public class OssServiceImpl implements OssService {

    @Autowired
    private OSS ossClient;

    @Autowired
    private OssConfig ossConfig;

    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            // 获取文件输入流
            InputStream inputStream = file.getInputStream();

            // 设置文件元信息
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // 拼接完整路径：basePath + fileName
            String fullPath = ossConfig.getBasePath() + fileName;

            // 上传到 OSS
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    ossConfig.getBucketName(),
                    fullPath,
                    inputStream,
                    metadata
            );
            ossClient.putObject(putObjectRequest);

            // 返回文件访问 URL
            return "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/" + fullPath;

        } catch (Exception e) {
            throw new BusinessException(Code.SYSTEM_ERROR, "文件上传失败：" + e.getMessage());
        }
    }
}
