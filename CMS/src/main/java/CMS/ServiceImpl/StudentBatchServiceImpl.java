package CMS.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import CMS.DAO.StudentBatchDAO;
import CMS.Master.StudentBatch;
import CMS.Master.StudentMaster;
import CMS.Service.StudentBatchService;

@Service
public class StudentBatchServiceImpl implements StudentBatchService  {

	
	@Autowired
	StudentBatchDAO batchDAO;
	
	@Autowired
	StudentBatchDAO dao;

	@Override
	public List<StudentBatch> getAllStudentBatches() {
		return dao.findAll();
	}

	 @Override
	    public Page<StudentBatch> findPaginated(int pageNo, int pageSize) {
	        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
	        return dao.findAll(pageable);
	    }
	

	@Override
	public StudentBatch addStudentBatch(StudentBatch studentBatch) {
		return dao.save(studentBatch);
	}

	@Override
	public StudentBatch updateStudentBatch(StudentBatch studentBatch, int id) {
		return dao.save(studentBatch);
	}

	@Override
	public void deleteStudentBatch(int id) {
		dao.deleteById(id);				
	}

	@Override
	public StudentBatch findStudentBatchById(int id) {
		Optional<StudentBatch> studentMaster = dao.findById(id);
		return studentMaster.orElse(null);
	}

	@Override
	public Object getAllStudents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAllBatches() {
		// TODO Auto-generated method stub
		return null;
	}

	
	//Student
	 @Override
	    public List<StudentBatch> getStudentBatchByStudent(StudentMaster studentMaster) {
	        return batchDAO.findByStudentMaster(studentMaster);
	    }
	
	
}
