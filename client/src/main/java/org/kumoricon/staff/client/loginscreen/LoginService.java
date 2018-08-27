package org.kumoricon.staff.client.loginscreen;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.kumoricon.staff.client.SessionService;
import org.kumoricon.staff.client.SettingsService;
import org.kumoricon.staff.dto.LoginResponse;
import org.kumoricon.staff.dto.PasswordChangeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private static final String LOGIN_URI_PATH = "/login";
    private static final String PASSWORD_CHANGE_URI_PATH = "/login/password";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    private SettingsService settingsService;

    @Inject
    private SessionService sessionService;

    @PostConstruct
    public void init() {
        log.info("Login service initialized");
        log.info(sessionService.getHostname() + " - " + settingsService.getClientId());
    }

    public LoginResponse login(String username, String password, String serverURL) {
        try {
            sessionService.setUsername(username);
            sessionService.setPassword(password);
            sessionService.setServerHostname(serverURL);
            LoginResponse response = tryLogin();
            sessionService.setPasswordChangeRequired(response.isPasswordChangeRequired());
            sessionService.setLoggedIn(response.isSuccess());
            log.info(username + " logged in");
            return response;
        } catch (IOException ex) {
            log.error("Login error", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    private LoginResponse tryLogin() throws IOException {
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

        LoginResponse loginResponse =  objectMapper.readValue(response.getEntity().getContent(), LoginResponse.class);

        EntityUtils.consume(response.getEntity());
        return loginResponse;
    }

    public PasswordChangeResponse setNewPassword(String newPassword) {
        try {
            HttpResponse response =
                    Request.Post(sessionService.getServerHostname() + PASSWORD_CHANGE_URI_PATH)
                            .addHeader(HttpHeaders.AUTHORIZATION, sessionService.getHttpAuthHeader())
                            .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                            .bodyForm(Form.form()
                                    .add("clientId", settingsService.getClientId())
                                    .add("newPassword", newPassword)
                                    .build())
                            .execute()
                            .returnResponse();
            log.info("Login response: {}", response.getStatusLine());
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("HTTP status " + response.getStatusLine().getStatusCode());
            }

            PasswordChangeResponse passwordResponse = objectMapper.readValue(response.getEntity().getContent(), PasswordChangeResponse.class);

            EntityUtils.consume(response.getEntity());
            if (passwordResponse.getSuccess()) {
                sessionService.setPassword(passwordResponse.getNewPassword());
                sessionService.setPasswordChangeRequired(false);
                sessionService.setLoggedIn(true);
            }
            return passwordResponse;
        } catch (IOException ex) {
            log.error("Error changing password", ex);
        }
        return new PasswordChangeResponse(false, "");
    }

}
