package CMS.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import CMS.Master.LaptopMaster;

public interface LaptopService {

	List<LaptopMaster> getAllLaptops();
	Page<LaptopMaster> findPaginated(int pageNo, int pageSize);
	LaptopMaster addLaptop(LaptopMaster laptopMaster); 
	LaptopMaster updateLaptop(LaptopMaster laptopMaster, int id);
	void deleteLaptop(int id);
	LaptopMaster findLaptopById(int id);
	
	 List<LaptopMaster> searchByLaptopSerialNo(String laptopSerialNo);
}
