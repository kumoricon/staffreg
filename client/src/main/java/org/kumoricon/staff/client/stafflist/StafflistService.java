package org.kumoricon.staff.client.stafflist;

import javax.annotation.PostConstruct;

public class StafflistService {

    @PostConstruct
    public void init() {
        System.out.println("Login service initialized");
    }

    public boolean login(String username, String password, String service) {
        return true;
    }

}
