package org.kumoricon.staff.dto;

import java.util.List;

/**
 * Used to output partial user information
 */
public class UserResponse {
    private String username;
    private boolean enabled;
    private List<String> roles;

    public UserResponse(String username, boolean enabled, List<String> roles) {
        this.username = username;
        this.enabled = enabled;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<String> getRoles() {
        return roles;
    }
}
