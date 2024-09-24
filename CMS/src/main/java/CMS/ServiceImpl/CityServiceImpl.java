package CMS.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import CMS.DAO.CityDAO;
import CMS.Master.CityMaster;
import CMS.Service.CityService;


@Service
public class CityServiceImpl implements CityService{

	@Autowired
	CityDAO dao;

	@Override
	public List<CityMaster> getAllCities() {
		return dao.findAll();
	}

	@Override
	public Page<CityMaster> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return dao.findAll(pageable);
	}

	@Override
	public CityMaster addCity(CityMaster cityMaster) {
		return dao.save(cityMaster);
	}

	@Override
	public CityMaster updateCity(CityMaster cityMaster, int id) {
		return dao.save(cityMaster);
	}

	@Override
	public void deleteCity(int id) {
		dao.deleteById(id);		
	}

	@Override
	public Object getAllStates() {
		return null;
	}

	@Override
	public CityMaster findCityById(int id) {
		Optional<CityMaster> cityMaster = dao.findById(id);
		return cityMaster.orElse(null);
	}

	@Override
	public void updateCity(CityMaster cityMaster) {
	    CityMaster existingCity = dao.findById(cityMaster.getCityId())
	        .orElseThrow(() -> new RuntimeException("City not found with id " + cityMaster.getCityId()));
	    existingCity.setCityName(cityMaster.getCityName());
	    existingCity.setCityDesc(cityMaster.getCityDesc());
	    existingCity.setCityPincode(cityMaster.getCityPincode());
	    existingCity.setStateMaster(cityMaster.getStateMaster());
	    dao.save(existingCity);
	}

}