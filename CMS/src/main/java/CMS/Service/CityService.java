package CMS.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import CMS.Master.CityMaster;

public interface CityService {
	
	List<CityMaster> getAllCities();
	Page<CityMaster> findPaginated(int pageNo, int pageSize);
	CityMaster addCity(CityMaster cityMaster); 
	CityMaster updateCity(CityMaster cityMaster, int id);
	void deleteCity(int id);
	CityMaster findCityById(int id);
	Object getAllStates();
	void updateCity(CityMaster cityMaster);

}