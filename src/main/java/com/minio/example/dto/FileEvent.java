package com.minio.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileEvent {
    private String eventType;    // "UPLOAD", "DOWNLOAD", "DELETE"
    private String bucketName;
    private String objectName;
    private String filePath;     // local path, used only for UPLOAD events
    private String contentType;
}