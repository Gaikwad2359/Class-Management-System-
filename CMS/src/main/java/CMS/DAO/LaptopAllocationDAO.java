package CMS.DAO;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import CMS.Master.LaptopAllocationMaster;
import CMS.Master.StudentMaster;

public interface LaptopAllocationDAO extends JpaRepository<LaptopAllocationMaster, Integer> {

	//Student Laptop
	List<LaptopAllocationMaster> findByStudentMaster(StudentMaster studentMaster);
}
