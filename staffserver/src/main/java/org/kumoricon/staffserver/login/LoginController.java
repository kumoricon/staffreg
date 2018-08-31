package org.kumoricon.staffserver.login;

import org.kumoricon.staff.dto.LoginResponse;
import org.kumoricon.staff.dto.PasswordChangeResponse;
import org.kumoricon.staffserver.exception.ForbiddenException;
import org.kumoricon.staffserver.user.User;
import org.kumoricon.staffserver.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final UserRepository userRepository;

    @Autowired
    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestParam(name = "clientId") String clientId, HttpServletRequest request) {
        log.info("{} logging in on client {} from {}",
                request.getRemoteUser(), clientId, request.getRemoteAddr());

        User u = userRepository.findByUsername(request.getRemoteUser());
        boolean loginSucceeded = true;
        if (loginSucceeded) {
            return new LoginResponse(request.getRemoteUser(), true, u.isPasswordResetRequired());
        } else {
            throw new ForbiddenException("Access denied");
        }
    }

    @RequestMapping(value = "/login/password", method = RequestMethod.POST)
    public PasswordChangeResponse changePassword(@RequestParam(name = "clientId") String clientId,
                                                 @RequestParam(name = "newPassword") String newPassword,
                                                 HttpServletRequest request) {
        log.info("{} setting new password on client {} from {}",
                request.getRemoteUser(), clientId, request.getRemoteAddr());

        boolean success = true;
        if (success) {
            return new PasswordChangeResponse(true, newPassword);
        } else {
            return new PasswordChangeResponse(false, "");
        }
    }
}