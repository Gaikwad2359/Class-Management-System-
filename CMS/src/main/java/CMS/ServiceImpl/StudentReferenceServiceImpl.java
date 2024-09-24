package CMS.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import CMS.DAO.StudentReferenceDAO;
import CMS.Master.StudentReference;
import CMS.Service.StudentReferenceService;

@Service
public class StudentReferenceServiceImpl implements StudentReferenceService {

    @Autowired
    private StudentReferenceDAO dao;

    @Override
    public List<StudentReference> getAllStudentReferences() {
        return dao.findAll();
    }

    @Override
    public Page<StudentReference> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return dao.findAll(pageable);
    }

    @Override
    public StudentReference addStudentReference(StudentReference studentReference) {
        return dao.save(studentReference);
    }

    @Override
    public StudentReference updateStudentReference(StudentReference studentReference, int id) {
        Optional<StudentReference> existingReference = dao.findById(id);
        if (existingReference.isPresent()) {
            StudentReference updatedReference = existingReference.get();
            updatedReference.setReferenceStudentName(studentReference.getReferenceStudentName());
            return dao.save(updatedReference);
        }
        return null;
    }

    @Override
    public void deleteStudentReference(int id) {
        dao.deleteById(id);
    }

    @Override
    public StudentReference findStudentReferenceById(int id) {
        Optional<StudentReference> studentReference = dao.findById(id);
        return studentReference.orElse(null);
    }

    @Override
    public Object getAllStudents() {
        return null; 
    }

    @Override
    public void updateStudentReference(StudentReference studentReference) {
        dao.save(studentReference); 
    }

    //Student Reference
    @Override
    public List<StudentReference> getStudentReferencesByStudentId(Integer studentId) {
        // Fetch references for the student by their ID
        return dao.findByStudentMaster_StudentId(studentId);
    }
}
