package CMS.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import CMS.Master.QualificationMaster;


public interface QualificationService {
	
	 List<QualificationMaster> getAllQualifications();
	 Page<QualificationMaster> findPaginated(int pageNo, int pageSize);
	 QualificationMaster addQualification(QualificationMaster qualificationMaster); 
		void deleteQualification(int id);
		QualificationMaster findQualificationById(int id);
		QualificationMaster updateQualification(QualificationMaster qualificationMaster);
}