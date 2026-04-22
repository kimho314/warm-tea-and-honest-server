package com.luna.warmteaandhonestreviews.aws.s3;

import com.luna.warmteaandhonestreviews.service.StorageService;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@SpringBootTest
public class S3ClientTest {

    private static final Logger log = LoggerFactory.getLogger(S3ClientTest.class);
    @Autowired
    S3Template s3Template;
    @Autowired
    StorageService storageService;
    @Autowired
    S3Client s3Client;

    @Value("${s3.bucket}")
    private String bucketName;
    @Value("${s3.file-path}")
    private String filePath;

    @Test
    void storeTest_S3Client() throws IOException {
        log.info("bucketName={}, filePath={}", bucketName, filePath);
        Resource resource = storageService.loadAsResource("IlkbaharRuyasi.jpg");
        String fileName = resource.getFilename();
        if (fileName == null) {
            throw new IOException("fileName is null");
        }
        String key = filePath + "/" + fileName;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType("image/jpeg")
            .build();
        PutObjectResponse response = s3Client.putObject(objectRequest, resource.getFilePath());
        log.info("stored={}", response.toString());
    }

    @Test
    void storeTest_S3Template() throws IOException {
        log.info("bucketName={}, filePath={}", bucketName, filePath);
        Resource resource = storageService.loadAsResource("IlkbaharRuyasi.jpg");
        String fileName = resource.getFilename();
        if (fileName == null) {
            throw new IOException("fileName is null");
        }
        String key = filePath + "/" + fileName;

        ObjectMetadata objectMetadata = ObjectMetadata.builder()
            .contentType("image/jpeg")
            .build();
        S3Resource stored = s3Template.upload(
            bucketName,
            key,
            resource.getInputStream(),
            objectMetadata
        );
        log.info("stored={}", stored.toString());

    }
}
