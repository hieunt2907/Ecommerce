package com.shino.ecommerce.common.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LogConsumer {
    private static final Logger logger = LoggerFactory.getLogger(LogConsumer.class);
    private final ObjectMapper objectMapper;
    
    public LogConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${spring.kafka.topic.log}", groupId = "log-group")
    public void consume(LogDTO logDTO) {
        try {
            String logJson = objectMapper.writeValueAsString(logDTO);
            writeToFile(logJson);
        } catch (Exception e) {
            logger.error("Error processing log message: {}", e.getMessage(), e);
        }
    }

    private void writeToFile(String logJson) {
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        File logFile = new File(logDir, "system-logs.json");
        try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
            out.println(logJson);
        } catch (IOException e) {
            logger.error("Error writing to log file: {}", e.getMessage(), e);
        }
    }
}
