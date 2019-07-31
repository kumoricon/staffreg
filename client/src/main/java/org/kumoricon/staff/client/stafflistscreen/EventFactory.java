package org.kumoricon.staff.client.stafflistscreen;

import org.kumoricon.staff.client.SessionService;
import org.kumoricon.staff.client.SettingsService;
import org.kumoricon.staff.client.model.Staff;
import org.kumoricon.staff.dto.StaffEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class EventFactory {
    private static final Logger log = LoggerFactory.getLogger(EventFactory.class);

    @Inject
    SessionService sessionService;

    @Inject
    SettingsService settingsService;

    @PostConstruct
    public void init() {

    }

    public StaffEvent buildCheckInEvent(Staff staff) {
        return buildEvent(staff, StaffEvent.EVENT_TYPE.CHECK_IN);
    }

    public StaffEvent buildReprintEvent(Staff staff) {
        return buildEvent(staff, StaffEvent.EVENT_TYPE.REPRINT_BADGE);
    }

    private StaffEvent buildEvent(Staff staff, StaffEvent.EVENT_TYPE eventType) {
         return new StaffEvent(
                    settingsService.getClientId(),
                    sessionService.getHostname(),
                    sessionService.getUsername(),
                    sessionService.getUsername(),
                    System.currentTimeMillis(),
                    staff.getUuid(),
                    staff.getName(),
                    eventType);
    }

}
