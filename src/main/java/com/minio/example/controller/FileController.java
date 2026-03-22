package com.minio.example.controller;

import com.minio.example.dto.FileEvent;
import com.minio.example.event.FileEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileEventProducer producer;

    @Value("${minio.bucket}")
    private String bucket;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam String objectName,
                                         @RequestParam String filePath,
                                         @RequestParam String contentType) {
        FileEvent event = new FileEvent("UPLOAD", bucket, objectName, filePath, contentType);
        producer.publishUploadEvent(event);
        return ResponseEntity.accepted().body("Upload event published for: " + objectName);
    }

    @GetMapping("/download/{objectName}")
    public ResponseEntity<String> download(@PathVariable String objectName) {
        FileEvent event = new FileEvent("DOWNLOAD", bucket, objectName, null, null);
        producer.publishDownloadEvent(event);
        return ResponseEntity.accepted().body("Download event published for: " + objectName);
    }

    @DeleteMapping("/{objectName}")
    public ResponseEntity<String> delete(@PathVariable String objectName) {
        FileEvent event = new FileEvent("DELETE", bucket, objectName, null, null);
        producer.publishDeleteEvent(event);
        return ResponseEntity.accepted().body("Delete event published for: " + objectName);
    }
}