package CMS.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import CMS.DAO.BatchDAO;
import CMS.DAO.CourseDAO;
import CMS.Master.BatchMaster;
import CMS.Master.CourseMaster;

@Service
public class NotificationService {

    @Autowired
    private BatchDAO batchRepository;

    @Autowired
    private CourseDAO courseRepository;

    public List<BatchMaster> getUpcomingBatches() {
        return batchRepository.findByBatchStartDateAfter(LocalDate.now());
    }

    public List<CourseMaster> getUpcomingCourses() {
        return courseRepository.findByCourseStartDateAfter(LocalDate.now());
    }
}
