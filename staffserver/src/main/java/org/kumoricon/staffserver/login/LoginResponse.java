package org.kumoricon.staffserver.login;

@SuppressWarnings(value = "unused")
public class LoginResponse {

    private final String username;
    private final Boolean success;
    private long timestamp;

    LoginResponse(String username, Boolean success) {
        this.username = username;
        this.success = success;
        this.timestamp = System.currentTimeMillis();
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
}