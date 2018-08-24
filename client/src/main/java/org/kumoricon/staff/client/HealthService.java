package org.kumoricon.staff.client;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Checks on the status of the outbound queue directory, getting the number of files waiting to be sent
 */
public class HealthService {
    private static final Logger log = LoggerFactory.getLogger(HealthService.class);

    private StringProperty statusMessage = new SimpleStringProperty("");

    private LongProperty workQueueCount = new SimpleLongProperty(0);
    private LongProperty outboundQueueCount = new SimpleLongProperty(0);

    @Inject
    private SettingsService settingsService;

    @PostConstruct
    public void init() {
        startFileCounter();
    }

    private void startFileCounter() {
        ScheduledService<String> svc = new ScheduledService<String>() {
            protected Task<String> createTask() {
                return new Task<String>() {
                    protected String call() {
                        Path outboundDir = Paths.get(settingsService.getOutboundQueue());
                        outboundQueueCount.set(countFiles(outboundDir));

                        Path workDir = Paths.get(settingsService.getWorkQueue());
                        workQueueCount.set(countFiles(workDir));

                        return "Waiting to be sent: " + outboundQueueCount.get();
                    }
                };
            }

            private long countFiles(Path directory) {
                try (Stream<Path> files = Files.list(directory)) {
                    long count = files.count();
                    return count;
                } catch (IOException ex) {
                    log.error("Error lising files in " + directory, ex);
                }
                return -1;
            }
        };
        svc.setPeriod(Duration.seconds(1));
        svc.setRestartOnFailure(true);
        svc.setMaximumCumulativePeriod(Duration.seconds(10));
        svc.setOnSucceeded(e -> statusMessage.setValue(e.getSource().getValue().toString()));
        svc.start();
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

    public long getWorkQueueCount() {
        return workQueueCount.get();
    }

    public LongProperty workQueueCountProperty() {
        return workQueueCount;
    }

    public long getOutboundQueueCount() {
        return outboundQueueCount.get();
    }

    public LongProperty outboundQueueCountProperty() {
        return outboundQueueCount;
    }
}
