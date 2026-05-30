package com.minio.example.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = FileEvent.FileEventBulider.class)
public class FileEvent {

    private final String eventType;
    private final String bucketName;
    private final String objectName;
    private final String filePath;
    private final String contentType;

    public FileEvent(FileEventBulider builder) {
        this.eventType = builder.eventType;
        this.bucketName = builder.bucketName;
        this.objectName = builder.objectName;
        this.filePath = builder.filePath;
        this.contentType = builder.contentType;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class FileEventBulider {

        private String eventType;
        private String bucketName;
        private String objectName;
        private String filePath;
        private String contentType;

        public FileEventBulider eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public FileEventBulider bucketName(String bucketName) {
            this.bucketName = bucketName;
            return this;
        }

        public FileEventBulider objectName(String objectName) {
            this.objectName = objectName;
            return this;
        }

        public FileEventBulider filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public FileEventBulider contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public FileEvent build() {
            return new FileEvent(this);
        }
    }
}