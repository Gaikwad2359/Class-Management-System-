package CMS.ServiceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import CMS.DAO.BatchDAO;
import CMS.DAO.NotesRepository;
import CMS.DAO.StudentLoginInstallmentRepository;
import CMS.DAO.UserRepo;
import CMS.Master.StudentInstallmentMaster;
import CMS.Master.StudentMaster;
import CMS.Service.UserService;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StudentLoginInstallmentRepository installmentRepository;

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private BatchDAO batchDAO;

    @Override
    public StudentMaster saveStudentMaster(StudentMaster student, String url, boolean isAdmin) {
        // Encrypt the password
        String password = passwordEncoder.encode(student.getPassword());
        student.setPassword(password);

        // Set role based on isAdmin and isGuest flags
        if (isAdmin) {
            student.setRole("ROLE_ADMIN");
        }  else {
            student.setRole("ROLE_USER");
        }

        student.setEnable(false);
        student.setVerificationCode(UUID.randomUUID().toString());

        StudentMaster newuser = userRepo.save(student);

        if (newuser != null) {
            sendEmail(newuser, url);
        }

        return newuser;
    }

    @Override
    public void sendEmail(StudentMaster student, String url) {
        String from = "gaikwadpravin667@gmail.com";
        String to = student.getEmail();
        String subject = "Account Verification";
        String content = "Dear [[name]],<br>" +
                "Please click the link below to verify your registration:<br>" +
                "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" +
                "Thank you,<br>" +
                "Zplus Cyber Secure Tech";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(from, "Zplus");
            helper.setTo(to);
            helper.setSubject(subject);

            content = content.replace("[[name]]", student.getName());
            String siteUrl = url + "/verify?code=" + student.getVerificationCode();

            content = content.replace("[[URL]]", siteUrl);

            helper.setText(content, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean verifyAccount(String verificationCode) {
        StudentMaster student = userRepo.findByVerificationCode(verificationCode);

        if (student == null) {
            return false;
        } else {
            student.setEnable(true);
            student.setVerificationCode(null);

            userRepo.save(student);

            return true;
        }
    }

    @Override
    public void removeSessionMessage() {
        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
                .getSession();

        session.removeAttribute("msg");
    }

    // Student Installments 
    @Override
    public StudentMaster findStudentByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public List<StudentInstallmentMaster> getInstallmentsByStudent(StudentMaster studentMaster) {
        return installmentRepository.findByStudentMaster(studentMaster);
    }
}
