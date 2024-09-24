package CMS.Master;

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
@Table(name="Student1")
public class StudentMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;
    
    private String studentFirstName;
    private String studentMiddleName;
    private String studentLastName;
    private String studentMobileNo;
    private String studentEmailId;
    private String studentReference;
    private double studentbalance;
    private String status;
    private String email;
	private String password;
	private String role;
	private boolean enable;
	private String verificationCode;
	private String name;
	
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="qualId")
    private QualificationMaster qualificationMaster;
    
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cityId")
    private CityMaster cityMaster;
    
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="stateId")
    private StateMaster stateMaster;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="countryId")
    private CountryMaster countryMaster;

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getStudentFirstName() {
		return studentFirstName;
	}

	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}

	public String getStudentMiddleName() {
		return studentMiddleName;
	}

	public void setStudentMiddleName(String studentMiddleName) {
		this.studentMiddleName = studentMiddleName;
	}

	public String getStudentLastName() {
		return studentLastName;
	}

	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	public String getStudentMobileNo() {
		return studentMobileNo;
	}

	public void setStudentMobileNo(String studentMobileNo) {
		this.studentMobileNo = studentMobileNo;
	}

	public String getStudentEmailId() {
		return studentEmailId;
	}

	public void setStudentEmailId(String studentEmailId) {
		this.studentEmailId = studentEmailId;
	}

	public String getStudentReference() {
		return studentReference;
	}

	public void setStudentReference(String studentReference) {
		this.studentReference = studentReference;
	}

	public double getStudentbalance() {
		return studentbalance;
	}

	public void setStudentbalance(double studentbalance) {
		this.studentbalance = studentbalance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public QualificationMaster getQualificationMaster() {
		return qualificationMaster;
	}

	public void setQualificationMaster(QualificationMaster qualificationMaster) {
		this.qualificationMaster = qualificationMaster;
	}

	public CityMaster getCityMaster() {
		return cityMaster;
	}

	public void setCityMaster(CityMaster cityMaster) {
		this.cityMaster = cityMaster;
	}

	public StateMaster getStateMaster() {
		return stateMaster;
	}

	public void setStateMaster(StateMaster stateMaster) {
		this.stateMaster = stateMaster;
	}

	public CountryMaster getCountryMaster() {
		return countryMaster;
	}

	public void setCountryMaster(CountryMaster countryMaster) {
		this.countryMaster = countryMaster;
	}

	public StudentMaster() {
		super();
	}

	public StudentMaster(int studentId, String studentFirstName, String studentMiddleName, String studentLastName,
			String studentMobileNo, String studentEmailId, String studentReference, double studentbalance,
			String status, QualificationMaster qualificationMaster, CityMaster cityMaster, StateMaster stateMaster,
			CountryMaster countryMaster) {
		super();
		this.studentId = studentId;
		this.studentFirstName = studentFirstName;
		this.studentMiddleName = studentMiddleName;
		this.studentLastName = studentLastName;
		this.studentMobileNo = studentMobileNo;
		this.studentEmailId = studentEmailId;
		this.studentReference = studentReference;
		this.studentbalance = studentbalance;
		this.status = status;
		this.qualificationMaster = qualificationMaster;
		this.cityMaster = cityMaster;
		this.stateMaster = stateMaster;
		this.countryMaster = countryMaster;
	}

	@Override
	public String toString() {
		return "StudentMaster [studentId=" + studentId + ", studentFirstName=" + studentFirstName
				+ ", studentMiddleName=" + studentMiddleName + ", studentLastName=" + studentLastName
				+ ", studentMobileNo=" + studentMobileNo + ", studentEmailId=" + studentEmailId + ", studentReference="
				+ studentReference + ", studentbalance=" + studentbalance + ", status=" + status
				+ ", qualificationMaster=" + qualificationMaster + ", cityMaster=" + cityMaster + ", stateMaster="
				+ stateMaster + ", countryMaster=" + countryMaster + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
