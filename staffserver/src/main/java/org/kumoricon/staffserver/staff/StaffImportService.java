package org.kumoricon.staffserver.staff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class StaffImportService {
    private static final Logger log = LoggerFactory.getLogger(StaffImportService.class);

    @Value("${staffreg.onlineinputpath}")
    private String onlineImportInputPath;

    @Value("${staffreg.onlinedlqpath}")
    private String onlineDLQPath;

    private Path inputPath;
    private Path dlqPath;


    @Scheduled(fixedDelay = 10000)
    public void doWork() {
        log.info("Reading files from " + onlineImportInputPath);
    }

    @PostConstruct
    public void createDirectories() {
        try {
            inputPath = Files.createDirectories(Paths.get(onlineImportInputPath));
            dlqPath = Files.createDirectories(Paths.get(onlineDLQPath));
            log.info("Monitoring input path: " + inputPath.toAbsolutePath().toString());
            log.info("DLQ path: " + dlqPath.toAbsolutePath().toString());
        } catch (IOException ex) {
            log.error("Error creating directory", ex);
        }
    }
}
