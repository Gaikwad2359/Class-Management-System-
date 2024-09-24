package CMS.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import CMS.Master.QualificationMaster;

@Repository
public interface QualificationDAO extends JpaRepository<QualificationMaster, Integer>{

}
