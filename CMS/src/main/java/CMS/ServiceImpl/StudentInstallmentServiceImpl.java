package CMS.ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import CMS.DAO.StudentDAO;
import CMS.DAO.StudentInstallmentDAO;
import CMS.Master.StudentInstallmentMaster;
import CMS.Master.StudentMaster;
import CMS.Service.StudentInstallmentService;

@Service
public class StudentInstallmentServiceImpl implements StudentInstallmentService {

    @Autowired
    private StudentInstallmentDAO installmentDAO;

    @Autowired
    private StudentDAO studentDAO;

    @Override
    public List<StudentInstallmentMaster> getAllInstallments() {
        return installmentDAO.findAll();
    }

    @Override
    public Page<StudentInstallmentMaster> findPaginated(int pageNo, int pageSize) {
        return installmentDAO.findAll(PageRequest.of(pageNo - 1, pageSize));
    }

    @Override
    public StudentInstallmentMaster addInstallment(StudentInstallmentMaster studentInstallmentMaster) {
        if (studentInstallmentMaster != null) {
            // Save the installment
            StudentInstallmentMaster savedInstallment = installmentDAO.save(studentInstallmentMaster);
            
            // Update the student's balance
            Optional<StudentMaster> optionalStudent = studentDAO.findById(studentInstallmentMaster.getStudentMaster().getStudentId());
            if (optionalStudent.isPresent()) {
                StudentMaster student = optionalStudent.get();
                double balance = student.getStudentbalance();
                balance -= studentInstallmentMaster.getInstAmount(); // Deduct the installment amount
                student.setStudentbalance(balance);
                studentDAO.save(student); // Save the updated student balance
            }
            
            return savedInstallment;
        }
        return null; // Or throw an exception as per your error handling strategy
    }

    @Override
    public StudentInstallmentMaster updateInstallment(StudentInstallmentMaster studentInstallmentMaster, int id) {
        if (installmentDAO.existsById(id)) {
            studentInstallmentMaster.setInstId(id);
            return installmentDAO.save(studentInstallmentMaster);
        }
        return null; // or throw an exception
    }

    @Override
    public void deleteInstallment(int id) {
    	installmentDAO.deleteById(id);
    }

    @Override
    public StudentInstallmentMaster findInstallmentById(int id) {
        return installmentDAO.findById(id).orElse(null);
    }

    @Override
    public Object getAllStudents() {
        // Implement based on actual requirements
        return null;
    }

    @Override
    public List<StudentInstallmentMaster> findInstallmentsWithinDates(LocalDate startDate, LocalDate endDate) {
        return installmentDAO.findAllByInstDateBetween(startDate, endDate);
    }

	@Override
	public List<StudentInstallmentMaster> getInstallmentsBetweenDates(LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	//Student Installment Bar Diagram 
//	@Override
//    public List<Object[]> getYearlyInstallments() {
//        return installmentDAO.findYearlyInstallments();
//    }
//
//    @Override
//    public List<Object[]> getMonthlyInstallments() {
//        return installmentDAO.findMonthlyInstallments();
//    }
	
}
