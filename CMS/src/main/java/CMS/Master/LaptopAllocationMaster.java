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
@Table(name = "tblLaptopAllocation")

public class LaptopAllocationMaster {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Integer Id;
	private LocalDate allocationDate;
	private double laptopCost;
	private String remarks;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laptopId")
    @JsonIgnore
    private LaptopMaster laptopMaster;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
    @JsonIgnore
    private StudentMaster studentMaster;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public LocalDate getAllocationDate() {
		return allocationDate;
	}

	public void setAllocationDate(LocalDate allocationDate) {
		this.allocationDate = allocationDate;
	}

	public double getLaptopCost() {
		return laptopCost;
	}

	public void setLaptopCost(double laptopCost) {
		this.laptopCost = laptopCost;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LaptopMaster getLaptopMaster() {
		return laptopMaster;
	}

	public void setLaptopMaster(LaptopMaster laptopMaster) {
		this.laptopMaster = laptopMaster;
	}

	public StudentMaster getStudentMaster() {
		return studentMaster;
	}

	public void setStudentMaster(StudentMaster studentMaster) {
		this.studentMaster = studentMaster;
	}

	public LaptopAllocationMaster() {
		super();
	}

	@Override
	public String toString() {
		return "LaptopAllocationMaster [Id=" + Id + ", allocationDate=" + allocationDate + ", laptopCost=" + laptopCost
				+ ", remarks=" + remarks + ", laptopMaster=" + laptopMaster + ", studentMaster=" + studentMaster + "]";
	}

	public LaptopAllocationMaster(Integer id, LocalDate allocationDate, double laptopCost, String remarks,
			LaptopMaster laptopMaster, StudentMaster studentMaster) {
		super();
		Id = id;
		this.allocationDate = allocationDate;
		this.laptopCost = laptopCost;
		this.remarks = remarks;
		this.laptopMaster = laptopMaster;
		this.studentMaster = studentMaster;
	}

	public void addLaptopAllocation(LaptopAllocationMaster laptopAllocationMaster) {
		// TODO Auto-generated method stub
		
	}

}