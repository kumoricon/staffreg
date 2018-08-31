package org.kumoricon.staffserver.event;

import org.kumoricon.staff.dto.StaffEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@RestController
@SuppressWarnings(value = "unused")
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);
    private EventDao eventDao;

    @Autowired
    public EventController(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    public ResponseEntity event(@RequestBody StaffEvent body, HttpServletRequest request) {
        StaffEventRecord record = new StaffEventRecord(body, request.getRemoteUser(), Instant.now(), request.getRemoteAddr());
        log.info("User: " + request.getRemoteUser());
        log.info("Received {}", record);
        eventDao.save(record);

        return ResponseEntity.ok("Accepted");
    }

}
