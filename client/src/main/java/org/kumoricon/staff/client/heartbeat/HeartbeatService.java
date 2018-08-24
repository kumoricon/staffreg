package org.kumoricon.staff.client.heartbeat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.kumoricon.staff.client.HealthService;
import org.kumoricon.staff.client.SessionService;
import org.kumoricon.staff.client.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HeartbeatService {
    @Inject
    HealthService healthService;

    @Inject
    SettingsService settingsService;

    @Inject
    SessionService sessionService;

    private final BooleanProperty sendHeartbeat = new SimpleBooleanProperty(true);
    // TODO: Change this to an object property and just maintain the Instant of the last successful heartbeat
    private final StringProperty statusMessage = new SimpleStringProperty();
    private static final String URI_PATH = "/heartbeat";
    private static final Logger log = LoggerFactory.getLogger(HeartbeatService.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

    @PostConstruct
    public void init() {
        startSendingHeartbeat();
    }

    private void startSendingHeartbeat() {
        ScheduledService<LocalDateTime> svc = new ScheduledService<LocalDateTime>() {
            protected Task<LocalDateTime> createTask() {
            return new Task<LocalDateTime>() {
                protected LocalDateTime call() throws JsonProcessingException {
                if (sendHeartbeat.get() && sessionService.isLoggedIn() && sessionService.getServerHostname() != null && !sessionService.getServerHostname().isEmpty()) {
                    String jsonData = mapper.writeValueAsString(getHeartbeatMessage());
                    try {
                        HttpResponse response =
                                Request.Post(sessionService.getServerHostname() + URI_PATH)
                                        .bodyString(jsonData, ContentType.APPLICATION_JSON)
                                        .execute()
                                        .returnResponse();
                        log.info("Heartbeat response: {}", response.getStatusLine());
                        EntityUtils.consume(response.getEntity());
                        return LocalDateTime.now();
                    } catch (IOException ex) {
                        log.error("Error sending heartbeat", ex);
                    }
                }
                return null;
                }
            };
            }
        };
        svc.setPeriod(Duration.seconds(15));
        svc.setRestartOnFailure(true);
        svc.setMaximumCumulativePeriod(Duration.seconds(10));
        svc.setOnSucceeded(e -> statusMessage.setValue(buildStatusMessage((LocalDateTime) e.getSource().getValue())));
        svc.start();
    }

    private String buildStatusMessage(LocalDateTime dateTime) {
        if (dateTime != null) {
            return FORMATTER.format(dateTime);
        }
        return "";
    }

    private HeartbeatMessage getHeartbeatMessage() {
        HeartbeatMessage h = new HeartbeatMessage();
        h.setClientId(settingsService.getClientId());
        h.setMachineName(sessionService.getHostname());
        h.setUsername(sessionService.getUsername());
        h.setOutboundQueueCount(healthService.getOutboundQueueCount());
        h.setWorkQueueCount(healthService.getWorkQueueCount());
        return h;
    }

    public String getStatusMessage() {
        return statusMessage.get();
    }

    public StringProperty statusMessageProperty() {
        return statusMessage;
    }
}
