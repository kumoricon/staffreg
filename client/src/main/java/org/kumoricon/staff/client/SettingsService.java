package org.kumoricon.staff.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

public class SettingsService {
    private static final String BASE_PATH = "work/";
    private static final Logger log = LoggerFactory.getLogger(SettingsService.class);

    @PostConstruct
    public void init() {
        WorkingDirectoryHelper.makeSureDirectoryExists(getAllPaths());
        log.info("Settings service initialized");
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
