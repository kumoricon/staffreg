package org.kumoricon.staffserver.event;

import java.time.Instant;

public class StaffEventRecord {
    private StaffEvent event;
    private Instant receivedAt;
    private String clientIpAddress;

    public StaffEventRecord(StaffEvent event, Instant receivedAt, String clientIpAddress) {
        this.event = event;
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

    @Override
    public String toString() {
        return "StaffEventRecord{" +
                "event=" + event +
                ", receivedAt=" + receivedAt +
                ", clientIpAddress='" + clientIpAddress + '\'' +
                '}';
    }
}
