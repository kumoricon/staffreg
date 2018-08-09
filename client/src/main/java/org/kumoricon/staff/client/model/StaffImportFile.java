package org.kumoricon.staff.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StaffImportFile {
    List<StaffImportData> persons;
    Long detailsVersion;

    public List<StaffImportData> getPersons() {
        return persons;
    }

    public void setPersons(List<StaffImportData> persons) {
        this.persons = persons;
    }

    public Long getDetailsVersion() {
        return detailsVersion;
    }

    public void setDetailsVersion(Long detailsVersion) {
        this.detailsVersion = detailsVersion;
    }
}
