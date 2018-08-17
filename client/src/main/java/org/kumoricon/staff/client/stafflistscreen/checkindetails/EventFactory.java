package org.kumoricon.staff.client.stafflistscreen.checkindetails;

import org.kumoricon.staff.client.SessionService;
import org.kumoricon.staff.client.SettingsService;
import org.kumoricon.staff.client.dto.StaffEvent;
import org.kumoricon.staff.client.model.Staff;
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
        StaffEvent event = buildEvent(staff);
        event.setEventType(StaffEvent.EVENT_TYPE.CHECK_IN);
        return event;
    }

    public StaffEvent buildReprintEvent(Staff staff) {
        StaffEvent event = buildEvent(staff);
        event.setEventType(StaffEvent.EVENT_TYPE.REPRINT_BADGE);
        return event;
    }

    private StaffEvent buildEvent(Staff staff) {
        StaffEvent staffEvent = new StaffEvent();
        staffEvent.setClientId(settingsService.getClientId());
        staffEvent.setUserId(sessionService.getUsername());
        staffEvent.setUsername(sessionService.getUsername());
        staffEvent.setPersonId(staff.getUuid());
        staffEvent.setPersonName(staff.getName());
        staffEvent.setClientMachineName(sessionService.getHostname());
        return staffEvent;
    }

}
