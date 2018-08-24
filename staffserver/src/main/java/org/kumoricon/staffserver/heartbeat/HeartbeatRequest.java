package org.kumoricon.staffserver.heartbeat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class HeartbeatRequest {

    @JsonCreator
    public HeartbeatRequest(
            @JsonProperty(value = "clientId", required = true) String clientId,
            @JsonProperty(value = "username", required = true) String username,
            @JsonProperty(value = "machineName", required = true) String machineName,
            @JsonProperty(value = "workQueueCount", required = true) Long workQueueCount,
            @JsonProperty(value = "outboundQueueCount", required = true) Long outboundQueueCount) {
        this.clientId = clientId;
        this.username = username;
        this.machineName = machineName;
        this.workQueueCount = workQueueCount;
        this.outboundQueueCount = outboundQueueCount;
    }

    private String clientId;
    private String username;
    private String machineName;
    private Long workQueueCount;
    private Long outboundQueueCount;

    public String getClientId() { return clientId; }

    public String getUsername() {
        return username;
    }

    public String getMachineName() {
        return machineName;
    }

    public Long getWorkQueueCount() {
        return workQueueCount;
    }

    public Long getOutboundQueueCount() {
        return outboundQueueCount;
    }
}
