package CMS.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import CMS.Master.BatchMaster;
import CMS.Master.DailyUpdate;

public interface DailyUpdateRepository extends JpaRepository<DailyUpdate, Integer> {

	//Particular Student Message
	 List<DailyUpdate> findByBatchMasterIn(List<BatchMaster> batchMasters);
}