package CMS.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import CMS.DAO.LaptopDAO;
import CMS.Master.LaptopMaster;
import CMS.Service.LaptopService;

@Service
public class LaptopServiceImpl implements LaptopService {

    @Autowired
    LaptopDAO dao;

	@Override
	public List<LaptopMaster> getAllLaptops() {
		return dao.findAll();
	}

	@Override
	public Page<LaptopMaster> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return dao.findAll(pageable);
	}

	@Override
	public LaptopMaster addLaptop(LaptopMaster laptopMaster) {
		return dao.save(laptopMaster);
	}

	@Override
	public LaptopMaster updateLaptop(LaptopMaster laptopMaster, int id) {
		return dao.save(laptopMaster);
	}

	@Override
	public void deleteLaptop(int id) {
		dao.deleteById(id);		
		
	}

	@Override
	public LaptopMaster findLaptopById(int id) {
		Optional<LaptopMaster> laptopMaster = dao.findById(id);
		return laptopMaster.orElse(null);
	}

	@Override
	public List<LaptopMaster> searchByLaptopSerialNo(String laptopSerialNo) {
		 return dao.findByLaptopSerialNoContaining(laptopSerialNo);
	}
	
}