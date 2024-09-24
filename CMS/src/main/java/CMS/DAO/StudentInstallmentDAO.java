package CMS.DAO;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import CMS.Master.StudentInstallmentMaster;

@Repository
public interface StudentInstallmentDAO extends JpaRepository<StudentInstallmentMaster, Integer> {
	@Query("SELECT s FROM StudentInstallmentMaster s WHERE s.instDate BETWEEN :startDate AND :endDate")
    List<StudentInstallmentMaster> findAllByInstDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	//Student Installment Bar Diagram 
//	 @Query("SELECT FUNCTION('YEAR', si.instDate) AS year, SUM(si.instAmount) FROM StudentInstallmentMaster si GROUP BY FUNCTION('YEAR', si.instDate)")
//	    List<Object[]> findYearlyInstallments();
//
//	    @Query("SELECT FUNCTION('MONTH', si.instDate) AS month, FUNCTION('YEAR', si.instDate) AS year, SUM(si.instAmount) FROM StudentInstallmentMaster si GROUP BY FUNCTION('YEAR', si.instDate), FUNCTION('MONTH', si.instDate)")
//	    List<Object[]> findMonthlyInstallments();

}