package org.kumoricon.staffserver.healthcheck;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@SuppressWarnings(value = "unused")
public class HealthcheckController {

    @RequestMapping(value = "/healthcheck", method = RequestMethod.GET)
    public String heartbeats(HttpServletRequest request) {
        return "OK";
    }


}