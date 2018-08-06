package org.kumoricon.staff.client.loginscreen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @PostConstruct
    public void init() {
        log.info("Login service initialized");
    }

    public boolean login(String username, String password, String service) {
        return true;
    }

}
