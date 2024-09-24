package CMS.Controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import CMS.Master.ApplyForm;
import CMS.Master.Contact;
import CMS.Service.ApplicationFormService;
import CMS.Service.ContactService;
import jakarta.validation.Valid;

@Controller
public class GuestController {

	 @Autowired
	 ContactService contactService;
	    
	 @Autowired
     private JavaMailSender mailSender;

    private final String adminEmail = "gaikwadpravin667@gmail.com";
	
	//Guest Home Page 
    @GetMapping("/homePage")
    public String showHomePage() {  // Corrected method name
        return "guest/home";  // This will map to 'guest/home.html'
    }
    
    
  //Student Services Page 
    @GetMapping("/guest/services/software-development")
    public String showSoftwareDevelopment() {
        return "guestServices/software-development";
    }

    @GetMapping("/guest/services/hardware-sales")
    public String showHardwareSales() {
        return "guestServices/hardware-sales";
    }

    @GetMapping("/guest/services/training-certifications")
    public String showTrainingCertifications() {
        return "guestServices/training-certifications";
    }

    @GetMapping("/guest/services/student-augmentation")
    public String showStudentAugmentation() {
        return "guestServices/student-augmentation";
    }

    @GetMapping("/guest/services/project-guidance")
    public String showProjectGuidance() {
        return "guestServices/project-guidance";
    }
    
    

    //About Us Page 
    @GetMapping("/guest/about")
    public String aboutUs() {
        return "guestAboutUs/aboutUs";
    }
    
    
    //Contact Us Form

    //ContactUs Page
    @GetMapping("/guest/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "guestContactUs/ContactUs";
    }

    @PostMapping("/guest/submitContact")
    public String submitContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "guestContactUs/ContactUs";
        }

        try {
            contactService.saveContact(contact);
            sendContactEmail(contact);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "Error";
        }

        return "redirect:/guest/contactSuccess";
    }
    @GetMapping("/guest/contactSuccess")
    public String contactSuccess() {
        return "guestContactUs/contactSuccess";
    }
    


    private void sendContactEmail(Contact contact) {
        // Email to admin
        SimpleMailMessage adminMessage = new SimpleMailMessage();
        adminMessage.setTo(adminEmail);
        adminMessage.setSubject("Zplus STS Contact Form Submission");
        adminMessage.setText("A new Zplus contact has been submitted with the following details:\n\n" +
                             "Name: " + contact.getName() + "\n" +
                             "Email: " + contact.getEmail() + "\n" +
                             "Phone: " + contact.getPhone() + "\n" +
                             "Message: " + contact.getMessage());

        mailSender.send(adminMessage);

        // Email to applicant
        SimpleMailMessage applicantMessage = new SimpleMailMessage();
        applicantMessage.setTo(contact.getEmail());
        applicantMessage.setSubject("Thank You for Contacting Zplus STS");
        applicantMessage.setText("Dear " + contact.getName() + ",\n\n" +
                                 "Thank you for getting in touch with us. We have received your message and will respond to you as soon as possible.\n\n" +
                                 "Here is a copy of your message:\n\n" +
                                 "Name: " + contact.getName() + "\n" +
                                 "Email: " + contact.getEmail() + "\n" +
                                 "Phone: " + contact.getPhone() + "\n" +
                                 "Message: " + contact.getMessage() + "\n\n" +
                                 "Best regards,\n" +
                                 "Zplus STS Team");

        mailSender.send(applicantMessage);
    }

    
    //Courses Page 

    //Courses Code 
    @GetMapping("/guest/courses/FullStackDevelopment")
    public String fullStackDevelopment(Model model) {
        model.addAttribute("courseName", "Java Full Stack Development");
        // Add more course-specific attributes if needed
        return "guestCourses/FullStackDevelopment";
    }

    @GetMapping("/guest/courses/DataStructure")
    public String dataStructure(Model model) {
        model.addAttribute("courseName", "Data Structure and Algorithms");
        return "guestCourses/DataStructure";
    }

    @GetMapping("/guest/courses/Angular")
    public String angular(Model model) {
        model.addAttribute("courseName", "Angular Development");
        return "guestCourses/Angular";
    }

    @GetMapping("/guest/courses/CProgramming")
    public String cProgramming(Model model) {
        model.addAttribute("courseName", "C Programming");
        return "guestCourses/CProgramming";
    }

    @GetMapping("/guest/courses/coreJAVA")
    public String coreJava(Model model) {
        model.addAttribute("courseName", "Core Java");
        return "guestCourses/CoreJava";
    }

    @GetMapping("/guest/courses/AdvancedJava")
    public String advancedJava(Model model) {
        model.addAttribute("courseName", "Advanced Java");
        return "guestCourses/AdvancedJava";
    }
    
    @Autowired
    private ApplicationFormService applyFormService;

    @GetMapping("/guest/ApplyCoursePage")
    public String showApplyForm(Model model) {
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

        return "guestCourses/apply-form";
    }

    @PostMapping("/guest/ApplyStudentCourse")
    public String submitApplyForm(@Valid @ModelAttribute("applyForm") ApplyForm applyForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<String> courses = Arrays.asList(
                    "Full Stack Development", 
                    "DSA", 
                    "Angular Development", 
                    "C Programming", 
                    "Core Java", 
                    "Advanced Java"
            );
            model.addAttribute("courses", courses);

            return "guestCourses/apply-form";
        }
        applyFormService.saveApplyForm(applyForm);
        return "guestCourses/apply-success";  // Directly returning the Thymeleaf template
    }


    @GetMapping("/guest/apply-success")
    public String showSuccessPage() {
        return "guestCourses/apply-success";
    }
    
}
