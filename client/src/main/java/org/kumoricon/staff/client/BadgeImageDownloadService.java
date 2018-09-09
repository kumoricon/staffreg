package org.kumoricon.staff.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class BadgeImageDownloadService {
    private static final Logger log = LoggerFactory.getLogger(BadgeImageDownloadService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BADGEIMAGE_DOWNLOAD_URI_PATH = "/badgeImages";

    private final Deque<String> imagesToDownload = new ArrayDeque<>();
    private final AtomicLong lastDownloadTimeMS = new AtomicLong(0);

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
                        if (sessionService.getServerHostname() == null || sessionService.getUsername() == null || sessionService.getPassword() == null) return null;
                        long downloadStart = System.currentTimeMillis();
                        String imagesListUrl = sessionService.getServerHostname() + BADGEIMAGE_DOWNLOAD_URI_PATH + "?after=" + lastDownloadTimeMS.get();

                        HttpResponse response;
                        List<String> filenames = new ArrayList<>();
                        try {
                            response = Request.Get(imagesListUrl)
                                    .addHeader(HttpHeaders.AUTHORIZATION, sessionService.getHttpAuthHeader())
                                    .execute()
                                    .returnResponse();
                            if (response != null) {
                                filenames = objectMapper.readValue(response.getEntity().getContent(), TypeFactory.defaultInstance().constructCollectionType(List.class, String.class));
                            }
                        } catch (IOException ex) {
                            log.error("Error getting badge image file list", ex);
                        }

                        imagesToDownload.addAll(filenames);
                        if (filenames.size() > 0) {
                            lastDownloadTimeMS.set(downloadStart);
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

    @PostConstruct
    public void initImageDownloader() {
        ScheduledService<Void> svc = new ScheduledService<Void>() {
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    protected Void call() {
                        if (sessionService.getServerHostname() == null || sessionService.getUsername() == null || sessionService.getPassword() == null) return null;
                        Path badgeImageDir = Paths.get(settingsService.getBadgeImagePath());
                        List<String> successfullyDownloaded = new ArrayList<>();

                        int toDownloadCount = imagesToDownload.size();
                        for (String filename : imagesToDownload) {
                            String url = null;
                            try {
                                url = sessionService.getServerHostname() + BADGEIMAGE_DOWNLOAD_URI_PATH + "/" + URLEncoder.encode(filename, "UTF8");
                            } catch (UnsupportedEncodingException e) {
                                log.error("Platform doesn't support character encoding UTF8");
                                return null;
                            }
                            HttpResponse response;
                            try {
                                response = Request.Get(url)
                                        .addHeader(HttpHeaders.AUTHORIZATION, sessionService.getHttpAuthHeader())
                                        .execute()
                                        .returnResponse();
                                if (response != null) {
                                    try (InputStream is = response.getEntity().getContent()){
                                        File outputFile = new File(badgeImageDir.toString(), filename);
                                        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                                            int inByte;
                                            while((inByte = is.read()) != -1)
                                                fos.write(inByte);
                                            successfullyDownloaded.add(filename);
                                        }
                                    }
                                }
                            } catch (IOException ex) {
                                log.error("Error getting badge image file list", ex);
                            }
                        }

                        for (String success : successfullyDownloaded) {
                            imagesToDownload.remove(success);
                        }

                        log.info("Downloaded {}/{} badge images successfully", successfullyDownloaded.size(), toDownloadCount);

                        return null;
                    }
                };
            }
        };
        svc.setPeriod(Duration.seconds(5));
        svc.setRestartOnFailure(true);
        svc.setMaximumCumulativePeriod(Duration.minutes(1));
        svc.start();
    }
}
