package org.kumoricon.staff.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class WorkingDirectoryHelper {
    private static final String BASE_PATH = "work/";
    public static final String WORK_QUEUE = BASE_PATH + "tmp/";
    public static final String OUTBOUND_QUEUE = BASE_PATH + "outbound/";

    private static final Logger log = LoggerFactory.getLogger(WorkingDirectoryHelper.class);


    public void makeSureDirectoriesExist() {
        String[] paths = {WORK_QUEUE, OUTBOUND_QUEUE};
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
