package org.kumoricon.staff.dto;

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

    /**
     * Client-specific GUID. It should be unique between clients and persistent across
     * software invocations and reboots
     * @return GUID
     */
    public String getClientId() { return clientId; }

    /**
     * The user currently logged in to the StaffReg client
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * The client machine's hostname
     * @return String
     */
    public String getMachineName() {
        return machineName;
    }

    /**
     * Number of files in the working directory. Represents images for staff member currently open for
     * editing.
     * @return File count
     */
    public Long getWorkQueueCount() {
        return workQueueCount;
    }

    /**
     * Number of files in the outbound queue directory. Represents files that will be sent to the server
     * but haven't been sent successfully yet
     * @return File count
     */
    public Long getOutboundQueueCount() {
        return outboundQueueCount;
    }
}
