package org.kumoricon.staff.client.loginscreen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordChangeResponse {
    private final Boolean success;
    private final String newPassword;

    @JsonCreator
    public PasswordChangeResponse(
            @JsonProperty(value = "success", required = true) Boolean success,
            @JsonProperty(value = "newPassword", required = true) String newPassword) {
        this.success = success;
        this.newPassword = newPassword;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
