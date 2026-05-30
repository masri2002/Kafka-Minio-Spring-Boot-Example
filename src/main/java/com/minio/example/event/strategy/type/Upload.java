package com.minio.example.event.strategy.type;

import com.minio.example.dto.FileEvent;
import org.springframework.kafka.core.KafkaTemplate;

public class Upload implements EventType {


    @Override
    public void processEvent(FileEvent event, KafkaTemplate<String, FileEvent> kafkaTemplate) {
        kafkaTemplate.send("file-upload-events", event.getObjectName(), event);

    }
}
