package CMS.Controller;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import CMS.Master.ApplyForm;
import CMS.Service.ApplicationFormService;
import jakarta.validation.Valid;

@Controller
public class ApplicationFormController {

	@Autowired
    private ApplicationFormService applyFormService;

    @GetMapping("/apply")
    public String showApplyForm(Model model) {
        model.addAttribute("applyForm", new ApplyForm());

        // Add the predefined course list to the model
        List<String> courses = Arrays.asList(
                "Full Stack Development", 
                "DSA", 
                "Angular Development", 
                "C Programming", 
                "Core Java", 
                "Advanced Java"
        );
        model.addAttribute("courses", courses);

        return "coursesDashboard/apply-form";  // Use the correct path to your form view
    }

    @PostMapping("/apply")
    public String submitApplyForm(@Valid @ModelAttribute("applyForm") ApplyForm applyForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Add the course list back to the model if there are validation errors
            List<String> courses = Arrays.asList(
                    "Full Stack Development", 
                    "DSA", 
                    "Angular Development", 
                    "C Programming", 
                    "Core Java", 
                    "Advanced Java"
            );
            model.addAttribute("courses", courses);

            // Stay on the same form page to show validation errors
            return "coursesDashboard/apply-form";  // Make sure the path matches your Thymeleaf view
        }

        // Save the form and redirect to avoid re-submission on refresh
        applyFormService.saveApplyForm(applyForm);
        return "redirect:/apply-success";  // Redirect to success page to prevent re-submission
    }

    @GetMapping("/apply-success")
    public String showSuccessPage() {
        return "coursesDashboard/apply-success";  // Success page after submission
    }
    
    @GetMapping("/applyForms")
    public String showApplyForms(Model model) {
        List<ApplyForm> applyForms = applyFormService.getAllApplyForms();
        model.addAttribute("applyForms", applyForms);
        return "coursesDashboard/applyForms"; // Main page displaying all entries with buttons
        
        
    }
    
    @PostMapping("/confirm/{id}")
    public String confirmApplication(@PathVariable Long id) {
        ApplyForm form = applyFormService.getApplyFormById(id);
        form.setStatus("Confirmed");
        applyFormService.saveApplyForm(form);
        return "redirect:/applyForms";  // Redirect back to the main page
    }

    // Handle "Pending" button click
    @PostMapping("/pending/{id}")
    public String pendingApplication(@PathVariable Long id) {
        ApplyForm form = applyFormService.getApplyFormById(id);
        form.setStatus("Pending");
        applyFormService.saveApplyForm(form);
        return "redirect:/applyForms";  // Redirect back to the main page
    }

    // Handle "Interested" button click
    @PostMapping("/interested/{id}")
    public String interestedApplication(@PathVariable Long id) {
        ApplyForm form = applyFormService.getApplyFormById(id);
        form.setStatus("Interested");
        applyFormService.saveApplyForm(form);
        return "redirect:/applyForms";  // Redirect back to the main page
    }

    // Show all applications (the main page)
    @GetMapping
    public String showAllApplications(Model model) {
        List<ApplyForm> applyForms = applyFormService.getAllApplyForms();
        model.addAttribute("applyForms", applyForms);
        return "coursesDashboard/applyForms";  // Main page to show all applications
    }

    // Show only "Confirmed" applications
    @GetMapping("/confirmed")
    public String showConfirmedApplications(Model model) {
        List<ApplyForm> confirmedApplications = applyFormService.getApplicationsByStatus("Confirmed");
        model.addAttribute("applyForms", confirmedApplications);
        return "coursesDashboard/confirmedApplications";
    }

    // Show only "Pending" applications
    @GetMapping("/pending")
    public String showPendingApplications(Model model) {
        List<ApplyForm> pendingApplications = applyFormService.getApplicationsByStatus("Pending");
        model.addAttribute("applyForms", pendingApplications);
        return "coursesDashboard/pendingApplications";
    }

    // Show only "Interested" applications
    @GetMapping("/interested")
    public String showInterestedApplications(Model model) {
        List<ApplyForm> interestedApplications = applyFormService.getApplicationsByStatus("Interested");
        model.addAttribute("applyForms", interestedApplications);
        return "coursesDashboard/interestedApplications";
    }

}
