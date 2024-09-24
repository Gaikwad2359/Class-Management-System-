package CMS.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import CMS.Master.StudentReference;

public interface StudentReferenceService {

	List<StudentReference> getAllStudentReferences();
	Page<StudentReference> findPaginated(int pageNo, int pageSize);
	StudentReference addStudentReference(StudentReference studentReference); 
	StudentReference updateStudentReference(StudentReference studentReference, int id);
	void deleteStudentReference(int id);
	StudentReference findStudentReferenceById(int id);
	Object getAllStudents();
	void updateStudentReference(StudentReference studentReference);
	//Reference Student 
	 List<StudentReference> getStudentReferencesByStudentId(Integer studentId);
}
