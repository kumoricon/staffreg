package org.kumoricon.staffserver.staff;

import org.kumoricon.staff.dto.PrintBadgeResponse;
import org.kumoricon.staff.dto.StaffResponse;
import org.kumoricon.staffserver.print.PrinterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@SuppressWarnings(value = "unused")
public class StaffController {
    private static final Logger log = LoggerFactory.getLogger(StaffController.class);
    private final StaffRepository staffRepository;
    private final PrinterService printerService;
    private final BadgeService badgeService;

    @Autowired
    public StaffController(StaffRepository staffRepository, PrinterService printerService, BadgeService badgeService) {
        this.staffRepository = staffRepository;
        this.printerService = printerService;
        this.badgeService = badgeService;
    }

    @RequestMapping(value = "/staff", method = RequestMethod.GET)
    public List<StaffResponse> getStaff(@RequestParam(defaultValue = "0") Long timestampMS,
                                        HttpServletRequest request) {

        List<Staff> staff = staffRepository.findByLastModifiedMSAfter(timestampMS);
        List<StaffResponse> staffResponses = new ArrayList<>();

        for (Staff s : staff) {
            staffResponses.add(s.toStaffResponse());
        }
        return staffResponses;
    }

    @RequestMapping(value = "/staff/{staffId:[a-zA-Z0-9\\-]+}/printBadge/{printerName:[a-zA-Z0-9\\-_]+}", method = RequestMethod.POST)
    public PrintBadgeResponse printBadge(@PathVariable("printerName")String printerName,
                                         @PathVariable("staffId")String staffId,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        log.info("{} printing staff {} to {}", request.getRemoteUser(), staffId, printerName);

        Staff staff = staffRepository.findByUuid(staffId);
        PrintService printService = printerService.findByName(printerName);

        if (staff == null) {
            log.error("{}: staff ID {} not found", request.getRemoteUser(), staffId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new PrintBadgeResponse(printerName, staffId, "Staff record not found", false);
        }
        if (printService == null) {
            log.error("{}: printer {} not found", request.getRemoteUser(), printerName);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new PrintBadgeResponse(printerName, staffId, "Printer not found", false);
        }

        try (InputStream badgePDF = badgeService.buildBadge(staff, 0, 0)) {
            printerService.printPDFDoubleSided(printService, badgePDF);
        } catch (Exception ex) {
            log.error("Error printing badge for {}", request.getRemoteUser(), ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new PrintBadgeResponse(printerName, staffId, ex.getMessage(), false);
        }

        staff.setBadgePrinted(true);
        staff.setBadgePrintCount(staff.getBadgePrintCount() + 1);
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        return new PrintBadgeResponse(printerName, staffId, "", true);

    }


}
