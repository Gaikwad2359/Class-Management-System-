package CMS.ServiceImpl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import CMS.DAO.LaptopApplyDAO;
import CMS.Master.LaptopApply;
import CMS.Service.LaptopApplyService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class LaptopApplyServiceImpl implements LaptopApplyService {

    @Autowired
    private LaptopApplyDAO laptopApplyDAO;

    @Autowired
    private JavaMailSender mailSender;

    private final String adminEmail = "gaikwadpravin667@gmail.com";  // Admin's email

    @Override
    public LaptopApply saveLaptopApplyForm(LaptopApply laptopApplyForm) {
        laptopApplyDAO.save(laptopApplyForm);
        try {
            sendEmailNotificationToAdmin(laptopApplyForm);
            sendEmailNotificationToStudent(laptopApplyForm);
            System.out.println("Emails sent successfully");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            System.err.println("Error sending email: " + e.getMessage());
        }
        return laptopApplyForm;
    }


    @Override
    public List<LaptopApply> getAllLaptopApplyForms() {
        return laptopApplyDAO.findAll();
    }

    @Override
    public LaptopApply getLaptopApplyFormById(Long id) {
        return laptopApplyDAO.findById(id).orElse(null);
    }

    @Override
    public void deleteLaptopApplyForm(Long id) {
        laptopApplyDAO.deleteById(id);
    }

    private void sendEmailNotificationToAdmin(LaptopApply laptopApplyForm) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(adminEmail);
        helper.setSubject("New Laptop Application");

        // Build HTML content for the email
        String emailContent = buildAdminEmailContent(laptopApplyForm);
        helper.setText(emailContent, true);

        // Embed images in the email
        embedImages(helper);

        mailSender.send(message);
    }

    private void sendEmailNotificationToStudent(LaptopApply laptopApplyForm) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(laptopApplyForm.getEmailId());
        helper.setSubject("Laptop Application Confirmation");

        // Build HTML content for the email
        String emailContent = buildStudentEmailContent(laptopApplyForm);
        helper.setText(emailContent, true);

        // Embed images in the email
        embedImages(helper);

        mailSender.send(message);
    }
    private String buildAdminEmailContent(LaptopApply laptopApplyForm) {
        return "<html>" +
               "<body style='background-color: white; color: black; font-family: Arial, sans-serif; padding: 20px;'>" +
               "<div style='text-align: center;'>" +
               "<img src='cid:companyBlackLogo' style='width: 200px; display: block; margin: auto;' alt='Company Logo'>" +
               "</div>" +
               "<h2 style='text-align: center;'>New Laptop Application</h2>" +
               "<p>A new application for a laptop has been submitted with the following details:</p>" +
               "<ul>" +
               "<li><strong>Full Name:</strong> " + laptopApplyForm.getFullName() + "</li>" +
               "<li><strong>Email ID:</strong> " + laptopApplyForm.getEmailId() + "</li>" +
               "<li><strong>Mobile No:</strong> " + laptopApplyForm.getPhoneNo() + "</li>" +
               "<li><strong>Laptop RAM:</strong> " + laptopApplyForm.getLaptopRam() + "</li>" +
               "<li><strong>Laptop ROM:</strong> " + laptopApplyForm.getLaptopRom() + "</li>" +
               "</ul>" +
               "<p>Best Regards,<br><strong>Zplus Cyber Secure Technologies Pvt. Ltd.</strong></p>" +
               "</body>" +
               "</html>";
    }

    private String buildStudentEmailContent(LaptopApply laptopApplyForm) {
        return "<html>" +
               "<body style='background-color: white; color: black; font-family: Arial, sans-serif; padding: 20px;'>" +
               "<div style='text-align: center;'>" +
               "<img src='cid:companyBlackLogo' style='width: 200px; display: block; margin: auto;' alt='Company Logo'>" +
               "</div>" +
               "<h2 style='text-align: center;'>Laptop Application Confirmation</h2>" +
               "<p>Dear <strong>" + laptopApplyForm.getFullName() + "</strong>,</p>" +
               "<p>Thank you for applying for a laptop with the following requirements:</p>" +
               "<ul>" +
               "<li><strong>Laptop RAM:</strong> " + laptopApplyForm.getLaptopRam() + "</li>" +
               "<li><strong>Laptop ROM:</strong> " + laptopApplyForm.getLaptopRom() + "</li>" +
               "</ul>" +
               "<p>We have received your application and will review it shortly.</p>" +
               "<p>Best Regards,<br><strong>Zplus Cyber Secure Technologies Pvt. Ltd.</strong></p>" +
               "</body>" +
               "</html>";
    }
    
    private void embedImages(MimeMessageHelper helper) throws MessagingException, IOException {
        // Replace the old logo with the new ZplusBlackLogo
        ClassPathResource logoImage = new ClassPathResource("static/images/ZplusBlackLogo.png");

        helper.addInline("companyBlackLogo", logoImage);
    }
}
