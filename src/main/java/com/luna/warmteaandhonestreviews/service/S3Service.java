package com.luna.warmteaandhonestreviews.service;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    private static final Logger log = LoggerFactory.getLogger(S3Service.class);
    private final S3Template s3Template;
    private final String filePath;
    private final String bucketName;

    public S3Service(S3Template s3Template,
        @Value("${s3.file-path}") String filePath,
        @Value("${s3.bucket}") String bucketName) {
        this.s3Template = s3Template;
        this.filePath = filePath;
        this.bucketName = bucketName;
    }

    public String getURL(MultipartFile file) {
        S3Resource s3Resource = upload(file);
        try {
            return s3Resource.getURL().toString();
        } catch (IOException e) {
            log.error("Failed to get URL from S3Resource: {}", e.getMessage());
            throw new RuntimeException("Failed to get URL from S3Resource");
        }
    }

    public S3Resource upload(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String key = filePath + "/" + file.getOriginalFilename();

            ObjectMetadata objectMetadata = ObjectMetadata.builder()
                .contentType("image/jpeg")
                .build();

            return s3Template.upload(
                bucketName,
                key,
                inputStream,
                objectMetadata
            );
        } catch (IOException e) {
            log.error("Failed to upload file to S3: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public S3Resource download(String fileName) {
        String key = filePath + "/" + fileName;
        log.info("bucket name: {}, key: {}", bucketName, key);
        S3Resource download = s3Template.download(bucketName, key);
        return download;
    }
}
