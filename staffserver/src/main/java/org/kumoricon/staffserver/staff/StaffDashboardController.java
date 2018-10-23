package org.kumoricon.staffserver.staff;

import org.kumoricon.staff.dto.PrintBadgeResponse;
import org.kumoricon.staff.dto.StaffResponse;
import org.kumoricon.staffserver.print.PrinterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.print.PrintService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@SuppressWarnings(value = "unused")
public class StaffDashboardController {
    private static final Logger log = LoggerFactory.getLogger(StaffDashboardController.class);
    private final StaffRepository staffRepository;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("E hh:mm:ss a (MM/dd/yyyy)")
                    .withLocale(Locale.US)
                    .withZone( ZoneId.of("US/Pacific"));
    @Autowired
    public StaffDashboardController(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }


    @RequestMapping(value = "/staff/dashboard", method = RequestMethod.GET)
    public String staffDashboard(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("content-type", "text/html");
        return buildDashboardPage(staffRepository.findAllByOrderByDepartmentAscFirstNameAscLastNameAsc());
    }

    private static String buildDashboardPage(List<Staff> staffList) {
        int checkedIn = 0;
        int total = 0;

        StringBuilder sb = new StringBuilder();
        sb.append("<html><head>");
        sb.append("<title>Staff Dashboard</title>");
        sb.append("<style>tr:nth-child(even) { background-color:#dddddd; }</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<table>");
        sb.append("<tr><th>Name</th><th>Department</th><th>Checked In at</th></tr>");
        for (Staff staff : staffList) {
            total++;
            if (staff.getCheckedIn()) checkedIn++;
            sb.append("<tr>");
            sb.append("<td>" + staff.getFirstName() + " " + staff.getLastName() + "</td>");
            sb.append("<td>" + staff.getDepartment() + "</td>");
            if (staff.getCheckedInAt() != null) {
                sb.append("<td>" + formatter.format(staff.getCheckedInAt()) + "</td>");
            } else {
                sb.append("<td></td>");
            }
        }
        sb.append("</table>");

        sb.append("<div style=\"position: fixed; top: 20px; right: 20px; padding: 5px; border: 1px solid black;\">");
        sb.append("Checked in: " + checkedIn + "/" + total + " as of " + formatter.format(Instant.now()));
        sb.append("</div>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }


}
