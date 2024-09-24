package CMS.Master;

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
@Table(name = "tblStudentReference")
public class StudentReference {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer referenceId;

	private String referenceStudentName;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
    @JsonIgnore
    private StudentMaster studentMaster;

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceStudentName() {
		return referenceStudentName;
	}

	public void setReferenceStudentName(String referenceStudentName) {
		this.referenceStudentName = referenceStudentName;
	}

	public StudentMaster getStudentMaster() {
		return studentMaster;
	}

	public void setStudentMaster(StudentMaster studentMaster) {
		this.studentMaster = studentMaster;
	}

	public StudentReference() {
		super();
	}

	public StudentReference(Integer referenceId, String referenceStudentName, StudentMaster studentMaster) {
		super();
		this.referenceId = referenceId;
		this.referenceStudentName = referenceStudentName;
		this.studentMaster = studentMaster;
	}

	@Override
	public String toString() {
		return "StudentReference [referenceId=" + referenceId + ", referenceStudentName=" + referenceStudentName
				+ ", studentMaster=" + studentMaster + "]";
	}	
}