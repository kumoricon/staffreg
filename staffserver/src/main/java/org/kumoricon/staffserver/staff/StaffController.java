package org.kumoricon.staffserver.staff;

import org.kumoricon.staff.dto.StaffResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@SuppressWarnings(value = "unused")
public class StaffController {
    private static final Logger log = LoggerFactory.getLogger(StaffController.class);
    private StaffRepository staffRepository;

    @Autowired
    public StaffController(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
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
}
