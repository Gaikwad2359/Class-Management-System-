package CMS.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import CMS.DAO.StateDAO;
import CMS.Master.StateMaster;
import CMS.Service.StateService;

@Service
public class StateServiceImpl  implements StateService{
	
	 @Autowired
	 StateDAO stateDAO;
	 @Override
		public List<StateMaster> getAllStates() {
			return stateDAO.findAll();
		}

		@Override
		public Page<StateMaster> findPaginated(int pageNo, int pageSize) {
			Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
			return stateDAO.findAll(pageable);
		}

		@Override
		public StateMaster addState(StateMaster stateMaster) {
			return stateDAO.save(stateMaster);
		}

		@Override
		public StateMaster updateState(StateMaster stateMaster, int id) {
			stateMaster.setStateId(id);
			return stateDAO.save(stateMaster);
		}

		@Override
		public void deleteState(int id) {
			stateDAO.deleteById(id);
		}

		@Override
		public StateMaster findStateById(int id) {
			Optional<StateMaster> stateMaster = stateDAO.findById(id);
			return stateMaster.orElse(null);
		}

		@Override
		public Object getAllCountries() {
			// TODO: Implement logic to retrieve all countries
			return null;
		}	
}