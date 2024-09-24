package CMS.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import CMS.Master.StateMaster;


public interface StateService {
	
	List<StateMaster> getAllStates();
	Page<StateMaster> findPaginated(int pageNo, int pageSize);
	StateMaster addState(StateMaster stateMaster); 
	StateMaster updateState(StateMaster stateMaster, int id);
	void deleteState(int id);
	StateMaster findStateById(int id);
	Object getAllCountries();
}