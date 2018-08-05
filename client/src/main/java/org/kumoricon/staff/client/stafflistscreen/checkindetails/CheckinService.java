package org.kumoricon.staff.client.stafflistscreen.checkindetails;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import org.kumoricon.staff.client.model.Staff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

public class CheckinService {
    private final ObservableList<Staff> staffObservableList = new SimpleListProperty<>();
    private static final Logger log = LoggerFactory.getLogger(CheckinService.class);

    @PostConstruct
    public void init() {
        log.info("Stafflist service initialized");
    }


    public ObservableList<Staff> getStaffObservableList() {
        return staffObservableList;
    }

}
