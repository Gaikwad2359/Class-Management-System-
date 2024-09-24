package CMS.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import CMS.Master.BatchMaster;
import CMS.Master.CourseMaster;
import CMS.Service.NotificationService;
@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notifications")
    public String getNotifications(Model model) {
        List<BatchMaster> upcomingBatches = notificationService.getUpcomingBatches();
        List<CourseMaster> upcomingCourses = notificationService.getUpcomingCourses();

        model.addAttribute("upcomingBatches", upcomingBatches);
        model.addAttribute("upcomingCourses", upcomingCourses);

        return "Notifications/notifications";
    }
}