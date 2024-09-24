package CMS.Master;

import jakarta.persistence.Column;
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
@Table(name = "tblLaptopApply")
public class LaptopApply{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(name = "student_email_id")  // Adjust based on your actual column name
    private String emailId;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phoneNo;

    @NotBlank(message = "Laptop requirement (RAM) is required")
    private String laptopRam;

    @NotBlank(message = "Laptop requirement (ROM) is required")
    private String laptopRom;

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

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getLaptopRam() {
		return laptopRam;
	}

	public void setLaptopRam(String laptopRam) {
		this.laptopRam = laptopRam;
	}

	public String getLaptopRom() {
		return laptopRom;
	}

	public void setLaptopRom(String laptopRom) {
		this.laptopRom = laptopRom;
	}

	public LaptopApply() {
		super();
	}

	public LaptopApply(Long id,
			@NotBlank(message = "Full name is required") @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters") String fullName,
			@Email(message = "Email should be valid") @NotBlank(message = "Email is required") String emailId,
			@NotBlank(message = "Phone number is required") @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits") String phoneNo,
			@NotBlank(message = "Laptop requirement (RAM) is required") String laptopRam,
			@NotBlank(message = "Laptop requirement (ROM) is required") String laptopRom) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.emailId = emailId;
		this.phoneNo = phoneNo;
		this.laptopRam = laptopRam;
		this.laptopRom = laptopRom;
	}

	@Override
	public String toString() {
		return "LaptopApply [id=" + id + ", fullName=" + fullName + ", emailId=" + emailId + ", phoneNo=" + phoneNo
				+ ", laptopRam=" + laptopRam + ", laptopRom=" + laptopRom + "]";
	}
}