package CMS.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import CMS.Master.BatchMaster;
import CMS.Master.Notes;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Long> {

	//Student Notes
	List<Notes> findByBatchMasterIn(List<BatchMaster> batchMasters);
}
