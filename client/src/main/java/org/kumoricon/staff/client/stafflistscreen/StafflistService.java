package org.kumoricon.staff.client.stafflistscreen;

import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.kumoricon.staff.client.model.Staff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.UUID;

public class StafflistService {
    private final ObservableList<Staff> staffObservableList = FXCollections.observableArrayList();
    private final ObservableMap<UUID, Staff> staffObservableMap = new SimpleMapProperty<>();
    private static final Logger log = LoggerFactory.getLogger(StafflistService.class);

    @PostConstruct
    public void init() {
        log.info("Stafflist service initialized");
    }






    public ObservableList<Staff> getStaffObservableList() {
        return staffObservableList;
    }
}
