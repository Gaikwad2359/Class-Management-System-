package CMS.Service;

import java.util.List;

import CMS.Master.StudentInstallmentMaster;
import CMS.Master.StudentMaster;

public interface UserService {

	
	public StudentMaster saveStudentMaster(StudentMaster student,String url, boolean isAdmin);

	public void removeSessionMessage();

	public void sendEmail(StudentMaster student, String path);

	public boolean verifyAccount(String verificationCode);
	
	
	//StudentInstallment
	StudentMaster findStudentByEmail(String email);
    List<StudentInstallmentMaster> getInstallmentsByStudent(StudentMaster studentMaster);
    
}
