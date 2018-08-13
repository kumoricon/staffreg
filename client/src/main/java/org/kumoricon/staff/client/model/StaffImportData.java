package org.kumoricon.staff.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StaffImportData {
    private String id;
    private String namePreferredFirst;
    private String namePreferredLast;
    private String nameOnIdFirst;
    private String nameOnIdLast;
    private String birthdate;
    private String tShirtSize;
    private List<Position> positions;
    private Boolean hasBadgeImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamePreferredFirst() {
        return namePreferredFirst;
    }

    public void setNamePreferredFirst(String namePreferredFirst) {
        this.namePreferredFirst = namePreferredFirst;
    }

    public String getNamePreferredLast() {
        return namePreferredLast;
    }

    public void setNamePreferredLast(String namePreferredLast) {
        this.namePreferredLast = namePreferredLast;
    }

    public String getNameOnIdFirst() {
        return nameOnIdFirst;
    }

    public void setNameOnIdFirst(String nameOnIdFirst) {
        this.nameOnIdFirst = nameOnIdFirst;
    }

    public String getNameOnIdLast() {
        return nameOnIdLast;
    }

    public void setNameOnIdLast(String nameOnIdLast) {
        this.nameOnIdLast = nameOnIdLast;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String gettShirtSize() {
        return tShirtSize;
    }

    public void settShirtSize(String tShirtSize) {
        this.tShirtSize = tShirtSize;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public Boolean getHasBadgeImage() {
        return hasBadgeImage;
    }

    public void setHasBadgeImage(Boolean hasBadgeImage) {
        this.hasBadgeImage = hasBadgeImage;
    }

    public Staff toStaff() {
        Staff s = new Staff(namePreferredFirst, namePreferredLast, positions.get(0).department, tShirtSize);
        s.setUuid(id);
        s.setLegalFirstName(nameOnIdFirst);
        s.setLegalLastName(nameOnIdLast);
        try {
            s.setBirthDate(LocalDate.parse(birthdate));
        } catch (DateTimeException ex) {
            s.setBirthDate(LocalDate.now());
        }
        return s;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Position {
        private String title;
        private String rank;
        private String department;
        private Boolean departmentSupressed;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public Boolean getDepartmentSupressed() {
            return departmentSupressed;
        }

        public void setDepartmentSupressed(Boolean departmentSupressed) {
            this.departmentSupressed = departmentSupressed;
        }
    }
}
