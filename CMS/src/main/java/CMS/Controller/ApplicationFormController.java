package CMS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import CMS.Master.ApplyForm;
import CMS.Service.ApplyFormService;

@Controller
public class ApplicationFormController {

    @Autowired
    private ApplyFormService applyFormService;

    @GetMapping("/applyCourse")
    public String showApplyForm(Model model) {
        model.addAttribute("applyForm", new ApplyForm());
        return "coursesDashboard/applyCourseForm"; // Return the apply form view
    }

    @PostMapping("/submitApplication")
    public String submitApplication(@ModelAttribute("applyForm") ApplyForm applyForm) {
        applyFormService.saveApplyForm(applyForm);
        return "redirect:/success"; // Redirect to the success page after submission
    }

    @GetMapping("/success")
    public String showSuccessPage(Model model) {
        model.addAttribute("message", "Your application has been submitted successfully!");
        return "coursesDashboard/success"; // Returns the success view name
    }
}
