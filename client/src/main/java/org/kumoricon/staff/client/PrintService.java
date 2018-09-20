package org.kumoricon.staff.client;

import javafx.concurrent.Task;
import org.kumoricon.staff.client.model.Staff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class PrintService {
    private static final Logger log = LoggerFactory.getLogger(PrintService.class);

    @Inject
    private SessionService sessionService;

    @Inject
    private SettingsService settingsService;

    @PostConstruct
    public void init() {
    }


    public String printBadge(Staff staff) {
        String printerName = "Fooooo";
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
            log.info("Printing badge for {} {} on {}", staff.getFirstName(), staff.getLastName(), printerName);
                return null;
            }
        };

        Thread th = new Thread(task);
        th.setUncaughtExceptionHandler((t, e) -> log.error("printBadge error", e));
        th.setDaemon(true);
        th.start();

        return printerName;
    }

}
