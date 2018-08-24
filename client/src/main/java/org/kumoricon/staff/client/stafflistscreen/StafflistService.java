package org.kumoricon.staff.client.stafflistscreen;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.kumoricon.staff.client.SettingsService;
import org.kumoricon.staff.client.model.Staff;
import org.kumoricon.staff.client.model.StaffImportData;
import org.kumoricon.staff.client.model.StaffImportFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
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

    @Inject
    private SettingsService settings;

    @PostConstruct
    public void init() {
        log.info("Stafflist service initialized");
        loadDataFromFileAsync();
        startDeleter();
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
                File inputFile = new File(settings.getBasePath() + "staffdata.json");
                log.info("Loading staff from " + inputFile);

                List<Staff> staffToAdd = new ArrayList<>();
                try {
                    StaffImportFile myObjects = mapper.readValue(inputFile, StaffImportFile.class);
                    log.info("Found {} staff to import", myObjects.getPersons().size());
                    int q = 0;
                    for (StaffImportData person : myObjects.getPersons()) {
                        if (q < 30) {
                            Staff s = person.toStaff();
                            staffToAdd.add(s);
                        }
                        q++;
                    }
                    staffObservableList.addAll(staffToAdd);         // Adding items one at a time to the ObserableList
                                                                    // would cause duplicates to show up until an item
                                                                    // was selected. Adding all at once seems to prevent
                                                                    // this.
                } catch (IOException ex) {
                    log.warn("Couldn't load data from " + inputFile, ex);
                } catch (Exception ex) {
                    log.error("Error loading data file", ex);
                }

                if (staffObservableList.size() == 0) {
                    log.info("Loading dummy staff instead");
                    staffObservableList.add(new Staff("Some", "Dude","Department of Awesome", "L"));
                    staffObservableList.add(new Staff("Other", "Guy", "Department of Things", "M"));
                    staffObservableList.add(new Staff("Alice", "Anderson", "Party People", "S"));
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
