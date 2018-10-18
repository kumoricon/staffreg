package org.kumoricon.staff.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.kumoricon.staff.dto.StaffEvent;
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
    private static final String IMAGE_UPLOAD_URI_PATH = "/uploadImage";
    private static final String JSON_UPLOAD_URI_PATH = "/event";

    @Inject
    private SessionService sessionService;

    @Inject
    private SettingsService settingsService;

    @PostConstruct
    public void init() {
        ScheduledService<Void> svc = new ScheduledService<Void>() {
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    protected Void call() {
                        Path outboundDir = Paths.get(settingsService.getOutboundQueue());

                        try (Stream<Path> files = Files.list(outboundDir)) {
                            files.forEach(curFilePath -> {
                                String uploadUrl;

                                if (curFilePath.toString().endsWith(".json")) {
                                    uploadUrl = sessionService.getServerHostname() + JSON_UPLOAD_URI_PATH;
                                } else {
                                    uploadUrl = sessionService.getServerHostname() + IMAGE_UPLOAD_URI_PATH;
                                }
                                FileBody bin = new FileBody(curFilePath.toFile());
                                HttpEntity reqEntity = MultipartEntityBuilder.create()
                                        .addPart("file", bin)
                                        .build();

                                HttpResponse response = null;
                                try {
                                    response = Request.Post(uploadUrl)
                                            .addHeader(HttpHeaders.AUTHORIZATION, sessionService.getHttpAuthHeader())
                                            .body(reqEntity)
                                            .execute()
                                            .returnResponse();
                                } catch (IOException ex) {
                                    log.error("Error uploading file", ex);
                                }

                                if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED) {
                                    try {
                                        Files.delete(curFilePath);
                                    } catch (IOException e) {
                                        log.error("Error deleting uploaded file {}", curFilePath, e);
                                    }
                                } else {
                                    String responseStatus;
                                    if (response == null) {
                                        responseStatus = "no response";
                                    } else {
                                        responseStatus = response.getStatusLine().toString();
                                    }
                                    log.error("Didn't get 202 response when uploading {} to {}. Got {} instead", curFilePath, uploadUrl, responseStatus);
                                }

                            });
                        } catch (IOException ex) {
                            log.error("Error sending files", ex);
                        }

                        return null;
                    }
                };
            }
        };
        svc.setPeriod(Duration.seconds(15));
        svc.setRestartOnFailure(true);
        svc.setMaximumCumulativePeriod(Duration.minutes(1));
        svc.start();
    }

    public void moveImagesToOutboundDirectory(String staffFileName) {
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                Path[] files = {
                        Paths.get(settingsService.getWorkQueue() + staffFileName + "-1.jpg"),
                        Paths.get(settingsService.getWorkQueue() + staffFileName + "-2.jpg")
                };

                int count = 0;
                boolean complete = false;
                while (!complete && count < 5) {
                    for (Path p : files) {
                        log.info("Checking " + p);
                        if (p.toFile().exists()) {
                            log.info("Moving " + p);
                            Path outbound = Paths.get(settingsService.getOutboundQueue() + p.getFileName());
                            try {
                                Files.move(p, outbound);
                            } catch (IOException ex) {
                                Thread.sleep(500);
                            }
                        }
                    }
                    if (!atLeastOneFileExists(files)) {
                        complete = true;
                    }
                    count++;
                }

                if (atLeastOneFileExists(files)){
                    log.error("ERROR: retried moving files from work queue to outbound queue, not all were moved");
                }
                return null;
            }
        };

        Thread th = new Thread(task);
        th.setUncaughtExceptionHandler((t, e) -> log.error("moveImagesToOutboundDirectory error", e));
        th.setDaemon(true);
        th.start();
    }

    private boolean atLeastOneFileExists(Path[] files) {
        for (Path p : files) {
            if (p.toFile().exists()) {
                return true;
            }
        }
        return false;
    }

    public void queueEventToSend(StaffEvent event) {
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                File outputFile = buildFilename(event);
                    objectMapper.writeValue(outputFile, event);
                return null;
            }
        };

        Thread th = new Thread(task);
        th.setUncaughtExceptionHandler((t, e) -> log.error("queueEventToSend error", e));
        th.setDaemon(true);
        th.start();
    }

    private File buildFilename(StaffEvent event) {
        File outputFile = new File(settingsService.getOutboundQueue() +
                System.currentTimeMillis() + "-" + event.getEventType() + "-" + event.getPersonName() + ".json");
        return outputFile;
    }
}
