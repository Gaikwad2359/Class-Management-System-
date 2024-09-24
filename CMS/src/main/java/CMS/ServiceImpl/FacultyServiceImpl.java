package CMS.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import CMS.DAO.FacultyDAO;
import CMS.Master.FacultyMaster;
import CMS.Service.FacultyService;

@Service
public class FacultyServiceImpl implements FacultyService {

	@Autowired
	FacultyDAO dao;

	@Override
	public List<FacultyMaster> getAllFaculties() {
		return dao.findAll();
	}

	@Override
	public Page<FacultyMaster> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return dao.findAll(pageable);
	}

	@Override
	public FacultyMaster addFaculty(FacultyMaster facultyMaster) {
		return dao.save(facultyMaster);
	}

	@Override
	public FacultyMaster updateFaculty(FacultyMaster facultyMaster, int id) {
		return dao.save(facultyMaster);
	}

	@Override
	public void deleteFaculty(int id) {
		dao.deleteById(id);				
	}

	@Override
	public FacultyMaster findFacultyById(int id) {
		Optional<FacultyMaster> facultyMaster = dao.findById(id);
		return facultyMaster.orElse(null);
	}

	@Override
	public Object getAllCities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAllStates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAllCountries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAllQualifications() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FacultyMaster> findFacultiesWithBalanceGreaterThanZero() {
		return dao.findByFacultyBalanceGreaterThan(0);
	}

	
	
}
