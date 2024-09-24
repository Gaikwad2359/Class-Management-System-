package CMS.DAO;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import CMS.Master.CourseMaster;

public interface CourseDAO extends JpaRepository<CourseMaster, Integer> {
	List<CourseMaster> findByCourseStartDateBetween(LocalDate startDate, LocalDate endDate);
	 List<CourseMaster> findByCourseStartDateAfter(LocalDate date);
	}	