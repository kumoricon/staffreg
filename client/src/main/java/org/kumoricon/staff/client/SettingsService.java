package org.kumoricon.staff.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.prefs.Preferences;

/**
 * Represents program settings, such as client ID, paths, etc.
 */
public class SettingsService {
    private static final String BASE_PATH = "work/";
    private static final Logger log = LoggerFactory.getLogger(SettingsService.class);

    private final SimpleStringProperty clientId = new SimpleStringProperty();

    private static final String CLIENT_ID_NAME = "clientId";


    @PostConstruct
    public void init() {
        WorkingDirectoryHelper.makeSureDirectoryExists(getAllPaths());
        log.info("Settings service initialized");
        loadSettingsAsync();
    }


    private void loadSettingsAsync() {
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                Preferences prefs = Preferences.userRoot().node("StaffReg");
                log.info("Loading settings from " + prefs.absolutePath());

                clientId.set(prefs.get(CLIENT_ID_NAME, UUID.randomUUID().toString()));
                prefs.put(CLIENT_ID_NAME, clientId.getValue());
                log.info("Client ID: " + clientId.getValue());

                return null;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public String getClientId() {
        return clientId.get();
    }

    public SimpleStringProperty clientIdProperty() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId.set(clientId);
    }

    public String getBasePath() {
        return BASE_PATH;
    }

    public String getWorkQueue() {
        return BASE_PATH + "tmp/";
    }

    public String getOutboundQueue() {
        return BASE_PATH + "outbound/";
    }

    public String[] getAllPaths() {
        String[] paths = {BASE_PATH, getWorkQueue(), getOutboundQueue()};
        return paths;
    }
}
