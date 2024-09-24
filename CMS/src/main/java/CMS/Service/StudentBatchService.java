package CMS.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import CMS.Master.StudentBatch;
import CMS.Master.StudentMaster;

public interface StudentBatchService {
  
	List<StudentBatch> getAllStudentBatches();
	Page<StudentBatch> findPaginated(int pageNo, int pageSize);
	StudentBatch addStudentBatch(StudentBatch studentBatch); 
	StudentBatch updateStudentBatch(StudentBatch studentBatch, int id);
	void deleteStudentBatch(int id);
	StudentBatch findStudentBatchById(int id);
	Object getAllStudents();
	Object getAllBatches();
	
	//Student
	List<StudentBatch> getStudentBatchByStudent(StudentMaster studentMaster);
}
