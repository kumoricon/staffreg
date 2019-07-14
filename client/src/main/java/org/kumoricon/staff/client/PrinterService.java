package org.kumoricon.staff.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.kumoricon.staff.client.model.Staff;
import org.kumoricon.staff.dto.PrinterResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrinterService {
    private static final Logger log = LoggerFactory.getLogger(PrinterService.class);
    private static final String STAFF_URI_PATH = "/staff/";
    private static final String STAFF_PRINT_PATH = "/printBadge/";
    private static final String PRINTER_URI_PATH = "/printers";

    private final ObjectMapper mapper = new ObjectMapper();

    private final ObservableList<String> availablePrinters = FXCollections.observableArrayList();

    @Inject
    private SettingsService settingsService;

    @Inject
    private SessionService sessionService;

    @PostConstruct
    public void init() {
        log.info("Staff Print service initialized");
        log.info(sessionService.getHostname() + " - " + settingsService.getClientId());
    }


    public String print(Staff staff) {
        try {
            tryPrint(staff.getUuid(), settingsService.getPrinterName());
        } catch (IOException ex) {
            log.error("Printer error", ex);
            return "Error printing on " + settingsService.getPrinterName() + ": " + ex.getMessage() +". Try printing again?";
        }
        return "Printed badge for " + staff.getName() + " on printer " + settingsService.getPrinterName() + ". Do you need to reprint?";
    }

    private void tryPrint(String staffId, String printerName) throws IOException {
        String url = sessionService.getServerHostname() + STAFF_URI_PATH + staffId + STAFF_PRINT_PATH + printerName;
        log.info("Sending POST to {}", url);
        HttpResponse response =
                Request.Post(url)
                        .addHeader(HttpHeaders.AUTHORIZATION, sessionService.getHttpAuthHeader())
                        .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                        .bodyForm(Form.form()
                                .add("clientId", settingsService.getClientId())
                                .build())
                        .execute()
                        .returnResponse();
        log.info("Print response: {}", response.getStatusLine());
        if (response.getStatusLine().getStatusCode() == 404) {
            throw new IOException("Printer not found (404)");
        } else if (response.getStatusLine().getStatusCode() == 500) {
            throw new IOException("Server error (500)");
        } else if (response.getStatusLine().getStatusCode() != 202) {
            throw new IOException("HTTP status " + response.getStatusLine().getStatusCode());
        }
    }

    public void refreshPrinterList() {
        Task<Void> getWebcamList = new Task<Void>() {
            @Override
            protected Void call() {
                log.info("Getting printers");
                List<String> printers = new ArrayList<>();

                try {
                    HttpResponse response =
                            Request.Get(sessionService.getServerHostname() + PRINTER_URI_PATH)
                                    .addHeader(HttpHeaders.AUTHORIZATION, sessionService.getHttpAuthHeader())
                                    .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType())

                                    .execute()
                                    .returnResponse();

                    List<PrinterResponse> printerResponses = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<PrinterResponse>>(){});
                    for (PrinterResponse printerResponse : printerResponses) {
                        printers.add(printerResponse.getName());
                    }
                } catch (IOException e) {
                    log.error("Bad response from {}", PRINTER_URI_PATH, e);
                }

                availablePrinters.setAll(printers);
                log.info("Printers: " + availablePrinters);
                return null;
            }
        };

        Thread t = new Thread(getWebcamList);
        t.setDaemon(true);
        t.start();
    }


    public ObservableList<String> getAvailablePrinters() {
        return availablePrinters;
    }
}
