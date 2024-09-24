package CMS.DAO;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import CMS.Master.BatchMaster;

public interface BatchDAO extends JpaRepository<BatchMaster, Integer>{

	List<BatchMaster> findByBatchStartDateBetween(LocalDate startDate, LocalDate endDate);
	List<BatchMaster> findByBatchStartDateAfter(LocalDate date);
	
}