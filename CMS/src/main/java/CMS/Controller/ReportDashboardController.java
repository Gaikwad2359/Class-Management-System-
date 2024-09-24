package CMS.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportDashboardController {

    @GetMapping("/reports")
    public String showReportDashboard(Model model) {
        // Add any necessary attributes to the model here
        return "/ReportDash/ReportDashboard"; // This corresponds to the ReportDashboard.html template
    }
}
