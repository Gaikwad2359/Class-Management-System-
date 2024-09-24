package CMS.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import CMS.DAO.QualificationDAO;
import CMS.Master.QualificationMaster;
import CMS.Service.QualificationService;

@Service
public class QualificationServiceImpl  implements QualificationService{
	
	 @Autowired
	  QualificationDAO dao;

	@Override
	public List<QualificationMaster> getAllQualifications() {
		return dao.findAll();
	}

	@Override
	public Page<QualificationMaster> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return dao.findAll(pageable);
	}

	@Override
	public QualificationMaster addQualification(QualificationMaster qualificationMaster) {
		return dao.save(qualificationMaster);
	}

	@Override
	public QualificationMaster updateQualification(QualificationMaster qualificationMaster) {
	    return dao.save(qualificationMaster);
	}


	@Override
	public void deleteQualification(int id) {
		dao.deleteById(id);			
	}

	@Override
	public QualificationMaster findQualificationById(int id) {
		Optional<QualificationMaster> qualificationMaster = dao.findById(id);
		return qualificationMaster.orElse(null);
	}

		
}