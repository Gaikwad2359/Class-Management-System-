package CMS.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import CMS.DAO.DailyUpdateRepository;
import CMS.Master.BatchMaster;
import CMS.Master.DailyUpdate;
import CMS.Service.DailyUpdateService;

@Service
public class DailyUpdateServiceImpl implements DailyUpdateService {

    @Autowired
    DailyUpdateRepository dao;

    @Override
    public List<DailyUpdate> getAllUpdates() {
        return dao.findAll();
    }

    @Override
    public Page<DailyUpdate> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return dao.findAll(pageable);
    }

    @Override
    public DailyUpdate addUpdate(DailyUpdate dailyUpdate) {
        return dao.save(dailyUpdate);
    }

    @Override
    public DailyUpdate updateUpdate(DailyUpdate dailyUpdate, int id) {
        Optional<DailyUpdate> existingUpdate = dao.findById(id);
        if (existingUpdate.isPresent()) {
            DailyUpdate update = existingUpdate.get();
            update.setMessage(dailyUpdate.getMessage());
            update.setUpdateDate(null);
            update.setBatchMaster(null);
            return dao.save(update);
        } else {
            throw new RuntimeException("Update not found with id " + id);
        }
    }

    @Override
    public void deleteUpdate(int id) {
        dao.deleteById(id);
    }

    @Override
    public DailyUpdate findUpdateById(int id) {
        Optional<DailyUpdate> dailyUpdate = dao.findById(id);
        return dailyUpdate.orElse(null);
    }

    @Override
    public Object getAllBatches() {
        // Implement logic to get all batches, if required.
        return null;
    }

    @Override
    public void updateUpdate(DailyUpdate dailyUpdate) {
        DailyUpdate existingUpdate = dao.findById(dailyUpdate.getUpdateId())
            .orElseThrow(() -> new RuntimeException("Update not found with id " + dailyUpdate.getUpdateId()));
        existingUpdate.setMessage(dailyUpdate.getMessage());
        existingUpdate.setUpdateDate(dailyUpdate.getUpdateDate());
        existingUpdate.setBatchMaster(dailyUpdate.getBatchMaster());
        dao.save(existingUpdate);
    }
    
    //Student Daily Update
    @Override
    public List<DailyUpdate> getUpdatesForBatches(List<BatchMaster> batchMasters) {
        return dao.findByBatchMasterIn(batchMasters);
    }
}
