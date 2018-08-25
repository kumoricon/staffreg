package org.kumoricon.staff.client.loginscreen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    private String username;
    private boolean success;
    private boolean passwordChangeRequired;
    private long timestamp;

    @JsonCreator
    public LoginResponse(
            @JsonProperty(value = "username", required = true) String username,
            @JsonProperty(value = "success", required = true) boolean success,
            @JsonProperty(value = "passwordChangeRequired", required = true) boolean passwordChangeRequired) {
        this.username = username;
        this.success = success;
        this.passwordChangeRequired = passwordChangeRequired;
        this.timestamp = System.currentTimeMillis();
    }

    public String getUsername() {
        return username;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "username='" + username + '\'' +
                ", success=" + success +
                ", passwordChangeRequired=" + passwordChangeRequired +
                ", timestamp=" + timestamp +
                '}';
    }
}
