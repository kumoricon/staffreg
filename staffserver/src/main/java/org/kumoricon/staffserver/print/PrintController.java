package org.kumoricon.staffserver.print;

import org.kumoricon.staff.dto.PrintBadgeResponse;
import org.kumoricon.staff.dto.PrinterResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@SuppressWarnings(value = "unused")
public class PrintController {
    private static final Logger log = LoggerFactory.getLogger(PrintController.class);
    private final PrinterService printerService;

    @Autowired
    public PrintController(PrinterService printerService) {
        this.printerService = printerService;
    }

    @RequestMapping(value = "/printers", method = RequestMethod.GET)
    public List<PrinterResponse> printers(HttpServletRequest request) {
        List<PrinterResponse> printerNames = new ArrayList<>();

        for (String printer : printerService.getPrinterNames()) {
            printerNames.add(new PrinterResponse(printer));
        }
        return printerNames;
    }


}