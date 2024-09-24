package CMS.Master;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "apply")
public class ApplyForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String emailId;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String emailPass;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits")
    private String mobileNo;

    @NotBlank(message = "Course is required")
    private String course;
    
    @NotBlank(message = "Status is required")
    private String status; // New field to track status

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getEmailPass() {
		return emailPass;
	}

	public void setEmailPass(String emailPass) {
		this.emailPass = emailPass;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public ApplyForm() {
		super();
	}

	@Override
	public String toString() {
		return "ApplyForm [id=" + id + ", fullName=" + fullName + ", emailId=" + emailId + ", emailPass=" + emailPass
				+ ", mobileNo=" + mobileNo + ", course=" + course + "]";
	}

	public ApplyForm(Long id,
			@NotBlank(message = "Full name is required") @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters") String fullName,
			@Email(message = "Email should be valid") @NotBlank(message = "Email is required") String emailId,
			@NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String emailPass,
			@NotBlank(message = "Mobile number is required") @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits") String mobileNo,
			@NotBlank(message = "Course is required") String course) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.emailId = emailId;
		this.emailPass = emailPass;
		this.mobileNo = mobileNo;
		this.course = course;
	}

    

}