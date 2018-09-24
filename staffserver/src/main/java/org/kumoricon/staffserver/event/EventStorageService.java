package org.kumoricon.staffserver.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kumoricon.staffserver.imageupload.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

@Service
public class EventStorageService {
    private static final Logger log = LoggerFactory.getLogger(EventStorageService.class);
    @Value("${staffreg.file.eventlogdir}")
    private String eventLogString;

    private Path eventLogPath;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    void storeEvent(StaffEventRecord staffEventRecord) {

        try {
            Path targetPath = eventLogPath.resolve(staffEventRecord.getReceivedAt().toEpochMilli() + "-" + staffEventRecord.getEvent().getEventType() + ".json");

            objectMapper.writeValue(targetPath.toFile(), staffEventRecord);

        } catch (IOException ex) {
            throw new FileStorageException("Error storing event {}", ex);
        }
    }

    @PostConstruct
    public void createDirectories() {
        try {
            eventLogPath = Files.createDirectories(Paths.get(eventLogString));
            log.info("Impage upload path: " + eventLogPath.toAbsolutePath().toString());
        } catch (IOException ex) {
            log.error("Error creating directory", ex);
        }
    }


}
