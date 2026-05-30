package com.minio.example.service;

import com.minio.example.dto.FileEvent;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String defaultBucket;

    public void uploadFile(FileEvent event)
            throws Exception {
        ensureBucketExists(event.getBucketName());
        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(event.getBucketName())
                        .object(event.getObjectName())
                        .filename(event.getFilePath())
                        .contentType(event.getContentType())
                        .build()
        );
        log.info("Uploaded: {}/{}", event.getBucketName(), event.getObjectName());
    }

    public InputStream downloadFile(FileEvent event) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(event.getBucketName())
                        .object(event.getObjectName())
                        .build()
        );
    }

    public void deleteFile(FileEvent event) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(event.getBucketName())
                        .object(event.getObjectName())
                        .build()
        );
        log.info("Deleted: {}/{}", event.getBucketName(), event.getObjectName());
    }

    private void ensureBucketExists(String bucket) throws Exception {
        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucket).build()
        );
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    public String readFile(String bucket, String objectName) throws Exception {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build())) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}