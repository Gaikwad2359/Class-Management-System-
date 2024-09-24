package CMS.DAO;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import CMS.Master.FacultyMaster;

public interface FacultyDAO extends JpaRepository<FacultyMaster, Integer> {

	List<FacultyMaster> findByFacultyBalanceGreaterThan(double balance);
	
}
