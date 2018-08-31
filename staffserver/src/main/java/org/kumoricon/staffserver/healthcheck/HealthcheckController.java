package org.kumoricon.staffserver.healthcheck;

import org.kumoricon.staff.dto.HeartbeatRequest;
import org.kumoricon.staffserver.heartbeat.HeartbeatDao;
import org.kumoricon.staffserver.heartbeat.HeartbeatRecord;
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
public class HealthcheckController {

    @RequestMapping(value = "/healthcheck", method = RequestMethod.GET)
    public String heartbeats(HttpServletRequest request) {
        return "OK";
    }


}