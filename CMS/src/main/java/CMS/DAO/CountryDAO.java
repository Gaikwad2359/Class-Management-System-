package CMS.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import CMS.Master.CountryMaster;


public interface CountryDAO extends JpaRepository<CountryMaster, Integer> {

}
