package org.kumoricon.staff.client.loginscreen;

import javax.annotation.PostConstruct;

public class LoginService {

    @PostConstruct
    public void init() {
        System.out.println("Login service initialized");
    }

    public boolean login(String username, String password, String service) {
        return true;
    }

}
