package CMS.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import CMS.Master.StudentMaster;

public interface StudentService {
	
     List<StudentMaster> getAllStudents();

    Page<StudentMaster> findPaginated(int pageNo, int pageSize);

    StudentMaster addStudent(StudentMaster studentMaster);

    StudentMaster updateStudent(StudentMaster studentMaster, int id);

    void deleteStudent(int id);

    StudentMaster findStudentById(int id);

    Object getAllCities();

    Object getAllStates();

    Object getAllCountries();

    Object getAllQualifications();

    List<StudentMaster> findStudentsWithBalanceGreaterThanZero();

    List<StudentMaster> searchByStudentName(String studentFirstName, String studentLastName);
    
    StudentMaster getStudentByEmail(String studentEmailId);
    
    List<StudentMaster> findAll();
    
    
    //login

//    public StudentMaster saveStudent(StudentMaster student,String url);
//
//	public void removeSessionMessage();
//
//	public void sendEmail(StudentMaster student, String path);
//
//	public boolean verifyAccount(String verificationCode);
}