package CMS.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import CMS.Master.BatchMaster;
import CMS.Master.DailyUpdate;

public interface DailyUpdateService {

    List<DailyUpdate> getAllUpdates();
    Page<DailyUpdate> findPaginated(int pageNo, int pageSize);
    DailyUpdate addUpdate(DailyUpdate dailyUpdate); 
    DailyUpdate updateUpdate(DailyUpdate dailyUpdate, int id);
    void deleteUpdate(int id);
    DailyUpdate findUpdateById(int id);
    Object getAllBatches();
    void updateUpdate(DailyUpdate dailyUpdate);
    
    //Student Daily Update
    List<DailyUpdate> getUpdatesForBatches(List<BatchMaster> batchMasters);
}
