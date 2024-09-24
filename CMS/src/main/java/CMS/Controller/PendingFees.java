package CMS.Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import CMS.Master.StudentMaster;
import CMS.Service.StudentService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Controller
public class PendingFees {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/report/studentBalance")
    public ModelAndView generateStudentBalanceReport() {
        ModelAndView modelAndView = new ModelAndView("report/studentBalanceReport");

        try {
            List<StudentMaster> students = studentService.findStudentsWithBalanceGreaterThanZero();
            modelAndView.addObject("students", students);

            // Calculate the total pending balance
            double totalPendingBalance = students.stream()
                    .mapToDouble(StudentMaster::getStudentbalance)
                    .sum();
            modelAndView.addObject("totalPendingBalance", totalPendingBalance);
        } catch (Exception e) {
            modelAndView.setViewName("report/error");
            modelAndView.addObject("message", "An unexpected error occurred: " + e.getMessage());
        }

        return modelAndView;
    }

    @GetMapping("/report/studentBalance/remainder")
    public ModelAndView sendPendingBalanceReports() {
        ModelAndView modelAndView = new ModelAndView("report/studentBalanceReport");

        try {
            List<StudentMaster> students = studentService.findStudentsWithBalanceGreaterThanZero();

            for (StudentMaster student : students) {
                ByteArrayInputStream pdfStream = generateStudentBalancePDF(student);
                sendEmailWithAttachment(student.getStudentEmailId(), "Pending Balance Report", "Please find your pending balance report attached.", pdfStream);
            }

            modelAndView.addObject("message", "Remainder emails sent successfully.");
        } catch (Exception e) {
            modelAndView.setViewName("report/error");
            modelAndView.addObject("message", "An unexpected error occurred: " + e.getMessage());
        }

        return modelAndView;
    }

    private ByteArrayInputStream generateStudentBalancePDF(StudentMaster student) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(25, 750);
            contentStream.showText("Student Balance Report");
            contentStream.newLine();
            contentStream.showText("Student ID: " + student.getStudentId());
            contentStream.newLine();
            contentStream.showText("Student Name: " + student.getStudentFirstName() + " " + student.getStudentLastName());
            contentStream.newLine();
            contentStream.showText("Pending Balance: " + student.getStudentbalance());
            contentStream.endText();
            contentStream.close();

            document.save(out);
            document.close();

            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private void sendEmailWithAttachment(String to, String subject, String text, ByteArrayInputStream attachment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        // Create email with attachment
        ByteArrayResource resource = new ByteArrayResource(attachment.readAllBytes());
        helper.addAttachment("PendingBalanceReport.pdf", resource);

        mailSender.send(message);
    }
    
    @PostMapping("/report/send-reminder")
    public ModelAndView sendReminderEmails() {
        ModelAndView modelAndView = new ModelAndView("report/studentBalanceReport");
        
        try {
            List<StudentMaster> students = studentService.findStudentsWithBalanceGreaterThanZero();
            for (StudentMaster student : students) {
                sendReminderEmail(student);
            }
            modelAndView.addObject("message", "Reminder emails have been sent successfully.");
        } catch (Exception e) {
            modelAndView.setViewName("report/error");
            modelAndView.addObject("message", "An unexpected error occurred while sending reminders: " + e.getMessage());
        }

        modelAndView.addObject("students", studentService.findStudentsWithBalanceGreaterThanZero());
        return modelAndView;
    }

    private void sendReminderEmail(StudentMaster student) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // 'true' indicates HTML content

            helper.setTo(student.getStudentEmailId());
            helper.setSubject("Pending Balance Reminder");

            // Constructing the HTML email content
            String htmlContent = "<html><body>"
                    + "<p>Dear " + student.getStudentFirstName() + " " + student.getStudentLastName() + ",</p>"
                    + "<p>This is a reminder that you have a pending balance of " + student.getStudentbalance() + ".</p>"
                    + "<p>Please make sure to settle your balance at your earliest convenience.</p>"
                    + "<p>If you have any questions regarding the pending fees, please mail us at <a href=\"mailto:info@zpluscybertech.com\">info@zpluscybertech.com</a> or WhatsApp us at +91 9850083751 | +91 6362803282.</p>"
                    + "<p>Warmest regards,</p>"
                    + "<p>Sham Gaikwad<br>"
                    + "CEO Zplus Cyber Secure Technologies Pvt Ltd.<br>"
                    + "Email: <a href=\"mailto:info@zpluscybertech.com\">info@zpluscybertech.com</a><br>"
                    + "Phone: +91 9850083751 | +91 6362803282</p>"
                    + "</body></html>";

            // Set the email content
            helper.setText(htmlContent, true);

            // Add logo as an inline resource
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Handle the exception as per your requirement
        }
    }
}
