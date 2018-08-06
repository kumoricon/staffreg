package org.kumoricon.staff.client.preferencesscreen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

public class PreferencesService {
    private static final Logger log = LoggerFactory.getLogger(PreferencesService.class);

    @PostConstruct
    public void init() {
        log.info("Preferences service initialized");
    }

}
