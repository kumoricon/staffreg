package org.kumoricon.staff.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonCreator
    public StaffEvent (
            @JsonProperty(value = "clientId", required = true) String clientId,
            @JsonProperty(value = "clientMachineName", required = true) String clientMachineName,
            @JsonProperty(value = "userId", required = true) String userId,
            @JsonProperty(value = "username", required = true) String username,
            @JsonProperty(value = "eventCreatedAt", required = true) Long eventCreatedAt,
            @JsonProperty(value = "personId", required = true) String personId,
            @JsonProperty(value = "personName", required = true) String personName,
            @JsonProperty(value = "eventType", required = true) EVENT_TYPE eventType) {
        this.clientId = clientId;
        this.clientMachineName = clientMachineName;
        this.userId = userId;
        this.username = username;
        this.eventCreatedAt = eventCreatedAt;
        this.personId = personId;
        this.personName = personName;
        this.eventType = eventType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getUserId() {
        return userId;
    }

    public Long getEventCreatedAt() {
        return eventCreatedAt;
    }

    public String getClientMachineName() {
        return clientMachineName;
    }

    public String getPersonId() {
        return personId;
    }

    public String getEventType() {
        return eventType.name();
    }

    public String getPersonName() {
        return personName;
    }

    public String getUsername() {
        return username;
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
