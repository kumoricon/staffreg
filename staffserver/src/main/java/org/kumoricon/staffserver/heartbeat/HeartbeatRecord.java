package org.kumoricon.staffserver.heartbeat;

import org.kumoricon.staff.dto.HeartbeatRequest;

import java.time.Instant;

public class HeartbeatRecord {
    private String clientID;
    private String clientUsername;
    private String clientMachineName;
    private String authUsername;
    private Long workQueueCount;
    private Long outboundQueueCount;
    private Instant receivedAt;
    private String clientIpAddress;

    public HeartbeatRecord(HeartbeatRequest request, String authUsername, String clientIpAddress, Instant receivedAt) {
        this.clientID = request.getClientId();
        this.clientUsername = request.getUsername();
        this.clientMachineName = request.getMachineName();
        this.workQueueCount = request.getWorkQueueCount();
        this.outboundQueueCount = request.getOutboundQueueCount();
        this.authUsername = authUsername;
        this.clientIpAddress = clientIpAddress;
        this.receivedAt = receivedAt;
    }

    public String getClientID() { return clientID; }

    public String getClientUsername() {
        return clientUsername;
    }

    public String getClientMachineName() {
        return clientMachineName;
    }

    public String getAuthUsername() {
        return authUsername;
    }

    public Long getWorkQueueCount() {
        return workQueueCount;
    }

    public Long getOutboundQueueCount() {
        return outboundQueueCount;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    @Override
    public String toString() {
        return "HeartbeatRecord{" +
                "clientID='" + clientID + '\'' +
                ", clientUsername='" + clientUsername + '\'' +
                ", clientMachineName='" + clientMachineName + '\'' +
                ", workQueueCount='" + workQueueCount + '\'' +
                ", outboundQueueCount='" + outboundQueueCount + '\'' +
                ", receivedAt=" + receivedAt +
                '}';
    }
}
