package org.kumoricon.staffserver.webui;

import org.kumoricon.staffserver.staff.Staff;
import org.kumoricon.staffserver.staff.StaffRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.Instant;

@Controller
public class CheckIn {
    private final StaffRepository staffRepository;

    public CheckIn(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @RequestMapping(value = "//checkin/{uuid}")
    public String checkIn1(Model model, @PathVariable(name = "uuid") String uuid) {
        model.addAttribute("staff", staffRepository.findByUuid(uuid));
        return "ui/checkin/step1";
    }

    @RequestMapping(value = "//checkin/{uuid}", method = RequestMethod.POST)
    public String checkIn1Post(Model model, @PathVariable(name = "uuid") String uuid) {
        Staff s = staffRepository.findByUuid(uuid);
        s.setInformationVerified(true);
        staffRepository.save(s);
        return "redirect:/checkin2/" + uuid;
    }

    @RequestMapping(value = "//checkin2/{uuid}")
    public String checkIn2(Model model, @PathVariable(name = "uuid") String uuid) {
        Staff staff = staffRepository.findByUuid(uuid);
        if (!staff.getInformationVerified()) {
            return "redirect:/checkin/" + uuid + "?err=Information+not+verified";
        }
        model.addAttribute("staff", staff);
        return "ui/checkin/step2";
    }

    @RequestMapping(value = "//checkin2/{uuid}", method = RequestMethod.POST)
    public String checkIn2Post(Model model, @PathVariable(name = "uuid") String uuid) {
        Staff s = staffRepository.findByUuid(uuid);
        s.setPictureSaved(true);
        staffRepository.save(s);
        return "redirect:/checkin3/" + uuid;
    }

    @RequestMapping(value = "//checkin3/{uuid}")
    public String checkIn3(Model model, @PathVariable(name = "uuid") String uuid) {
        Staff staff = staffRepository.findByUuid(uuid);
        if (!staff.getPictureSaved()) {
            return "redirect:/checkin2/" + uuid + "?err=Picture+not+saved";
        }
        model.addAttribute("staff", staff);
        return "ui/checkin/step3";
    }

    @RequestMapping(value = "//checkin3/{uuid}", method = RequestMethod.POST)
    public String checkIn3Post(Model model, @PathVariable(name = "uuid") String uuid) {
        Staff s = staffRepository.findByUuid(uuid);
        s.setSignatureSaved(true);
        s.setCheckedIn(true);
        s.setCheckedInAt(Instant.now());
        staffRepository.save(s);
        return "redirect:/checkin4/" + uuid;
    }

    @RequestMapping(value = "//checkin4/{uuid}")
    public String checkInStep4(Model model, @PathVariable(name = "uuid") String uuid) {
        Staff staff = staffRepository.findByUuid(uuid);
        if (!staff.getInformationVerified()) {
            return "redirect:/checkin/" + uuid + "?err=Information+not+verified";
        }
        if (!staff.getPictureSaved()) {
            return "redirect:/checkin2/" + uuid + "?err=Picture+not+saved";
        }
        if (!staff.getSignatureSaved()) {
            return "redirect:/checkin3/" + uuid + "?err=Signature+not+saved";
        }
        model.addAttribute("staff", staff);
        return "ui/checkin/step4";
    }


}
