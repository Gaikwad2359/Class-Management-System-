package CMS.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import CMS.Master.StudentMaster;

@Repository
public interface StudentDAO extends JpaRepository<StudentMaster, Integer>{

	
	@Query("SELECT s FROM StudentMaster s WHERE s.id = :id")
	Optional<StudentMaster> findStudentById(@Param("id") Integer id);
	
	List<StudentMaster> findByStudentbalanceGreaterThan(double amount);
	
	List<StudentMaster> findByStudentFirstNameContainingAndStudentLastNameContaining(
	        String studentFirstName, String studentLastName);
	
	
	StudentMaster findByStudentEmailId(String email);
	
//	public StudentMaster findByEmail(String emaill);
//
//	public StudentMaster findByVerificationCode(String code);

}