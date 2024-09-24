package CMS.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import CMS.DAO.UserRepo;
import CMS.Master.StudentMaster;
import CMS.Service.UserService;

@Controller
public class LoginHomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @ModelAttribute
    public void commonUser(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            StudentMaster student = userRepo.findByEmail(email);
            model.addAttribute("user", student);
        }
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/signin")
    public String login() {
        return "login";
    }
    
}

