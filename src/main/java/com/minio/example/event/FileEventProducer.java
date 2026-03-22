package com.minio.example.event;

import com.minio.example.dto.FileEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileEventProducer {

    private final KafkaTemplate<String, FileEvent> kafkaTemplate;

    public void publishUploadEvent(FileEvent event) {
        kafkaTemplate.send("file-upload-events", event.getObjectName(), event);
    }

    public void publishDownloadEvent(FileEvent event) {
        kafkaTemplate.send("file-download-events", event.getObjectName(), event);
    }

    public void publishDeleteEvent(FileEvent event) {
        kafkaTemplate.send("file-delete-events", event.getObjectName(), event);
    }
}