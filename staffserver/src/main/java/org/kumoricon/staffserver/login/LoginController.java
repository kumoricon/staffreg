package org.kumoricon.staffserver.login;

import org.kumoricon.staff.dto.LoginResponse;
import org.kumoricon.staff.dto.PasswordChangeResponse;
import org.kumoricon.staffserver.exception.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
            return new LoginResponse(request.getRemoteUser(), true, true);
        } else {
            throw new ForbiddenException("Access denied");
        }
    }

    @RequestMapping(value = "/login/password", method = RequestMethod.POST)
    public PasswordChangeResponse changePassword(@RequestParam(name = "clientId") String clientId,
                                                 @RequestParam(name = "newPassword") String newPassword,
                                                 HttpServletRequest request) {
        log.info("clientId: {}", clientId);
        log.info("IP: {}", request.getRemoteAddr());
        log.info("User: {}", request.getRemoteUser());
        log.info("New Password: {}", newPassword);

        log.info(request.getHeader("Authorization"));
        boolean success = true;
        if (success) {
            return new PasswordChangeResponse(true, newPassword);
        } else {
            return new PasswordChangeResponse(false, "");
        }
    }
}