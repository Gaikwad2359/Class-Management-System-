package CMS.DAO;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import CMS.Master.ApplyForm;

@Repository
public interface ApplicationFormRepository extends JpaRepository<ApplyForm, Long> {
	List<ApplyForm> findByStatus(String status);
}
