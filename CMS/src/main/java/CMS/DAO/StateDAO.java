package CMS.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import CMS.Master.StateMaster;


@Repository
public interface StateDAO extends JpaRepository<StateMaster, Integer>{

}
