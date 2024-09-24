package CMS.Master;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DashboardMaster {
	@Id
	Integer did;
	Integer totalCountries;
	Integer totalStates;
	Integer totalCities;
	Integer totalBatches;
	Integer totalCourses;
	Integer totalFaculties;
	Integer totalLaptopAllocations;
	Integer totalLaptops;
	Integer totalQualifications;
	Integer totalStudentBatches;
	Integer totalStudentInstallments;
	Integer totalStudents;
	Integer totalStudentReference;
	Integer totalStudentNotes;
	Integer totalApplyForm;
	
	public Integer getDid() {
		return did;
	}
	public void setDid(Integer did) {
		this.did = did;
	}
	public Integer getTotalCountries() {
		return totalCountries;
	}
	public void setTotalCountries(Integer totalCountries) {
		this.totalCountries = totalCountries;
	}
	public Integer getTotalStates() {
		return totalStates;
	}
	public void setTotalStates(Integer totalStates) {
		this.totalStates = totalStates;
	}
	public Integer getTotalCities() {
		return totalCities;
	}
	public void setTotalCities(Integer totalCities) {
		this.totalCities = totalCities;
	}
	public Integer getTotalBatches() {
		return totalBatches;
	}
	public void setTotalBatches(Integer totalBatches) {
		this.totalBatches = totalBatches;
	}
	public Integer getTotalCourses() {
		return totalCourses;
	}
	public void setTotalCourses(Integer totalCourses) {
		this.totalCourses = totalCourses;
	}
	public Integer getTotalFaculties() {
		return totalFaculties;
	}
	public void setTotalFaculties(Integer totalFaculties) {
		this.totalFaculties = totalFaculties;
	}
	public Integer getTotalLaptopAllocations() {
		return totalLaptopAllocations;
	}
	public void setTotalLaptopAllocations(Integer totalLaptopAllocations) {
		this.totalLaptopAllocations = totalLaptopAllocations;
	}
	public Integer getTotalLaptops() {
		return totalLaptops;
	}
	public void setTotalLaptops(Integer totalLaptops) {
		this.totalLaptops = totalLaptops;
	}
	public Integer getTotalQualifications() {
		return totalQualifications;
	}
	public void setTotalQualifications(Integer totalQualifications) {
		this.totalQualifications = totalQualifications;
	}
	public Integer getTotalStudentBatches() {
		return totalStudentBatches;
	}
	public void setTotalStudentBatches(Integer totalStudentBatches) {
		this.totalStudentBatches = totalStudentBatches;
	}
	public Integer getTotalStudentInstallments() {
		return totalStudentInstallments;
	}
	public void setTotalStudentInstallments(Integer totalStudentInstallments) {
		this.totalStudentInstallments = totalStudentInstallments;
	}
	public Integer getTotalStudents() {
		return totalStudents;
	}
	public void setTotalStudents(Integer totalStudents) {
		this.totalStudents = totalStudents;
	}
	public DashboardMaster() {
		super();
	}
	public DashboardMaster(Integer did, Integer totalCountries, Integer totalStates, Integer totalCities,
			Integer totalBatches, Integer totalCourses, Integer totalFaculties, Integer totalLaptopAllocations,
			Integer totalLaptops, Integer totalQualifications, Integer totalStudentBatches,
			Integer totalStudentInstallments, Integer totalStudents) {
		super();
		this.did = did;
		this.totalCountries = totalCountries;
		this.totalStates = totalStates;
		this.totalCities = totalCities;
		this.totalBatches = totalBatches;
		this.totalCourses = totalCourses;
		this.totalFaculties = totalFaculties;
		this.totalLaptopAllocations = totalLaptopAllocations;
		this.totalLaptops = totalLaptops;
		this.totalQualifications = totalQualifications;
		this.totalStudentBatches = totalStudentBatches;
		this.totalStudentInstallments = totalStudentInstallments;
		this.totalStudents = totalStudents;
	}
	@Override
	public String toString() {
		return "DashboardMaster [did=" + did + ", totalCountries=" + totalCountries + ", totalStates=" + totalStates
				+ ", totalCities=" + totalCities + ", totalBatches=" + totalBatches + ", totalCourses=" + totalCourses
				+ ", totalFaculties=" + totalFaculties + ", totalLaptopAllocations=" + totalLaptopAllocations
				+ ", totalLaptops=" + totalLaptops + ", totalQualifications=" + totalQualifications
				+ ", totalStudentBatches=" + totalStudentBatches + ", totalStudentInstallments="
				+ totalStudentInstallments + ", totalStudents=" + totalStudents + "]";
	}
	public Integer getTotalStudentReference() {
		return totalStudentReference;
	}
	public void setTotalStudentReference(Integer totalStudentReference) {
		this.totalStudentReference = totalStudentReference;
	}
	public Integer getTotalStudentNotes() {
		return totalStudentNotes;
	}
	public void setTotalStudentNotes(Integer totalStudentNotes) {
		this.totalStudentNotes = totalStudentNotes;
	}
	public Integer getTotalApplyForm() {
		return totalApplyForm;
	}
	public void setTotalApplyForm(Integer totalApplyForm) {
		this.totalApplyForm = totalApplyForm;
	}
}