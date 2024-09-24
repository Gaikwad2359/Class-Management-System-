package CMS.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	  @GetMapping("/home")
	    public String showHomePage(HttpSession session, Model model) {
	        if (session.getAttribute("user") != null) {
	            // User is logged in, show all pages
	            model.addAttribute("showAllPages", true);
	        } else {
	            // User is not logged in, restrict access
	            model.addAttribute("showAllPages", false);
	        }
	        return "/Home/home";  // return your home page
	    }

}
