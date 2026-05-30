package com.minio.example.event.strategy;

import com.minio.example.dto.FileEvent;
import com.minio.example.event.strategy.type.Delete;
import com.minio.example.event.strategy.type.Download;
import com.minio.example.event.strategy.type.EventType;
import com.minio.example.event.strategy.type.Upload;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EventHandler {

    private final Map<String, EventType> strategies = new HashMap<>();
    private final KafkaTemplate<String, FileEvent> kafkaTemplate;

    public EventHandler(KafkaTemplate<String, FileEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        strategies.put("UPLOAD", new Upload());
        strategies.put("DOWNLOAD", new Download());
        strategies.put("DELETE", new Delete());

    }

    public void handle(String type, FileEvent event) {
        strategies.get(type).processEvent(event, kafkaTemplate);
    }
}
