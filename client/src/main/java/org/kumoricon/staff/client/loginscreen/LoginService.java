package org.kumoricon.staff.client.loginscreen;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.kumoricon.staff.client.SessionService;
import org.kumoricon.staff.client.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private static final String LOGIN_URI_PATH = "/login";

    @Inject
    private SettingsService settingsService;

    @Inject
    private SessionService sessionService;

    @PostConstruct
    public void init() {
        log.info("Login service initialized");
        log.info(sessionService.getHostname() + " - " + settingsService.getClientId());
    }

    public void login(String username, String password, String serverURL) {
        try {
            sessionService.setUsername(username);
            sessionService.setPassword(password);
            sessionService.setServerHostname(serverURL);
            tryLogin();
            log.info(username + " logged in successfully");
            sessionService.setLoggedIn(true);
        } catch (IOException ex) {
            log.error("Login error", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    private boolean tryLogin() throws IOException {
        HttpResponse response =
                Request.Post(sessionService.getServerHostname() + LOGIN_URI_PATH)
                        .addHeader(HttpHeaders.AUTHORIZATION, sessionService.getHttpAuthHeader())
                        .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                        .bodyForm(Form.form()
                                .add("clientId", settingsService.getClientId())
                                .build())
                        .execute()
                        .returnResponse();
        log.info("Login response: {}", response.getStatusLine());
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("HTTP status " + response.getStatusLine().getStatusCode());
        }
        EntityUtils.consume(response.getEntity());
        return true;
    }



}
