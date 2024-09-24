package CMS.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import CMS.Master.StudentInstallmentMaster;

public interface StudentInstallmentService {

    // Retrieve all student installments
    List<StudentInstallmentMaster> getAllInstallments();

    // Retrieve a paginated list of student installments
    Page<StudentInstallmentMaster> findPaginated(int pageNo, int pageSize);

    // Add a new student installment
    StudentInstallmentMaster addInstallment(StudentInstallmentMaster studentInstallmentMaster);

    // Update an existing student installment
    StudentInstallmentMaster updateInstallment(StudentInstallmentMaster studentInstallmentMaster, int id);

    // Delete a student installment by its ID
    void deleteInstallment(int id);

    // Find a student installment by its ID
    StudentInstallmentMaster findInstallmentById(int id);

    // Retrieve all installments for students (You might need to adjust this based on actual requirements)
    Object getAllStudents();

    // Retrieve installments within a date range
    List<StudentInstallmentMaster> findInstallmentsWithinDates(LocalDate startDate, LocalDate endDate);

	List<StudentInstallmentMaster> getInstallmentsBetweenDates(LocalDate startDate, LocalDate endDate);
	
	
	//Student Installment Bar Diagram 
//	 List<Object[]> getYearlyInstallments();
//	    List<Object[]> getMonthlyInstallments();
}
