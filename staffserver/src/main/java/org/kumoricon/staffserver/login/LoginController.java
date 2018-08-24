package org.kumoricon.staffserver.login;

import org.kumoricon.staffserver.exception.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestParam(name = "clientId") String clientId, HttpServletRequest request) {
        log.info("clientId: {}", clientId);
        log.info("IP: {}", request.getRemoteAddr());
        log.info("User: {}", request.getRemoteUser());

        log.info(request.getHeader("Authorization"));
        boolean loginSucceeded = true;
        if (loginSucceeded) {
            return new LoginResponse(request.getRemoteUser(), true);
        } else {
            throw new ForbiddenException("Access denied");
        }
    }
}