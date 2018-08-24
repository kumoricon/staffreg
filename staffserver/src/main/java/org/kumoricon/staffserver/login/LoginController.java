package org.kumoricon.staffserver.login;

import org.kumoricon.staffserver.exception.ForbiddenException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@SuppressWarnings(value = "unused")
public class LoginController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody LoginRequest body, HttpServletRequest request) {
        System.out.println(request.getRemoteAddr());

        boolean loginSucceeded = true;
        if (loginSucceeded) {
            return new LoginResponse(body.getUsername(), true);
        } else {
            throw new ForbiddenException("Access denied");
        }
    }
}