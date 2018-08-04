package org.kumoricon.staff.client.stafflistscreen;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.kumoricon.staff.client.model.Staff;

import javax.annotation.PostConstruct;
import java.util.UUID;

public class StafflistService {
    private final ObservableMap<UUID, Staff> staffObservableMap = new SimpleMapProperty<>();

    @PostConstruct
    public void init() {
        System.out.println("Stafflist service initialized");
    }


    public ObservableMap<UUID, Staff> getStaffObservableMap() {
        return staffObservableMap;
    }

}
