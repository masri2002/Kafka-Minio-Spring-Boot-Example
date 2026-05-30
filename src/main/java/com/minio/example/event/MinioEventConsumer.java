package com.minio.example.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minio.example.dto.FileEvent;
import com.minio.example.dto.Person;
import com.minio.example.parsers.CsvParsers;
import com.minio.example.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioEventConsumer {

    private final MinioService minioService;
    private final CsvParsers csvParsers;
    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(
            topics = "minio-file-events",
            groupId = "file-service-group",
            containerFactory = "stringKafkaListenerContainerFactory"
    )
    public void handleMinioEvent(String rawEvent) {
        try {
            JsonNode root = mapper.readTree(rawEvent);
            JsonNode records = root.path("Records");

            if (records.isMissingNode() || !records.isArray()) {
                log.warn("No Records in MinIO event payload");
                return;
            }

            for (JsonNode record : records) {
                String eventName = record.path("eventName").asText();
                String bucketName = record.path("s3").path("bucket").path("name").asText();
                String objectName = record.path("s3").path("object").path("key").asText();
                long objectSize = record.path("s3").path("object").path("size").asLong();
                String eventTime = record.path("eventTime").asText();

                log.info("==============================================");
                log.info("  MinIO event received!");
                log.info("  Event  : {}", eventName);
                log.info("  Bucket : {}", bucketName);
                log.info("  File   : {}", objectName);
                log.info("  Size   : {} bytes", objectSize);
                log.info("  Time   : {}", eventTime);
                log.info("==============================================");

                if (eventName.startsWith("s3:ObjectCreated")) {
                    readAndParseCSV(bucketName, objectName);
                }
            }

        } catch (Exception e) {
            log.error("Failed to parse MinIO event: {}", e.getMessage());
        }
    }

    private void readAndParseCSV(String bucket, String objectName) {

        if (!objectName.toLowerCase().endsWith(".csv")) {
            log.warn("Not a CSV file, skipping: {}", objectName);
            deleteFile(bucket, objectName);
            return;
        }

        String content;
        try {
            content = minioService.readFile(bucket, objectName);
        } catch (Exception e) {
            log.error("Failed to read {}/{}: {}", bucket, objectName, e.getMessage());
            return;
        }

        List<Person> people = csvParsers.parseCsv(content);

        log.info("----------------------------------------------");
        log.info("  Parsed {} record(s) from: {}", people.size(), objectName);
        log.info("----------------------------------------------");
        for (Person p : people) {
            log.info("  First name : {}", p.getFirstName());
            log.info("  Last name  : {}", p.getLastName());
            log.info("  Age        : {}", p.getAge());
            log.info("  Country    : {}", p.getCountry());
            log.info("  ............................................");
        }
        log.info("----------------------------------------------");

        deleteFile(bucket, objectName);
    }

    private void deleteFile(String bucket, String objectName) {
        try {
            FileEvent event = new FileEvent.FileEventBulider()
                    .bucketName(bucket)
                    .objectName(objectName)
                    .build();
            minioService.deleteFile(event);
            log.info("Deleted after processing: {}/{}", bucket, objectName);
        } catch (Exception e) {
            log.error("Failed to delete {}/{}: {}", bucket, objectName, e.getMessage());
        }
    }
}
