package org.kumoricon.staffserver.login;

@SuppressWarnings(value = "unused")
public class LoginResponse {

    private final String username;
    private final Boolean success;
    private final Boolean passwordChangeRequired;
    private long timestamp;

    LoginResponse(String username, Boolean success, Boolean passwordChangeRequired) {
        this.username = username;
        this.success = success;
        this.timestamp = System.currentTimeMillis();
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getSuccess() {
        return success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Boolean getPasswordChangeRequired() { return passwordChangeRequired; }
}