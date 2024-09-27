package CMS.ServiceImpl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import CMS.DAO.ApplicationFormRepository;
import CMS.Master.ApplyForm;
import CMS.Service.ApplyFormService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ApplyFormServiceImpl implements ApplyFormService {

    @Autowired
    private ApplicationFormRepository applyFormRepository;

    @Autowired
    private JavaMailSender mailSender;

    private final String adminEmail = "gaikwadpravin667@gmail.com"; // Admin's email

    @Override
    public ApplyForm saveApplyForm(ApplyForm applyForm) {
        applyFormRepository.save(applyForm);
        try {
            sendEmailNotificationToAdmin(applyForm);
            sendEmailNotificationToStudent(applyForm);
            System.out.println("Emails sent successfully");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            System.err.println("Error sending email: " + e.getMessage());
        }
        return applyForm;
    }

    @Override
    public List<ApplyForm> getAllApplyForms() {
        return applyFormRepository.findAll();
    }

    @Override
    public ApplyForm getApplyFormById(Long id) {
        return applyFormRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteApplyForm(Long id) {
        applyFormRepository.deleteById(id);
    }

    private void sendEmailNotificationToAdmin(ApplyForm applyForm) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(adminEmail);
        helper.setSubject("New Course Application");

        // Build HTML content for the email
        String emailContent = buildAdminEmailContent(applyForm);
        helper.setText(emailContent, true);

        // Embed images in the email
        embedImages(helper, applyForm.getCourse());

        mailSender.send(message);
    }

    private void sendEmailNotificationToStudent(ApplyForm applyForm) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(applyForm.getEmailId());
        helper.setSubject("Course Application Confirmation");

        // Build HTML content for the email
        String emailContent = buildStudentEmailContent(applyForm);
        helper.setText(emailContent, true);

        // Embed images in the email
        embedImages(helper, applyForm.getCourse());

        mailSender.send(message);
    }

    private String buildAdminEmailContent(ApplyForm applyForm) {
        return "<html>" +
               "<body style='background-color: white; color: black; font-family: Arial, sans-serif; padding: 20px;'>" +
               "<div style='text-align: center;'>" +
               "<img src='cid:companyBlackLogo' style='width: 200px; display: block; margin: auto;' alt='Company Logo'>" +
               "</div>" +
               "<h2 style='text-align: center;'>New Course Application</h2>" +
               "<p>A new application for a course has been submitted with the following details:</p>" +
               "<ul>" +
               "<li><strong>Full Name:</strong> " + applyForm.getFullName() + "</li>" +
               "<li><strong>Email ID:</strong> " + applyForm.getEmailId() + "</li>" +
               "<li><strong>Mobile No:</strong> " + applyForm.getMobileNo() + "</li>" +
               "<li><strong>Course:</strong> " + applyForm.getCourse() + "</li>" +
               "</ul>" +
               "<p>Best Regards,<br><strong>Zplus Cyber Secure Technologies Pvt. Ltd.</strong></p>" +
               "</body>" +
               "</html>";
    }

    private String buildStudentEmailContent(ApplyForm applyForm) {
        return "<html>" +
               "<body style='background-color: white; color: black; font-family: Arial, sans-serif; padding: 20px;'>" +
               "<div style='text-align: center;'>" +
               "<img src='cid:companyBlackLogo' style='width: 200px; display: block; margin: auto;' alt='Company Logo'>" +
               "</div>" +
               "<h2 style='text-align: center;'>Course Application Confirmation</h2>" +
               "<p>Dear <strong>" + applyForm.getFullName() + "</strong>,</p>" +
               "<p>Thank you for applying for the following course:</p>" +
               "<ul>" +
               "<li><strong>Course:</strong> " + applyForm.getCourse() + "</li>" +
               "</ul>" +
               "<p>We have received your application and will review it shortly.</p>" +
               "<p>Best Regards,<br><strong>Zplus Cyber Secure Technologies Pvt. Ltd.</strong></p>" +
               "</body>" +
               "</html>";
    }

    private void embedImages(MimeMessageHelper helper, String courseName) throws MessagingException, IOException {
        // Embed company logo
        ClassPathResource logoImage = new ClassPathResource("static/images/ZplusBlackLogo.png");
        helper.addInline("companyBlackLogo", logoImage);

        // Embed course-specific images
        ClassPathResource courseImage = new ClassPathResource("static/images/" + getImageFileName(courseName));
        helper.addInline("courseImage", courseImage);
    }

    private String getImageFileName(String courseName) {
        switch (courseName.toLowerCase()) {
            case "full stack development":
                return "fullstackdevelopment.png";
            case "dsa":
                return "DSA.png";
            case "angular development":
                return "angulardevelopment.png";
            case "core java":
                return "coreJava.png";
            case "c programming":
                return "cprogramming.png";
            case "advanced java":
                return "advanceJava.png";
            default:
                return "fullstackdevelopment.png"; // Fallback image if course is not recognized
        }
    }
}
