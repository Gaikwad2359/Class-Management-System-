package CMS.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import CMS.Master.BatchMaster;

public interface BatchService {

	List<BatchMaster> getAllBatches();
	Page<BatchMaster> findPaginated(int pageNo, int pageSize);
	BatchMaster addBatch(BatchMaster batchMaster); 
	BatchMaster updateBatch(BatchMaster batchMaster, int id);
	void deleteBatch(int id);
	BatchMaster findBatchById(int id);
	Object getAllCourses();
	Object getAllFaculties();
	List<BatchMaster> findBatchesWithinDates(LocalDate startDate, LocalDate endDate);

}