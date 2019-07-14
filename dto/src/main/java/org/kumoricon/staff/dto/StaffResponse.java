package org.kumoricon.staff.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDate;

@SuppressWarnings(value = "unused")
public class StaffResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String legalFirstName;
    private String legalLastName;
    private String shirtSize;
    private String department;
    private Boolean checkedIn;
    private LocalDate birthDate;
    private Boolean deleted;
    private Instant checkedInAt;
    private Instant lastModifiedAt;
    private Boolean badgePrinted;
    private Boolean suppressPrintingDepartment;

    @JsonCreator
    public StaffResponse(
            @JsonProperty(value="id", required = true) String id,
            @JsonProperty(value="firstName", required = true) String firstName,
            @JsonProperty(value="lastName", required = true) String lastName,
            @JsonProperty(value="legalFirstName") String legalFirstName,
            @JsonProperty(value="legalLastName") String legalLastName,
            @JsonProperty(value="shirtSize") String shirtSize,
            @JsonProperty(value="department", required = true) String department,
            @JsonProperty(value="checkedIn") Boolean checkedIn,
            @JsonProperty(value="birthDate", required = true) LocalDate birthDate,
            @JsonProperty(value="deleted", defaultValue = "false") Boolean deleted,
            @JsonProperty(value="checkedInAt") Instant checkedInAt,
            @JsonProperty(value="lastModifiedAt") Instant lastModifiedAt,
            @JsonProperty(value="badgePrinted", defaultValue = "false") Boolean badgePrinted,
            @JsonProperty(value="suppressPrintingDepartment", defaultValue = "false") Boolean suppressPrintingDepartment
            ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.legalFirstName = legalFirstName == null ? firstName : legalFirstName;
        this.legalLastName = legalLastName == null ? lastName : legalLastName;
        this.shirtSize = shirtSize;
        this.department = department;
        this.id = id;
        this.checkedIn = checkedIn == null ? false : checkedIn;
        this.birthDate = birthDate;
        this.deleted = deleted == null ? false : deleted;
        this.checkedInAt = checkedInAt;
        this.lastModifiedAt = lastModifiedAt;
        this.badgePrinted = badgePrinted;
        this.suppressPrintingDepartment = suppressPrintingDepartment;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLegalFirstName() {
        return legalFirstName;
    }

    public String getLegalLastName() {
        return legalLastName;
    }

    public String getShirtSize() {
        return shirtSize;
    }

    public String getDepartment() {
        return department;
    }

    public Boolean getCheckedIn() {
        return checkedIn;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public Instant getCheckedInAt() {
        return checkedInAt;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public Boolean getBadgePrinted() {
        return badgePrinted;
    }

    public Boolean getSuppressPrintingDepartment() {
        return suppressPrintingDepartment;
    }
}
