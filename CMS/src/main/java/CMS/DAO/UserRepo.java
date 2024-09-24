package CMS.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import CMS.Master.StudentMaster;

public interface UserRepo extends JpaRepository<StudentMaster, Integer> {

	public StudentMaster findByEmail(String emaill);

	public StudentMaster findByVerificationCode(String code);
	
}
