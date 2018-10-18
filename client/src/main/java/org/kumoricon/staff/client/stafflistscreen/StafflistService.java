package org.kumoricon.staff.client.stafflistscreen;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.kumoricon.staff.client.SessionService;
import org.kumoricon.staff.client.SettingsService;
import org.kumoricon.staff.client.model.Staff;
import org.kumoricon.staff.dto.StaffResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StafflistService {
    private final ObservableList<Staff> staffObservableList = FXCollections.observableArrayList();
    private static final Logger log = LoggerFactory.getLogger(StafflistService.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private AtomicInteger i = new AtomicInteger(0);
    private static final String STAFF_URI_PATH = "/staff";

    @Inject
    private SettingsService settings;

    @Inject
    private SessionService sessionService;

    @PostConstruct
    public void init() {
        log.info("Stafflist service initialized");
        startDownloader();
        mapper.registerModule(new JavaTimeModule());
    }

    public void startDownloader() {
        ScheduledService<Void> svc = new ScheduledService<Void>() {
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    protected Void call() throws IOException {
                        if (!sessionService.isLoggedIn() || sessionService.isPasswordChangeRequired()) { return null; }
                        HttpResponse response =
                                Request.Get(sessionService.getServerHostname() + STAFF_URI_PATH)
                                        .addHeader(HttpHeaders.AUTHORIZATION, sessionService.getHttpAuthHeader())
                                        .execute()
                                        .returnResponse();
                        if (response.getStatusLine().getStatusCode() != 200) {
                            throw new RuntimeException("HTTP status " + response.getStatusLine().getStatusCode());
                        }

                        Map<String, Staff> currentStaffMap = new HashMap<>();
                        staffObservableList.forEach(current -> {
                            currentStaffMap.put(current.getUuid(), current);
                        });

                        List<Staff> staffToAdd = new ArrayList<>();
                        try {
                            List<StaffResponse> staffToImport = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<StaffResponse>>(){});

                            EntityUtils.consume(response.getEntity());
                            log.info("Found {} staff to import", staffToImport.size());
                            for (StaffResponse incomingRecord : staffToImport) {
                                if (incomingRecord != null) {
                                    Staff incoming = Staff.fromStaffResponse(incomingRecord);
                                    Staff existing = currentStaffMap.get(incoming.getUuid());
                                    if (existing == null) {
                                        staffToAdd.add(incoming);
                                    } else if (existing.getLastModifiedAt() < incoming.getLastModifiedAt()) {
                                        staffToAdd.add(incoming);
                                        log.info("Existing record for {} is oudtated. Old timestamp: {} New timestamp: {}", incoming, existing.getLastModifiedAt(), incoming.getLastModifiedAt());
                                    } else {
                                        staffToAdd.add(existing);
                                    }
                                }
                            }

                            staffObservableList.setAll(staffToAdd);
                            // Adding items one at a time to the ObserableList
                            // would cause duplicates to show up until an item
                            // was selected. Adding all at once seems to prevent
                            // this.
                        } catch (Exception ex) {
                            log.error("Error loading staff", ex);
                        }

                        return null;
                    }
                };
            }
        };
        svc.setPeriod(Duration.seconds(15));
        svc.setRestartOnFailure(true);
        svc.setMaximumCumulativePeriod(Duration.seconds(10));
        svc.start();
    }


    public ObservableList<Staff> getStaffObservableList() {
        return staffObservableList;
    }
}
