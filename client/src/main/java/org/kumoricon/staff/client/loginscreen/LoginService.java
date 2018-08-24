package org.kumoricon.staff.client.loginscreen;

import org.kumoricon.staff.client.SessionService;
import org.kumoricon.staff.client.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDateTime;

public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @Inject
    private SettingsService settingsService;

    @Inject
    private SessionService sessionService;

    @PostConstruct
    public void init() {
        log.info("Login service initialized");
        log.info(sessionService.getHostname() + " - " + settingsService.getClientId());
    }

    public boolean login(String username, String password, String serverURL) {
        // If login successful:
        log.info(username + " logged in successfully");
        sessionService.setUsername(username);
        sessionService.setPassword(password);
        sessionService.setServerHostname(serverURL);
        sessionService.setLoggedIn(true);
        return true;
    }

}
