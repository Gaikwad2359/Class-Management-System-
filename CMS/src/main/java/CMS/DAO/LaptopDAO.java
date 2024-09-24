package CMS.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import CMS.Master.LaptopMaster;

@Repository
public interface LaptopDAO extends JpaRepository<LaptopMaster, Integer> {

    List<LaptopMaster> findByLaptopSerialNoContaining(String laptopSerialNo);
}
