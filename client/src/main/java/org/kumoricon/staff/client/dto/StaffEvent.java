package org.kumoricon.staff.client.dto;

/**
 * Represents an event, done by a user, on a specific person
 */
public class StaffEvent {
    public enum EVENT_TYPE {CHECK_IN, REPRINT_BADGE}
    private String clientId;
    private String clientMachineName;
    private String userId;
    private String username;            // For debugging, use userId for record keeping
    private Long eventCreatedAt;
    private String personId;
    private String personName;          // For convenience, use personId for record keeping
    private EVENT_TYPE eventType;


    public StaffEvent() {
        eventCreatedAt = System.currentTimeMillis();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getEventCreatedAt() {
        return eventCreatedAt;
    }

    public void setEventCreatedAt(Long timestamp) {
        this.eventCreatedAt = timestamp;
    }

    public String getClientMachineName() {
        return clientMachineName;
    }

    public void setClientMachineName(String clientMachineName) {
        this.clientMachineName = clientMachineName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getEventType() {
        return eventType.name();
    }

    public void setEventType(EVENT_TYPE eventType) {
        this.eventType = eventType;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "StaffEvent{" +
                "eventCreatedAt=" + eventCreatedAt +
                ", eventType=" + eventType.name() + '\'' +
                ", personId='" + personId + '\'' +
                ", personName='" + personName + '\'' +
                '}';
    }
}
