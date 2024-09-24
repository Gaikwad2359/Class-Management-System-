package CMS.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CourseDashboardCotroller {

    @GetMapping("/courses/FullStackDevelopment")
    public String fullStackDevelopment(Model model) {
        model.addAttribute("courseName", "Java Full Stack Development");
        // Add more course-specific attributes if needed
        return "coursesDashboard/FullStackDevelopment";
    }

    @GetMapping("/courses/DataStructure")
    public String dataStructure(Model model) {
        model.addAttribute("courseName", "Data Structure and Algorithms");
        return "coursesDashboard/DataStructure";
    }

    @GetMapping("/courses/Angular")
    public String angular(Model model) {
        model.addAttribute("courseName", "Angular Development");
        return "coursesDashboard/Angular";
    }

    @GetMapping("/courses/CProgramming")
    public String cProgramming(Model model) {
        model.addAttribute("courseName", "C Programming");
        return "coursesDashboard/CProgramming";
    }

    @GetMapping("/courses/coreJAVA")
    public String coreJava(Model model) {
        model.addAttribute("courseName", "Core Java");
        return "coursesDashboard/CoreJava";
    }

    @GetMapping("/courses/AdvancedJava")
    public String advancedJava(Model model) {
        model.addAttribute("courseName", "Advanced Java");
        return "coursesDashboard/AdvancedJava";
    }
}
