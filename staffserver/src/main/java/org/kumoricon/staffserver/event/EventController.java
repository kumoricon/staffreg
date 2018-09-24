package org.kumoricon.staffserver.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kumoricon.staff.dto.StaffEvent;
import org.kumoricon.staffserver.staff.Staff;
import org.kumoricon.staffserver.staff.StaffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;

@RestController
@SuppressWarnings(value = "unused")
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);
    private EventStorageService eventStorageService;
    private StaffRepository staffRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public EventController(EventStorageService eventService, StaffRepository staffRepository) {
        this.eventStorageService = eventService;
        this.staffRepository = staffRepository;
    }

    @PostMapping(value = "/event")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) {
        boolean success = true;
        for (MultipartFile file : files) {
            try {
                StaffEvent staffEvent = objectMapper.readValue(file.getInputStream(), StaffEvent.class);
                StaffEventRecord record = new StaffEventRecord(staffEvent, request.getRemoteUser(), Instant.now(), request.getRemoteAddr());
                log.info("Received {}", record);
                eventStorageService.storeEvent(record);
                processEvent(record);
            } catch (IOException ex) {
                success = false;
                log.error("Error processing {}", file.getOriginalFilename(), ex);
            }
        }
        if (success) {
            return ResponseEntity.accepted().body("Accepted");
        } else {
            return ResponseEntity.unprocessableEntity().body("Error processing uploaded record");
        }
    }


    private void processEvent(StaffEventRecord event) {
        Staff staff = staffRepository.findByUuid(event.getEvent().getPersonId());
        if (staff != null) {

            if (StaffEvent.EVENT_TYPE.CHECK_IN.name().equals(event.getEvent().getEventType())) {
                staff.setLastModified(Instant.now());
                staff.setCheckedIn(true);
                staff.setCheckedInAt(Instant.ofEpochMilli(event.getEvent().getEventCreatedAt()));
                staff.setBadgePrinted(true);
                staff.setBadgePrintCount(1);
                staffRepository.save(staff);
            } else if (StaffEvent.EVENT_TYPE.REPRINT_BADGE.name().equals(event.getEvent().getEventType())) {
                staff.setBadgePrintCount(staff.getBadgePrintCount()+1);
                staffRepository.save(staff);
            }
        } else {
            log.error("Checkin event received for {} but staff record not found. Event: {}",
                    event.getEvent().getPersonId(), event);
        }
    }
}
