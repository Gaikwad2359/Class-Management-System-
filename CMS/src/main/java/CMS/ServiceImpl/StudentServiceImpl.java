package CMS.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import CMS.DAO.StudentDAO;
import CMS.Master.StudentMaster;
import CMS.Service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentDAO dao;

    
    @Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender mailSender;
	
    @Override
    public List<StudentMaster> getAllStudents() {
        return dao.findAll();
    }

    @Override
    public Page<StudentMaster> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return dao.findAll(pageable);
    }

    @Override
    public StudentMaster addStudent(StudentMaster studentMaster) {
        return dao.save(studentMaster);
    }

    @Override
    public StudentMaster updateStudent(StudentMaster studentMaster, int id) {
        return dao.save(studentMaster);
    }

    @Override
    public void deleteStudent(int id) {
        dao.deleteById(id);
    }

    @Override
    public StudentMaster findStudentById(int id) {
        Optional<StudentMaster> studentMaster = dao.findById( id);
        return studentMaster.orElse(null);
    }

    @Override
    public Object getAllCities() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getAllStates() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getAllCountries() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getAllQualifications() {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public List<StudentMaster> findStudentsWithBalanceGreaterThanZero() {
		return dao.findByStudentbalanceGreaterThan(0);
	}

	@Override
    public List<StudentMaster> searchByStudentName(String studentFirstName, String studentLastName) {
        // Handle null or empty parameters
        if (studentFirstName == null) studentFirstName = "";
        if (studentLastName == null) studentLastName = "";

        // Search by first name and last name
        return dao.findByStudentFirstNameContainingAndStudentLastNameContaining(studentFirstName, studentLastName);
    }
	
	 @Override
	    public StudentMaster getStudentByEmail(String studentEmailId) {
	        return dao.findByStudentEmailId(studentEmailId);
	    }
	 
	 @Override
	    public List<StudentMaster> findAll() {
	        return dao.findAll();
	    }

	 //login
	 
//	@Override
//	public StudentMaster saveStudent(StudentMaster student, String url) {
//		String password = passwordEncoder.encode(student.getPassword());
//		student.setPassword(password);
//		student.setRole("ROLE_USER");
//
//		student.setEnable(false);
//		student.setVerificationCode(UUID.randomUUID().toString());
//
//		StudentMaster newstudent = dao.save(student);
//
//		if (newstudent != null) {
//			sendEmail(newstudent, url);
//		}
//
//		return newstudent;
//	}
//
//	@Override
//	public void removeSessionMessage() {
//		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
//				.getSession();
//
//		session.removeAttribute("msg");
//	}
//
//	@Override
//	public void sendEmail(StudentMaster user, String url) {
//		String from = "gaikwadpravin667@gmail.com";
//		String to = user.getEmail();
//		String subject = "Account Verfication";
//		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
//				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Becoder";
//
//		try {
//
//			MimeMessage message = mailSender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(message);
//
//			helper.setFrom(from, "ZPlus");
//			helper.setTo(to);
//			helper.setSubject(subject);
//
//			content = content.replace("[[name]]", user.getStudentFirstName());
//			String siteUrl = url + "/verify?code=" + user.getVerificationCode();
//
//			System.out.println(siteUrl);
//
//			content = content.replace("[[URL]]", siteUrl);
//
//			helper.setText(content, true);
//
//			mailSender.send(message);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Override
//	public boolean verifyAccount(String verificationCode) {
//		StudentMaster student = dao.findByVerificationCode(verificationCode);
//
//		if (student == null) {
//			return false;
//		} else {
//
//			student.setEnable(true);
//			student.setVerificationCode(null);
//
//			dao.save(student);
//
//			return true;
//		}
//
//}
}