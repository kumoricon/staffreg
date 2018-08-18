package org.kumoricon.staff.client.model;

import static org.junit.Assert.*;

public class StaffTest {

    @org.junit.Test
    public void namesGetTrimmed() {
        Staff s = new Staff(" ", " ", " ", "L");
        s.setLegalFirstName(" ");
        s.setLegalLastName(" ");

        assertEquals("", s.getFirstName());
        assertEquals("", s.getLastName());
        assertEquals("", s.getLegalFirstName());
        assertEquals("", s.getLegalLastName());
    }
}