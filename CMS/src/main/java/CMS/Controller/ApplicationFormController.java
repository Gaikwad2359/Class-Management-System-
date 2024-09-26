package CMS.Controller;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(ApplicationFormController.class);

    @Autowired
    private ApplicationFormService applyFormService;

    @GetMapping("/apply")
    public String showApplyForm(Model model) {
        logger.info("Loading apply form page.");
        model.addAttribute("applyForm", new ApplyForm());

        List<String> courses = Arrays.asList(
                "Full Stack Development", 
                "DSA", 
                "Angular Development", 
                "C Programming", 
                "Core Java", 
                "Advanced Java"
        );
        model.addAttribute("courses", courses);

        return "coursesDashboard/apply-form";  // Path to the form view
    }

    @PostMapping("/applyCourse")
    public String submitApplyForm(@Valid @ModelAttribute("applyForm") ApplyForm applyForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Validation errors found while submitting apply form.");
            List<String> courses = Arrays.asList(
                    "Full Stack Development", 
                    "DSA", 
                    "Angular Development", 
                    "C Programming", 
                    "Core Java", 
                    "Advanced Java"
            );
            model.addAttribute("courses", courses);

            return "coursesDashboard/apply-form";  // Return to the form page if errors
        }

        logger.info("Saving apply form for student: {}", applyForm.getFullName());
        applyFormService.saveApplyForm(applyForm);
        return "redirect:/apply-success";  // Redirect to success page after submission
    }

    @GetMapping("/apply-success")
    public String showSuccessPage() {
        logger.info("Displaying success page after form submission.");
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
