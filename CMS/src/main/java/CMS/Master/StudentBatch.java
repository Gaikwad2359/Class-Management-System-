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
@Table(name = "tblStudentBatch")

public class StudentBatch {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String remarks;
	private LocalDate joinDate;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
    @JsonIgnore
    private StudentMaster studentMaster;
	
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batchId")
    @JsonIgnore
    private BatchMaster batchMaster;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public LocalDate getJoinDate() {
		return joinDate;
	}


	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public StudentMaster getStudentMaster() {
		return studentMaster;
	}


	public void setStudentMaster(StudentMaster studentMaster) {
		this.studentMaster = studentMaster;
	}


	public BatchMaster getBatchMaster() {
		return batchMaster;
	}


	public void setBatchMaster(BatchMaster batchMaster) {
		this.batchMaster = batchMaster;
	}


	public StudentBatch() {
		super();
	}


	public StudentBatch(Integer id, LocalDate joinDate, String remarks, StudentMaster studentMaster,
			BatchMaster batchMaster) {
		super();
		this.id = id;
		this.joinDate = joinDate;
		this.remarks = remarks;
		this.studentMaster = studentMaster;
		this.batchMaster = batchMaster;
	}


	@Override
	public String toString() {
		return "StudentBatch [id=" + id + ", joinDate=" + joinDate + ", remarks=" + remarks + ", studentMaster="
				+ studentMaster + ", batchMaster=" + batchMaster + "]";
	}
}