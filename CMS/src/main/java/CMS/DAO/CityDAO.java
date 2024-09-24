package CMS.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import CMS.Master.CityMaster;

@Repository
public interface CityDAO extends JpaRepository<CityMaster, Integer>{

}