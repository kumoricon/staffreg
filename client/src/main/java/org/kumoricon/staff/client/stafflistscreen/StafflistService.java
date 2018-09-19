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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class StafflistService {
    private final ObservableList<Staff> staffObservableList = FXCollections.observableArrayList();
    private final ObservableMap<UUID, Staff> staffObservableMap = new SimpleMapProperty<>();
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
        loadDataFromFileAsync();
        mapper.registerModule(new JavaTimeModule());
    }

    public void startDeleter() {
        ScheduledService<String> svc = new ScheduledService<String>() {
            protected Task<String> createTask() {
                return new Task<String>() {
                    protected String call() {
                        int x = i.getAndIncrement();
                        if (x < staffObservableList.size()) {
                            staffObservableList.get(x).setCheckedIn(true);
                        }
                        return null;
                    }
                };
            }
        };
        svc.setPeriod(Duration.millis(500));
        svc.setRestartOnFailure(true);
        svc.setMaximumCumulativePeriod(Duration.seconds(10));
        svc.start();
    }

    private void loadDataFromFileAsync() {
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                HttpResponse response =
                        Request.Get(sessionService.getServerHostname() + STAFF_URI_PATH)
                                .addHeader(HttpHeaders.AUTHORIZATION, sessionService.getHttpAuthHeader())
                                .execute()
                                .returnResponse();
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException("HTTP status " + response.getStatusLine().getStatusCode());
                }


                List<Staff> staffToAdd = new ArrayList<>();
                try {
                    List<StaffResponse> staffToImport = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<StaffResponse>>(){});

                    EntityUtils.consume(response.getEntity());
                    log.info("Found {} staff to import", staffToImport.size());
                    for (StaffResponse person : staffToImport) {
                        if (person != null) {
                            Staff s = Staff.fromStaffResponse(person);
                            staffToAdd.add(s);
                        }
                    }
                    staffObservableList.setAll(staffToAdd);         // Adding items one at a time to the ObserableList
                    // would cause duplicates to show up until an item
                    // was selected. Adding all at once seems to prevent
                    // this.
                    } catch (Exception ex) {
                    log.error("Error loading staff", ex);
                }

                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public ObservableList<Staff> getStaffObservableList() {
        return staffObservableList;
    }
}
