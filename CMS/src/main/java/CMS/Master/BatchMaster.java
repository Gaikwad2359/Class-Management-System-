package CMS.Master;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Batch")
public class BatchMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer batchId;
    private String batchName;
    private LocalDate batchStartDate;
    private LocalDate batchEndDate;
    private String batchDetails;
    private String batchRemarks;
    private String batchStatus;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId")
    @JsonIgnore
    private CourseMaster courseMaster;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facultyId")
    @JsonIgnore
    private FacultyMaster facultyMaster;

    public BatchMaster() {
    }

    public BatchMaster(Integer batchId, String batchName, LocalDate batchStartDate, LocalDate batchEndDate,
                       String batchDetails, String batchRemarks, String batchStatus,
                       CourseMaster courseMaster, FacultyMaster facultyMaster) {
        this.batchId = batchId;
        this.batchName = batchName;
        this.batchStartDate = batchStartDate;
        this.batchEndDate = batchEndDate;
        this.batchDetails = batchDetails;
        this.batchRemarks = batchRemarks;
        this.batchStatus = batchStatus;
        this.courseMaster = courseMaster;
        this.facultyMaster = facultyMaster;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer id) {
        this.batchId = id;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public LocalDate getBatchStartDate() {
        return batchStartDate;
    }

    public void setBatchStartDate(LocalDate batchStartDate) {
        this.batchStartDate = batchStartDate;
    }

    public LocalDate getBatchEndDate() {
        return batchEndDate;
    }

    public void setBatchEndDate(LocalDate batchEndDate) {
        this.batchEndDate = batchEndDate;
    }

    public String getBatchDetails() {
        return batchDetails;
    }

    public void setBatchDetails(String batchDetails) {
        this.batchDetails = batchDetails;
    }

    public String getBatchRemarks() {
        return batchRemarks;
    }

    public void setBatchRemarks(String batchRemarks) {
        this.batchRemarks = batchRemarks;
    }

    public String getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
    }

    public CourseMaster getCourseMaster() {
        return courseMaster;
    }

    public void setCourseMaster(CourseMaster courseMaster) {
        this.courseMaster = courseMaster;
    }

    public FacultyMaster getFacultyMaster() {
        return facultyMaster;
    }

    public void setFacultyMaster(FacultyMaster facultyMaster) {
        this.facultyMaster = facultyMaster;
    }

    @Override
    public String toString() {
        return "BatchMaster{" +
                "batchId=" + batchId +
                ", batchName='" + batchName + '\'' +
                ", batchStartDate=" + batchStartDate +
                ", batchEndDate=" + batchEndDate +
                ", batchDetails='" + batchDetails + '\'' +
                ", batchRemarks='" + batchRemarks + '\'' +
                ", batchStatus='" + batchStatus + '\'' +
                ", courseMaster=" + courseMaster +
                ", facultyMaster=" + facultyMaster +
                '}';
    }
}
