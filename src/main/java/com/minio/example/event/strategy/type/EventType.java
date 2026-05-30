package com.minio.example.event.strategy.type;

import com.minio.example.dto.FileEvent;
import org.springframework.kafka.core.KafkaTemplate;

public interface EventType {

    public void processEvent(FileEvent event, KafkaTemplate<String, FileEvent> kafkaTemplate);
}
