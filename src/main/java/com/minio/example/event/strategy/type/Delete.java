package com.minio.example.event.strategy.type;

import com.minio.example.dto.FileEvent;
import org.springframework.kafka.core.KafkaTemplate;

public class Delete implements EventType {

    @Override
    public void processEvent(FileEvent event, KafkaTemplate<String, FileEvent> kafkaTemplate) {
        kafkaTemplate.send("file-delete-events", event.getObjectName(), event);
    }
}
