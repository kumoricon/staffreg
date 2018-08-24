package org.kumoricon.staff.client.heartbeat;

public class HeartbeatMessage {
    private String clientId;
    private String username;
    private String machineName;
    private Long workQueueCount;
    private Long outboundQueueCount;

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Long getWorkQueueCount() {
        return workQueueCount;
    }

    public void setWorkQueueCount(Long workQueueCount) {
        this.workQueueCount = workQueueCount;
    }

    public Long getOutboundQueueCount() {
        return outboundQueueCount;
    }

    public void setOutboundQueueCount(Long outboundQueueCount) {
        this.outboundQueueCount = outboundQueueCount;
    }

}
