package CMS.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import CMS.Master.StudentInstallmentMaster;
import CMS.Master.StudentMaster;

@Repository
public interface StudentLoginInstallmentRepository extends JpaRepository<StudentInstallmentMaster, Integer> {
    
    // Custom query method to find installments by studentMaster
    List<StudentInstallmentMaster> findByStudentMaster(StudentMaster studentMaster);
}
