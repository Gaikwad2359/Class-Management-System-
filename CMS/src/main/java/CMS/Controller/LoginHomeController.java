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
    
    
    //Registration
//    @GetMapping("/register")
//    public String register() {
//        return "/register";
//    }
//
//    
//    @GetMapping("/registerAdmin")
//    public String registerAdmin() {
//        return "/registerAdmin";
//    }
//
//    @PostMapping("/saveUser")
//    public String saveUser(@ModelAttribute StudentMaster student, HttpSession session, HttpServletRequest request) {
//        String url = request.getRequestURL().toString().replace(request.getServletPath(), "");
//
//        StudentMaster newUser = userService.saveStudentMaster(student, url, false);
//
//        if (newUser != null) {
//            session.setAttribute("msg", "Registered successfully");
//        } else {
//            session.setAttribute("msg", "Something went wrong on the server");
//        }
//
//        return "redirect:/register";
//    }
//
//    @PostMapping("/saveAdmin")
//    public String saveAdmin(@ModelAttribute StudentMaster user, HttpSession session, HttpServletRequest request) {
//        String url = request.getRequestURL().toString().replace(request.getServletPath(), "");
//
//        StudentMaster newAdmin = userService.saveStudentMaster(user, url, true);
//
//        if (newAdmin != null) {
//            session.setAttribute("msg", "Admin registered successfully");
//        } else {
//            session.setAttribute("msg", "Something went wrong on the server");
//        }
//
//        return "redirect:/register";
//    }
//    
//    @GetMapping("/verify")
//    public String verifyAccount(@Param("code") String code, Model model) {
//        boolean isVerified = userService.verifyAccount(code);
//
//        if (isVerified) {
//            model.addAttribute("msg", "Successfully verified your account");
//        } else {
//            model.addAttribute("msg", "Verification code might be incorrect or already verified");
//        }
//
//        return "message";
//    }
//	
    
}

