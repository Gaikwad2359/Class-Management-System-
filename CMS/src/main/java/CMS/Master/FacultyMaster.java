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
@Table(name = "TblFaculty")

public class FacultyMaster {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	
	private Integer facultyId;
	private String facultyFirstName;
	private String facultyMiddleName;
	private String facultyLastName;
	private String facultyMobileNo;
	private String facultyEmailId;
	private String facultyAlternateMobileNumber;
	private String facultyReference;
	private String facultyStatus;
	private Double facultyBalance;
	
	
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cityId")
    @JsonIgnore
    private CityMaster cityMaster;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="stateId")
    @JsonIgnore
    private StateMaster stateMaster;
	
	
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="countryId")
    @JsonIgnore
    private CountryMaster countryMaster;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="qualId")
    @JsonIgnore
    private QualificationMaster qualificationMaster;

	public Integer getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(Integer facultyId) {
		this.facultyId = facultyId;
	}

	public String getFacultyFirstName() {
		return facultyFirstName;
	}

	public void setFacultyFirstName(String facultyFirstName) {
		this.facultyFirstName = facultyFirstName;
	}

	public String getFacultyMiddleName() {
		return facultyMiddleName;
	}

	public void setFacultyMiddleName(String facultyMiddleName) {
		this.facultyMiddleName = facultyMiddleName;
	}

	public String getFacultyLastName() {
		return facultyLastName;
	}

	public void setFacultyLastName(String facultyLastName) {
		this.facultyLastName = facultyLastName;
	}

	public String getFacultyMobileNo() {
		return facultyMobileNo;
	}

	public void setFacultyMobileNo(String facultyMobileNo) {
		this.facultyMobileNo = facultyMobileNo;
	}

	public String getFacultyEmailId() {
		return facultyEmailId;
	}

	public void setFacultyEmailId(String facultyEmailId) {
		this.facultyEmailId = facultyEmailId;
	}

	public String getFacultyAlternateMobileNumber() {
		return facultyAlternateMobileNumber;
	}

	public void setFacultyAlternateMobileNumber(String facultyAlternateMobileNumber) {
		this.facultyAlternateMobileNumber = facultyAlternateMobileNumber;
	}

	public String getFacultyReference() {
		return facultyReference;
	}

	public void setFacultyReference(String facultyReference) {
		this.facultyReference = facultyReference;
	}

	public String getFacultyStatus() {
		return facultyStatus;
	}

	public void setFacultyStatus(String facultyStatus) {
		this.facultyStatus = facultyStatus;
	}

	public Double getFacultyBalance() {
		return facultyBalance;
	}

	public void setFacultyBalance(Double facultyBalance) {
		this.facultyBalance = facultyBalance;
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

	public QualificationMaster getQualificationMaster() {
		return qualificationMaster;
	}

	public void setQualificationMaster(QualificationMaster qualificationMaster) {
		this.qualificationMaster = qualificationMaster;
	}

	public FacultyMaster() {
		super();
	}

	public FacultyMaster(Integer facultyId, String facultyFirstName, String facultyMiddleName, String facultyLastName,
			String facultyMobileNo, String facultyEmailId, String facultyAlternateMobileNumber, String facultyReference,
			String facultyStatus, Double facultyBalance, CityMaster cityMaster, StateMaster stateMaster,
			CountryMaster countryMaster, QualificationMaster qualificationMaster) {
		super();
		this.facultyId = facultyId;
		this.facultyFirstName = facultyFirstName;
		this.facultyMiddleName = facultyMiddleName;
		this.facultyLastName = facultyLastName;
		this.facultyMobileNo = facultyMobileNo;
		this.facultyEmailId = facultyEmailId;
		this.facultyAlternateMobileNumber = facultyAlternateMobileNumber;
		this.facultyReference = facultyReference;
		this.facultyStatus = facultyStatus;
		this.facultyBalance = facultyBalance;
		this.cityMaster = cityMaster;
		this.stateMaster = stateMaster;
		this.countryMaster = countryMaster;
		this.qualificationMaster = qualificationMaster;
	}

	@Override
	public String toString() {
		return "FacultyMaster [facultyId=" + facultyId + ", facultyFirstName=" + facultyFirstName
				+ ", facultyMiddleName=" + facultyMiddleName + ", facultyLastName=" + facultyLastName
				+ ", facultyMobileNo=" + facultyMobileNo + ", facultyEmailId=" + facultyEmailId
				+ ", facultyAlternateMobileNumber=" + facultyAlternateMobileNumber + ", facultyReference="
				+ facultyReference + ", facultyStatus=" + facultyStatus + ", facultyBalance=" + facultyBalance
				+ ", cityMaster=" + cityMaster + ", stateMaster=" + stateMaster + ", countryMaster=" + countryMaster
				+ ", qualificationMaster=" + qualificationMaster + "]";
	}

}