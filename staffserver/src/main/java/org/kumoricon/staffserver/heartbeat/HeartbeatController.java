package org.kumoricon.staffserver.heartbeat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;

@RestController
@SuppressWarnings(value = "unused")
public class HeartbeatController {
    private HeartbeatDao heartbeatDao;

    @Autowired
    public HeartbeatController(HeartbeatDao heartbeatDao) {
        this.heartbeatDao = heartbeatDao;
    }

    @RequestMapping(value = "/heartbeat", method = RequestMethod.POST)
    public ResponseEntity heartbeat(@RequestBody HeartbeatRequest body, HttpServletRequest request) {
        HeartbeatRecord record = new HeartbeatRecord(body, request.getRemoteUser(), request.getRemoteAddr(), Instant.now());

        heartbeatDao.save(record);

        return ResponseEntity.ok("Accepted");
    }

    @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
    public List<HeartbeatRecord> heartbeats(HttpServletRequest request) {
        return heartbeatDao.getRecords();
    }


}