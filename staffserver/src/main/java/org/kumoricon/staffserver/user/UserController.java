package org.kumoricon.staffserver.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<UserResponse> getUsers(HttpServletRequest request) {
        List<User> users = userRepository.findAll();
        log.info(request.getRemoteUser());
        log.info(request.getAuthType());
        log.info("ADMIN: {}", request.isUserInRole("ADMIN"));
        log.info("TEST: {}", request.isUserInRole("TEST"));
        List<UserResponse> userOutput = new ArrayList<>();
        for (User user : users) {
            List<String> roles = new ArrayList<>();
            for (Role role : user.getRoles()) {
                roles.add(role.getName());
            }
            userOutput.add(new UserResponse(user.getUsername(), user.isEnabled(), roles));
        }
        return userOutput;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addUser(@RequestParam(name = "username") String username,
            @RequestParam(name = "role") String role,
            HttpServletRequest request) {

        Role r = roleRepository.findByName(role);

        User user = new User();
        user.setRoles(Collections.singletonList(r));
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("password"));
        user.setEnabled(true);
        user.setPasswordResetRequired(true);

        userRepository.save(user);

        log.info("Creating user {}", user);
        return "Created " + user.getUsername();
    }

}