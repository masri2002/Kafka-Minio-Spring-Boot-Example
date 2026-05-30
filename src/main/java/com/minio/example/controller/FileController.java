package com.minio.example.controller;

import com.minio.example.dto.FileEvent;
import com.minio.example.event.strategy.EventHandler;
import com.minio.example.event.strategy.type.Delete;
import com.minio.example.event.strategy.type.Download;
import com.minio.example.event.strategy.type.Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final Upload upload = new Upload();
    private final Download download = new Download();
    private final Delete delete = new Delete();
    private final EventHandler eventHandler;

    @Value("${minio.bucket}")
    private String bucket;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam String objectName,
                                         @RequestParam String filePath,
                                         @RequestParam String contentType) {
        FileEvent event = new FileEvent.FileEventBulider().eventType("UPLOAD")
                .bucketName(bucket)
                .objectName(objectName)
                .filePath(filePath)
                .contentType(contentType)
                .build();
        eventHandler.handle("UPLOAD", event);
        return ResponseEntity.accepted().body("Upload event published for: " + objectName);
    }

    @GetMapping("/download/{objectName}")
    public ResponseEntity<String> download(@PathVariable String objectName) {
        FileEvent event = new FileEvent.FileEventBulider()
                .eventType("DOWNLOAD")
                .bucketName(bucket)
                .objectName(objectName)
                .build();
        eventHandler.handle("DOWNLOAD", event);
        return ResponseEntity.accepted().body("Download event published for: " + objectName);
    }

    @DeleteMapping("/{objectName}")
    public ResponseEntity<String> delete(@PathVariable String objectName) {
        FileEvent event = new FileEvent.FileEventBulider()
                .eventType("DELETE")
                .bucketName(bucket)
                .objectName(objectName)
                .build();
        eventHandler.handle("DELETE", event);
        return ResponseEntity.accepted().body("Delete event published for: " + objectName);
    }
}