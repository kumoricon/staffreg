package org.kumoricon.staffserver.staff;


import org.kumoricon.staff.dto.StaffResponse;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String uuid;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String legalFirstName;
    private String legalLastName;
    private String shirtSize;
    @NotNull
    private String department;
    @NotNull
    private String position;
    private Boolean suppressPrintingDepartment;
    private Boolean checkedIn;
    private LocalDate birthDate;
    private Boolean deleted;
    private Instant checkedInAt;
    @NotNull
    private Long lastModifiedMS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLegalFirstName() {
        return legalFirstName;
    }

    public void setLegalFirstName(String legalFirstName) {
        this.legalFirstName = legalFirstName;
    }

    public String getLegalLastName() {
        return legalLastName;
    }

    public void setLegalLastName(String legalLastName) {
        this.legalLastName = legalLastName;
    }

    public String getShirtSize() {
        return shirtSize;
    }

    public void setShirtSize(String shirtSize) {
        this.shirtSize = shirtSize;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Boolean getSuppressPrintingDepartment() {
        return suppressPrintingDepartment;
    }

    public void setSuppressPrintingDepartment(Boolean suppressPrintingDepartment) {
        this.suppressPrintingDepartment = suppressPrintingDepartment;
    }

    public Boolean getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(Instant checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public Long getLastModifiedMS() {
        return lastModifiedMS;
    }

    public void setLastModifiedMS(Long lastModifiedMS) {
        this.lastModifiedMS = lastModifiedMS;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModifiedMS = lastModified.toEpochMilli();
    }

    public StaffResponse toStaffResponse() {
        return new StaffResponse(uuid,
                firstName,
                lastName,
                legalFirstName,
                legalLastName,
                shirtSize,
                department,
                checkedIn,
                birthDate,
                deleted,
                checkedInAt,
                Instant.ofEpochMilli(lastModifiedMS));
    }
}
