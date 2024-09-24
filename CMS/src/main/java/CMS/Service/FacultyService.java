package CMS.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import CMS.Master.FacultyMaster;

public interface FacultyService {

	List<FacultyMaster> getAllFaculties();
	Page<FacultyMaster> findPaginated(int pageNo, int pageSize);
	FacultyMaster addFaculty(FacultyMaster facultyMaster); 
	FacultyMaster updateFaculty(FacultyMaster facultyMaster, int id);
	void deleteFaculty(int id);
	FacultyMaster findFacultyById(int id);
	Object getAllCities();
	Object getAllStates();
	Object getAllCountries();
	Object getAllQualifications();
	
	  // New method for finding faculties with a balance greater than 0
    List<FacultyMaster> findFacultiesWithBalanceGreaterThanZero();

}
