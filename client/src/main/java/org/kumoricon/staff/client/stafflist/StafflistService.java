package org.kumoricon.staff.client.stafflist;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import org.kumoricon.staff.client.model.Staff;

import javax.annotation.PostConstruct;

public class StafflistService {
    private final ObservableList<Staff> staffObservableList = new SimpleListProperty<>();

    @PostConstruct
    public void init() {
        System.out.println("Stafflist service initialized");
    }


    public ObservableList<Staff> getStaffObservableList() {
        return staffObservableList;
    }

}
