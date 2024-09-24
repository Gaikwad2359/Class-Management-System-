package CMS.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServiceController {

    @GetMapping("/services/software-development")
    public String showSoftwareDevelopment() {
        return "Service/software-development";
    }

    @GetMapping("/services/hardware-sales")
    public String showHardwareSales() {
        return "Service/hardware-sales";
    }

    @GetMapping("/services/training-certifications")
    public String showTrainingCertifications() {
        return "Service/training-certifications";
    }

    @GetMapping("/services/student-augmentation")
    public String showStudentAugmentation() {
        return "Service/student-augmentation";
    }

    @GetMapping("/services/project-guidance")
    public String showProjectGuidance() {
        return "Service/project-guidance";
    }
}

