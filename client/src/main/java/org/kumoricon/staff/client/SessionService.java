package org.kumoricon.staff.client;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Represents the current user session
 */
public class SessionService {
    private static final Logger log = LoggerFactory.getLogger(SessionService.class);
    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    private final SimpleStringProperty hostname = new SimpleStringProperty();
    private final SimpleBooleanProperty loggedIn = new SimpleBooleanProperty(false);

    @PostConstruct
    public void init() {
        log.info("Session service initialized");
        getHostnameAsync();
    }

    private void getHostnameAsync() {
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                hostname.set(findComputerName());
                log.info("Hostname: " + hostname.getValue());
                return null;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private String findComputerName() {
        String hostname;
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME")) {
            hostname = env.get("COMPUTERNAME");
        } else if (env.containsKey("HOSTNAME")) {
            hostname = env.get("HOSTNAME");
        } else {
            try {
                InetAddress addr = InetAddress.getLocalHost();
                hostname = addr.getHostName();
            } catch (UnknownHostException ex) {
                hostname = "Unknown computer";
                log.warn("Hostname can not be resolved");
            }
        }
        log.info("Computer hostname: " + hostname);
        return hostname;
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getHostname() {
        return hostname.get();
    }

    public SimpleStringProperty hostnameProperty() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname.set(hostname);
    }

    public boolean isLoggedIn() {
        return loggedIn.get();
    }

    public SimpleBooleanProperty loggedInProperty() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn.set(loggedIn);
    }
}
