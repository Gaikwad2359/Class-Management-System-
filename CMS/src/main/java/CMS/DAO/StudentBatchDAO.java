package CMS.DAO;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import CMS.Master.StudentBatch;
import CMS.Master.StudentMaster;

public interface StudentBatchDAO extends JpaRepository<StudentBatch, Integer> {

	//Student
	List<StudentBatch> findByStudentMaster(StudentMaster studentMaster);
}