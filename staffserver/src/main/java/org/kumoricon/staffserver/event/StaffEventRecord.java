package org.kumoricon.staffserver.event;

import org.kumoricon.staff.dto.StaffEvent;

import java.time.Instant;

public class StaffEventRecord {
    private StaffEvent event;
    private Instant receivedAt;
    private String clientIpAddress;
    private String authenticatedUser;

    public StaffEventRecord(StaffEvent event, String authenticatedUser, Instant receivedAt, String clientIpAddress) {
        this.event = event;
        this.authenticatedUser = authenticatedUser;
        this.receivedAt = receivedAt;
        this.clientIpAddress = clientIpAddress;
    }

    public StaffEvent getEvent() {
        return event;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public String getAuthenticatedUser() {
        return authenticatedUser;
    }

    @Override
    public String toString() {
        return "StaffEventRecord{" +
                "event=" + event +
                ", receivedAt=" + receivedAt +
                ", clientIpAddress='" + clientIpAddress + '\'' +
                ", authenticatedUser='" + authenticatedUser + '\'' +
                '}';
    }
}
