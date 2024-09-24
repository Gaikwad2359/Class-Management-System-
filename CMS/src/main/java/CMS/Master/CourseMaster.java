package CMS.Master;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tblCourse")
public class CourseMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer courseId;
    private String courseName;
    private String courseDesc;
    private String courseFees;
    private LocalDate courseStartDate;
    private LocalDate courseEndDate;
    private String status;

    // Getters and Setters
    public Integer getCourseId() {
        return courseId;
    }
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getCourseDesc() {
        return courseDesc;
    }
    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }
    public String getCourseFees() {
        return courseFees;
    }
    public void setCourseFees(String courseFees) {
        this.courseFees = courseFees;
    }
    public LocalDate getCourseStartDate() {
        return courseStartDate;
    }
    public void setCourseStartDate(LocalDate courseStartDate) {
        this.courseStartDate = courseStartDate;
    }
    public LocalDate getCourseEndDate() {
        return courseEndDate;
    }
    public void setCourseEndDate(LocalDate courseEndDate) {
        this.courseEndDate = courseEndDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    // Constructors
    public CourseMaster() {
        super();
    }
    public CourseMaster(Integer courseId, String courseName, String courseDesc, String courseFees,
                        LocalDate courseStartDate, LocalDate courseEndDate, String status) {
        super();
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDesc = courseDesc;
        this.courseFees = courseFees;
        this.courseStartDate = courseStartDate;
        this.courseEndDate = courseEndDate;
        this.status = status;
    }
    @Override
    public String toString() {
        return "CourseMaster [courseId=" + courseId + ", courseName=" + courseName + ", courseDesc=" + courseDesc
                + ", courseFees=" + courseFees + ", courseStartDate=" + courseStartDate + ", courseEndDate="
                + courseEndDate + ", status=" + status + "]";
    }    
}