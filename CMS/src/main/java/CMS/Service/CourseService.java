package CMS.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import CMS.Master.CourseMaster;

public interface CourseService {
  
    List<CourseMaster> getAllCourses();
    
    Page<CourseMaster> findPaginated(int pageNo, int pageSize);
    
    CourseMaster addCourse(CourseMaster courseMaster);
    
    CourseMaster updateCourse(CourseMaster courseMaster, int id);
    
    void deleteCourse(int id);
    
    CourseMaster findCourseById(int id);
    
    List<CourseMaster> findCoursesBetweenDates(LocalDate startDate, LocalDate endDate);
    
   
 }