package org.kumoricon.staffserver.staff;


import org.hibernate.annotations.ColumnDefault;
import org.kumoricon.staff.dto.StaffResponse;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

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
    private String departmentColorCode;

    @ElementCollection
    @CollectionTable(name="staff_positions", joinColumns=@JoinColumn(name="id"))
    private List<String> positions;
    @NotNull
    private Boolean suppressPrintingDepartment;
    private Boolean checkedIn;
    private LocalDate birthDate;
    private String ageCategoryAtCon;
    private Boolean deleted;
    @NotNull
    private Boolean hasBadgeImage;
    private String badgeImageFileType;
    private Instant checkedInAt;
    @NotNull
    private Long lastModifiedMS;
    @NotNull
    private Boolean badgePrinted;
    @NotNull
    @ColumnDefault("0")
    private Integer badgePrintCount;

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

    public String getDepartmentColorCode() {
        return departmentColorCode;
    }

    public void setDepartmentColorCode(String departmentColorCode) {
        this.departmentColorCode = departmentColorCode;
    }

    public List<String> getPositions() {
        return positions;
    }

    public Boolean getHasBadgeImage() {
        return hasBadgeImage;
    }

    public void setHasBadgeImage(Boolean hasBadgeImage) {
        this.hasBadgeImage = hasBadgeImage;
    }

    public String[] getPositionsArray() {
        String[] positionsArray = new String[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsArray[i] = positions.get(i);
        }
        return positionsArray;
    }

    public void setPositions(List<String> positions) {
        this.positions = positions;
    }

    public String getAgeCategoryAtCon() {
        return ageCategoryAtCon;
    }

    public void setAgeCategoryAtCon(String ageCategoryAtCon) {
        this.ageCategoryAtCon = ageCategoryAtCon;
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

    public String getBadgeImageFileType() {
        return badgeImageFileType;
    }

    public void setBadgeImageFileType(String badgeImageFileType) {
        this.badgeImageFileType = badgeImageFileType;
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

    public Boolean getBadgePrinted() {
        return badgePrinted;
    }

    public void setBadgePrinted(Boolean badgePrinted) {
        this.badgePrinted = badgePrinted;
    }

    public Integer getBadgePrintCount() {
        return badgePrintCount;
    }

    public void setBadgePrintCount(Integer badgePrintCount) {
        this.badgePrintCount = badgePrintCount;
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
                Instant.ofEpochMilli(lastModifiedMS),
                badgePrinted,
                suppressPrintingDepartment);
    }
}
