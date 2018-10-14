package org.kumoricon.staffserver.staff;

import org.kumoricon.staff.StaffBadgePrintFormatter;
import org.kumoricon.staff.badgelib.StaffBadgeDTO;
import org.kumoricon.staffserver.badgeimage.BadgeImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class BadgeService {
    private static final Logger log = LoggerFactory.getLogger(BadgeImageService.class);
    @Value("${staffreg.printing.enabled}")
    private Boolean enablePrinting;

    @Autowired
    public BadgeService() {
        if (enablePrinting == null) enablePrinting = true;
    }

    public InputStream buildBadge(Staff staff, int xOffset, int yOffset) {
        StaffBadgeDTO badgeDTO = staffToStaffBadgeDTO(staff);
        StaffBadgePrintFormatter formatter = new StaffBadgePrintFormatter(badgeDTO, null, xOffset, yOffset);
        return formatter.getStream();
    }

    private static StaffBadgeDTO staffToStaffBadgeDTO(Staff staff) {
        return new StaffBadgeDTO.Builder()
                .withFirstName("Jason")
                .withLastName("Short")
                .withAgeBackgroundColor("#323E99")
                .withAgeRange("Adult")
                .withDepartment("Registration")
                .withDepartmentBackgroundColor("#f57f20")
                .withHasBadgeImage(false)
                .withPosition("Registration Software Development Manager")
//                .withPosition("Grand Poobah")
                .withPosition("Dude")
//                .withBadgeImage(staffImage)
                .build();

        //        return new StaffBadgeDTO.Builder()
//                .withFirstName(staff.getFirstName())
//                .withLastName(staff.getLastName())
//                .withDepartmentBackgroundColor(staff.)
//                .withPositions(staff.getPosition().split("|"))
//                .withDepartment(staff.getDepartment())
//                .build();
    }

}

