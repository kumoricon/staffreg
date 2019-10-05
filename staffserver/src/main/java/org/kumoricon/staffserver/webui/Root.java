package org.kumoricon.staffserver.webui;

import org.kumoricon.staffserver.staff.StaffRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Root {
    private final StaffRepository staffRepository;

    public Root(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @RequestMapping(value = "/")
    public String webUi(Model model) {
        model.addAttribute("staff", staffRepository.findAllByOrderByDepartmentAscFirstNameAscLastNameAsc());
        return "ui/home";
    }
}
