package CMS.DAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import CMS.Master.ApplyForm;

@Repository
public interface ApplicationFormRepository extends JpaRepository<ApplyForm, Long> {
}
