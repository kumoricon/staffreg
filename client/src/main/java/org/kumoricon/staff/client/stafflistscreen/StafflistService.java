package org.kumoricon.staff.client.stafflistscreen;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.kumoricon.staff.client.SettingsService;
import org.kumoricon.staff.client.WorkingDirectoryHelper;
import org.kumoricon.staff.client.model.Staff;
import org.kumoricon.staff.client.model.StaffImportData;
import org.kumoricon.staff.client.model.StaffImportFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class StafflistService {
    private final ObservableList<Staff> staffObservableList = FXCollections.observableArrayList();
    private final ObservableMap<UUID, Staff> staffObservableMap = new SimpleMapProperty<>();
    private static final Logger log = LoggerFactory.getLogger(StafflistService.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    private SettingsService settings;

    @PostConstruct
    public void init() {
        log.info("Stafflist service initialized");
        loadDataFromFile();

        if (staffObservableList.size() == 0) {
            staffObservableList.add(new Staff("Some", "Dude","Department of Awesome", "L"));
            staffObservableList.add(new Staff("Other", "Guy", "Department of Things", "M"));
            staffObservableList.add(new Staff("Alice", "Anderson", "Party People", "S"));
        }
    }

    private void loadDataFromFile() {
        File inputFile = new File(settings.getBasePath() + "staffdata.json");
        List<Staff> staffList;
        try {
            StaffImportFile myObjects = mapper.readValue(inputFile, StaffImportFile.class);

            log.info("Found {} staff to import", myObjects.getPersons().size());

            for (StaffImportData person : myObjects.getPersons()) {
                Staff s = person.toStaff();
                staffObservableList.add(s);
            }
        } catch (IOException ex) {
            log.warn("Couldn't load data from " + inputFile, ex);
        } catch (Exception ex) {
            log.error("Error loading data file", ex);
        }
    }


    public ObservableList<Staff> getStaffObservableList() {
        return staffObservableList;
    }
}
