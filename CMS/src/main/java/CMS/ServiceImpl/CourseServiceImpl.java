package CMS.ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import CMS.DAO.CourseDAO;
import CMS.Master.CourseMaster;
import CMS.Service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDAO dao;

    @Override
    public List<CourseMaster> getAllCourses() {
        return dao.findAll();
    }

    @Override
    public Page<CourseMaster> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return dao.findAll(pageable);
    }

    @Override
    public CourseMaster addCourse(CourseMaster courseMaster) {
        return dao.save(courseMaster);
    }

    @Override
    public CourseMaster updateCourse(CourseMaster courseMaster, int id) {
        // Check if the course exists
        Optional<CourseMaster> existingCourse = dao.findById(id);
        if (existingCourse.isPresent()) {
            CourseMaster updatedCourse = existingCourse.get();
            // Update fields
            updatedCourse.setCourseName(courseMaster.getCourseName());
            updatedCourse.setCourseDesc(courseMaster.getCourseDesc());
            updatedCourse.setCourseFees(courseMaster.getCourseFees());
            updatedCourse.setCourseStartDate(courseMaster.getCourseStartDate());
            updatedCourse.setCourseEndDate(courseMaster.getCourseEndDate());
            updatedCourse.setStatus(courseMaster.getStatus());
            return dao.save(updatedCourse);
        } else {
            // Optionally throw an exception or handle the case where the course is not found
            return null;
        }
    }

    @Override
    public void deleteCourse(int id) {
        dao.deleteById(id);
    }

    @Override
    public CourseMaster findCourseById(int id) {
        Optional<CourseMaster> courseMaster = dao.findById(id);
        return courseMaster.orElse(null);
    }
    
    @Override
    public List<CourseMaster> findCoursesBetweenDates(LocalDate startDate, LocalDate endDate) {
    	return dao.findByCourseStartDateBetween(startDate, endDate);
    }
    

}
