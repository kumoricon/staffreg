package org.kumoricon.staff.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;

public class WorkingDirectoryHelper {
    private static final Logger log = LoggerFactory.getLogger(WorkingDirectoryHelper.class);

    public static void makeSureDirectoryExists(String[] paths) {
        for (String path : paths) {
            File directory = new File(path);
            if (!directory.exists()) {
                log.info("Creating " + path);
                boolean success = directory.mkdirs();
                if (!success) {
                    log.error("Error creating " + path);
                }
            }
        }
    }

}
