package CMS.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import CMS.Master.StudentReference;

@Repository
public interface StudentReferenceDAO extends JpaRepository<StudentReference, Integer> {

	 List<StudentReference> findByStudentMaster_StudentId(Integer studentId);
	
}
