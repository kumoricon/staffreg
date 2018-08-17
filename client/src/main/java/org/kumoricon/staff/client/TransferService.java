package org.kumoricon.staff.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.kumoricon.staff.client.dto.StaffEvent;
import org.kumoricon.staff.client.stafflistscreen.checkindetails.EventFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TransferService {
    private static final Logger log = LoggerFactory.getLogger(TransferService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private StringProperty statusMessage = new SimpleStringProperty("");

    @Inject
    private SettingsService settingsService;



    @PostConstruct
    public void init() {
        ScheduledService<String> svc = new ScheduledService<String>() {
            protected Task<String> createTask() {
                return new Task<String>() {
                    protected String call() {
                        log.info("Sending files");
                        Path outboundDir = Paths.get(settingsService.getOutboundQueue());

                        try (Stream<Path> files = Files.list(outboundDir)) {
                            files.forEach(p -> {
                                File f = p.toFile();
                                log.info("Deleting " + f.getAbsolutePath());
                                boolean result = f.delete();
                                if (result) {
                                    log.info("Deleted " + f);
                                }
                            });

                        } catch (IOException ex) {

                        }
                        return "Waiting to be sent: " + countFiles(outboundDir);
                    }
                };
            }

            private String countFiles(Path directory) {
                try (Stream<Path> files = Files.list(directory)) {
                    long count = files.count();
                    return Long.toString(count);
                } catch (IOException ex) {
                    log.error("Error lising files in " + directory, ex);
                }
                return "Error";
            }
        };
        svc.setPeriod(Duration.seconds(15));
        svc.setRestartOnFailure(true);
        svc.setMaximumCumulativePeriod(Duration.minutes(1));
        svc.setOnSucceeded(e -> statusMessage.setValue(e.getSource().getValue().toString()));
        svc.start();
    }

    public void saveImageToWorkDirectory() {

    }

    public void moveImagesToOutboundDirectory() {

    }

    public void queueEventToSend(StaffEvent event) {
        File outputFile = buildFilename(event);
        try {
            objectMapper.writeValue(outputFile, event);
        } catch (IOException ex) {
            log.error("Error writing file " + outputFile, ex);
            throw new RuntimeException(ex);
        }
    }

    private File buildFilename(StaffEvent event) {
        File outputFile = new File(settingsService.getOutboundQueue() +
                System.currentTimeMillis() + "-" + event.getEventType() + ".json");
        return outputFile;
    }

    public String getStatusMessage() {
        return statusMessage.get();
    }

    public StringProperty statusMessageProperty() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage.set(statusMessage);
    }
}
