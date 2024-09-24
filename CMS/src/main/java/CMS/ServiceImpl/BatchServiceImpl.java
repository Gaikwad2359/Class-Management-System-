package CMS.ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import CMS.DAO.BatchDAO;
import CMS.Master.BatchMaster;
import CMS.Service.BatchService;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    BatchDAO dao;

	@Override
	public List<BatchMaster> getAllBatches() {
		return dao.findAll();
	}

	@Override
	public Page<BatchMaster> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return dao.findAll(pageable);
	}

	@Override
	public BatchMaster addBatch(BatchMaster batchMaster) {
		return dao.save(batchMaster);
	}

	@Override
	public BatchMaster updateBatch(BatchMaster batchMaster, int id) {
		return dao.save(batchMaster);
	}

	@Override
	public void deleteBatch(int id) {
		dao.deleteById((int) id);				
	}

	@Override
	public BatchMaster findBatchById(int id) {
		Optional<BatchMaster> BatchMaster = dao.findById((int) id);
		return BatchMaster.orElse(null);
	}

	@Override
	public Object getAllCourses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAllFaculties() {
		// TODO Auto-generated method stub
		return null;
	}

	 @Override
	    public List<BatchMaster> findBatchesWithinDates(LocalDate startDate, LocalDate endDate) {
	        return dao.findByBatchStartDateBetween(startDate, endDate);
	    }

	 
	 
	
}
