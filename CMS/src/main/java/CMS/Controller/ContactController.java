package CMS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import CMS.Master.Contact;
import CMS.Service.ContactService;
import jakarta.validation.Valid;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private JavaMailSender mailSender;

    private final String adminEmail = "gaikwadpravin667@gmail.com";

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "Contact/ContactUs";
    }

    @PostMapping("/submitContact")
    public String submitContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "Contact/ContactUs";
        }

        try {
            contactService.saveContact(contact);
            sendContactEmail(contact);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "Error";
        }

        return "redirect:/contactSuccess";
    }

    @PostMapping("/submitContactAlt")
    public String submitContactAlternative(@Valid @ModelAttribute("contact") Contact contact, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "Contact/ContactUs";
        }

        try {
            contactService.saveContact(contact);
            sendContactEmail(contact);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "Error";
        }

        return "redirect:/contactSuccess";
    }

    @GetMapping("/contactSuccess")
    public String contactSuccess() {
        return "Contact/contactSuccess";
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
}
