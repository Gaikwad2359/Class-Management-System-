package CMS.ServiceImpl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import CMS.DAO.ApplicationFormRepository;
import CMS.Master.ApplyForm;
import CMS.Service.ApplicationFormService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ApplicationFormServiceImpl implements ApplicationFormService {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationFormServiceImpl.class);

    @Autowired
    private ApplicationFormRepository applyFormRepository;

    @Autowired
    private JavaMailSender mailSender;

    private final String adminEmail = "gaikwadpravin667@gmail.com";  // Admin's email

    @Override
    public ApplyForm saveApplyForm(ApplyForm applyForm) {
        applyFormRepository.save(applyForm);
        // Try sending the email notifications, but don't prevent saving if emails fail
        try {
            logger.info("Attempting to send email notification to admin...");
            sendEmailNotificationToAdmin(applyForm);
            logger.info("Email notification sent to admin successfully.");

            logger.info("Attempting to send email notification to student...");
            sendEmailNotificationToStudent(applyForm);
            logger.info("Email notification sent to student successfully.");
        } catch (MessagingException | IOException e) {
            logger.error("Error occurred while sending email notifications: ", e);
        }
        return applyForm;
    }

    @Override
    public List<ApplyForm> getAllApplyForms() {
        return applyFormRepository.findAll();
    }

    @Override
    public void deleteApplyForm(Long id) {
        applyFormRepository.deleteById(id);
    }

    private void sendEmailNotificationToAdmin(ApplyForm applyForm) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(adminEmail);
        helper.setSubject("New Student Application");

        String emailContent = buildAdminEmailContent(applyForm);
        helper.setText(emailContent, true);
        embedImages(helper);
        attachCourseImage(helper, applyForm.getCourse());
        mailSender.send(message);
    }

    private void sendEmailNotificationToStudent(ApplyForm applyForm) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(applyForm.getEmailId());
        helper.setSubject("Application Confirmation");

        String emailContent = buildStudentEmailContent(applyForm);
        helper.setText(emailContent, true);
        embedImages(helper);
        attachCourseImage(helper, applyForm.getCourse());
        mailSender.send(message);
    }

    private void embedImages(MimeMessageHelper helper) throws MessagingException, IOException {
        ClassPathResource logoImage = new ClassPathResource("static/images/ZplusLogo.png");
        ClassPathResource classesImage = new ClassPathResource("static/images/classes.png");
        helper.addInline("zplusLogo", logoImage);
        helper.addInline("classesImage", classesImage);
    }

    private void attachCourseImage(MimeMessageHelper helper, String course) throws MessagingException, IOException {
        String imageName = null;
        switch (course) {
            case "Full Stack Development":
                imageName = "fullstackdevelopment.png";
                break;
            case "DSA":
                imageName = "DSA.png";
                break;
            case "Angular Development":
                imageName = "angulardevelopment.png";
                break;
            case "C Programming":
                imageName = "cprogramming.png";
                break;
            case "Core Java":
                imageName = "coreJava.png";
                break;
            case "Advanced Java":
                imageName = "advanceJava.png";
                break;
        }

        if (imageName != null) {
            ClassPathResource image = new ClassPathResource("static/images/" + imageName);
            helper.addInline(course + "Image", image);
        }
    }

    private String buildAdminEmailContent(ApplyForm applyForm) {
        return "<html><body style='background-color: black; color: white;'>" +
               "<img src='cid:zplusLogo' style='width: 200px; display: block; margin: auto;' alt='Zplus Logo'>" +
               "<h2 style='text-align: center;'>New Student Application</h2>" +
               "<p>A new student has applied for the " + applyForm.getCourse() + " course.</p>" +
               "<p><strong>Full Name:</strong> " + applyForm.getFullName() + "</p>" +
               "<p><strong>Email ID:</strong> " + applyForm.getEmailId() + "</p>" +
               "<p><strong>Mobile No:</strong> " + applyForm.getMobileNo() + "</p>" +
               "<p><strong>Course:</strong> " + applyForm.getCourse() + "</p>" +
               "<br><img src='cid:classesImage' style='width: 100%;' alt='Classes'>" +
               "<p>Best Regards,<br>Zplus Cyber Secure Technologies Pvt Ltd</p>" +
               "</body></html>";
    }

    private String buildStudentEmailContent(ApplyForm applyForm) {
        return "<html><body style='background-color: black; color: white;'>" +
               "<img src='cid:zplusLogo' style='width: 200px; display: block; margin: auto;' alt='Zplus Logo'>" +
               "<h2 style='text-align: center;'>Application Confirmation</h2>" +
               "<p>Congratulations " + applyForm.getFullName() + "!</p>" +
               "<p>Thank you for applying for the " + applyForm.getCourse() + " course.</p>" +
               "<p>We have received your application and will get back to you soon.</p>" +
               "<br><img src='cid:classesImage' style='width: 100%;' alt='Classes'>" +
               "<p>Best Regards,<br>Zplus Cyber Secure Technologies Pvt Ltd</p>" +
               "</body></html>";
    }
	    
    
    public List<ApplyForm> getApplicationsByStatus(String status) {
        return applyFormRepository.findByStatus(status); // Assuming there's a findByStatus method in the repository
    }

    public ApplyForm getApplyFormById(Long id) {
        return applyFormRepository.findById(id).orElseThrow(() -> new RuntimeException("Application not found"));
    }

    public void save(ApplyForm applyForm) {
        applyFormRepository.save(applyForm); // Save changes to the database
    }

}
