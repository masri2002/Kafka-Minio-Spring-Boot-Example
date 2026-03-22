package com.minio.example.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic uploadTopic() {
        return TopicBuilder.name("file-upload-events").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic downloadTopic() {
        return TopicBuilder.name("file-download-events").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic deleteTopic() {
        return TopicBuilder.name("file-delete-events").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic minioEventsTopic() {
        return TopicBuilder.name("minio-file-events").partitions(3).replicas(1).build();
    }
}