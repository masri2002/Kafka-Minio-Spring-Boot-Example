package com.minio.example.event;

import com.minio.example.dto.FileEvent;
import com.minio.example.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileEventConsumer {

    private final MinioService minioService;

    @KafkaListener(topics = "file-upload-events", groupId = "file-service-group")
    public void handleUpload(FileEvent event) {
        log.info("Upload event received: {}", event.getObjectName());
        try {
            minioService.uploadFile(
                    event.getBucketName(),
                    event.getObjectName(),
                    event.getFilePath(),
                    event.getContentType()
            );
        } catch (Exception e) {
            log.error("Upload failed: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "file-download-events", groupId = "file-service-group")
    public void handleDownload(FileEvent event) {
        log.info("Download event received: {}", event.getObjectName());
        try (InputStream stream = minioService.downloadFile(
                event.getBucketName(), event.getObjectName())) {
            log.info("Downloaded {} bytes", stream.available());
        } catch (Exception e) {
            log.error("Download failed: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "file-delete-events", groupId = "file-service-group")
    public void handleDelete(FileEvent event) {
        log.info("Delete event received: {}", event.getObjectName());
        try {
            minioService.deleteFile(event.getBucketName(), event.getObjectName());
        } catch (Exception e) {
            log.error("Delete failed: {}", e.getMessage());
        }
    }
}