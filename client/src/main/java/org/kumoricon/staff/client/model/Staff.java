package org.kumoricon.staff.client.model;

import javafx.beans.property.*;
import org.kumoricon.staff.dto.StaffResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@SuppressWarnings("unused")
public class Staff {
    private StringProperty firstName = new SimpleStringProperty();
    private StringProperty lastName = new SimpleStringProperty();
    private StringProperty legalFirstName = new SimpleStringProperty();
    private StringProperty legalLastName = new SimpleStringProperty();
    private StringProperty shirtSize = new SimpleStringProperty();
    private StringProperty department = new SimpleStringProperty();
    private String uuid;        // IDs may be numbers for now, GUIDs later
    private BooleanProperty checkedIn = new SimpleBooleanProperty();
    private BooleanProperty picture1Saved = new SimpleBooleanProperty();
    private BooleanProperty signatureSaved = new SimpleBooleanProperty();
    private ObjectProperty<LocalDate> birthDate = new SimpleObjectProperty<>();
    private BooleanProperty badgePrinted = new SimpleBooleanProperty();
    private BooleanProperty deleted = new SimpleBooleanProperty();
    private BooleanProperty suppressPrintingDepartment = new SimpleBooleanProperty();
    private Instant checkedInAt;

    private Staff() {}

    public Staff(String firstName, String lastName, String department, String shirtSize) {
        this.uuid = UUID.randomUUID().toString();
        setFirstName(firstName);
        setLastName(lastName);
        this.department.setValue(department);
        this.shirtSize.setValue(shirtSize);
        this.checkedIn.setValue(false);
        this.picture1Saved.setValue(false);
        this.signatureSaved.setValue(false);
        this.badgePrinted.setValue(false);
        this.deleted.setValue(false);
        this.birthDate.setValue(LocalDate.of(2010, 1, 1));
        this.suppressPrintingDepartment.setValue(false);
        this.badgePrinted.setValue(false);
    }

    public static Staff fromStaffResponse(StaffResponse person) {
        if (person == null) { return null; }
        Staff s = new Staff();
        s.setUuid(person.getId());
        s.setFirstName(person.getFirstName());
        s.setLastName(person.getLastName());
        s.setLegalFirstName(person.getLegalFirstName());
        s.setLegalLastName(person.getLegalLastName());
        s.setBirthDate(person.getBirthDate());
        s.setCheckedIn(person.getCheckedIn());
        s.setCheckedInAt(person.getCheckedInAt());
        s.setDeleted(person.getDeleted());
        s.setShirtSize(person.getShirtSize());
        s.setDepartment(person.getDepartment());
        s.setBadgePrinted(person.getBadgePrinted());
        s.setSuppressPrintingDepartment(person.getSuppressPrintingDepartment());
        return s;
    }

    /**
     * Returns the preferred name of this person. If either the first or last name is empty, will
     * return just the other name without an extra space.
     * @return Full name
     */
    public String getName() {
        String fName = firstName.getValue().trim();
        String lName = lastName.getValue().trim();
        return combineNames(fName, lName);
    }

    public String getFirstName() {
        return firstName.getValue();
    }

    public void setFirstName(String firstName) {
        this.firstName.setValue(sanitzieName(firstName));
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty shirtSizeProperty() {
        return shirtSize;
    }

    public StringProperty departmentProperty() {
        return department;
    }

    public boolean isCheckedIn() {
        return checkedIn.get();
    }

    public BooleanProperty checkedInProperty() {
        return checkedIn;
    }

    public BooleanProperty picture1SavedProperty() {
        return picture1Saved;
    }

    public BooleanProperty signatureSavedProperty() {
        return signatureSaved;
    }

    public String getLastName() {
        return lastName.getValue();
    }

    public void setLastName(String lastName) {
        this.lastName.setValue(sanitzieName(lastName));
    }

    public String getShirtSize() {
        return shirtSize.getValue();
    }

    public void setShirtSize(String shirtSize) {
        this.shirtSize.setValue(shirtSize);
    }

    public String getDepartment() {
        return department.getValue();
    }

    public void setDepartment(String department) {
        this.department.setValue(department);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getCheckedIn() {
        return checkedIn.getValue();
    }

    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn.setValue(checkedIn);
        if (checkedInAt == null && checkedIn) {
            checkedInAt = Instant.now();
        } else if (!checkedIn) {
            checkedInAt = null;
        }

    }

    public boolean isPicture1Saved() {
        return picture1Saved.getValue();
    }

    public void setPicture1Saved(boolean picture1Saved) {
        this.picture1Saved.setValue(picture1Saved);
    }

    public boolean isSignatureSaved() {
        return signatureSaved.getValue();
    }

    public void setSignatureSaved(boolean signatureSaved) {
        this.signatureSaved.setValue(signatureSaved);
    }

    public Instant getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(Instant checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public String getLegalFirstName() {
        return legalFirstName.get();
    }

    public StringProperty legalFirstNameProperty() {
        return legalFirstName;
    }

    public void setLegalFirstName(String legalFirstName) {
        this.legalFirstName.set(sanitzieName(legalFirstName));
    }

    public String getLegalLastName() {
        return legalLastName.get();
    }

    public StringProperty legalLastNameProperty() {
        return legalLastName;
    }

    public void setLegalLastName(String legalLastName) {
        this.legalLastName.set(sanitzieName(legalLastName));
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn.set(checkedIn);
    }

    public LocalDate getBirthDate() {
        return birthDate.get();
    }

    public ObjectProperty<LocalDate> birthDateProperty() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate.set(birthDate);
    }

    public boolean isBadgePrinted() {
        return badgePrinted.get();
    }

    public BooleanProperty badgePrintedProperty() {
        return badgePrinted;
    }

    public void setBadgePrinted(boolean badgePrinted) {
        this.badgePrinted.set(badgePrinted);
    }

    public boolean isDeleted() {
        return deleted.get();
    }

    public BooleanProperty deletedProperty() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted.set(deleted);
    }

    public boolean getSuppressPrintingDepartment() {
        return suppressPrintingDepartment.get();
    }

    public BooleanProperty suppressPrintingDepartmentProperty() {
        return suppressPrintingDepartment;
    }

    public void setSuppressPrintingDepartment(boolean suppressPrintingDepartment) {
        this.suppressPrintingDepartment.set(suppressPrintingDepartment);
    }

    @Override
    public String toString() {
        return "Staff{" +
                "firstName='" + firstName.getValue() + '\'' +
                ", lastName='" + lastName.getValue() + '\'' +
                ", uuid=" + uuid +
                '}';
    }

    /**
     * Builds an identifier for this record that could be used as part of a filename
     * (IE, should be safe to use in common filesystems). It should contain only A-Z, a-z, 0-9, underscore, and
     * dash
     * @return String
     */
    public String getFilename() {
        String filename = firstName.getValue().toLowerCase().trim() +
                " " +
                lastName.getValue().toLowerCase().trim() +
                " " +
                uuid;
        return filename.replaceAll("[^A-Za-z0-9 _-]", "").replace(' ', '_');
    }

    public String getLegalName() {
        return combineNames(legalFirstName.getValue(), legalLastName.getValue());
    }

    static String combineNames(String first, String last) {
        String separator = " ";

        if (first.isEmpty() || last.isEmpty()) {
            separator = "";
        }

        return first + separator + last;
    }

    private static String sanitzieName(String name) {
        if (name == null) return "";
        return name.trim();
    }

    public Integer getCurrentAge() {
        if (birthDate.getValue() != null) {
            return Period.between(birthDate.getValue(), LocalDate.now()).getYears();
        } else {
            return 0;
        }
    }
}
