package CMS.Master;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tblLaptopMaster")

public class LaptopMaster {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Integer laptopId;
	private LocalDate laptopPurchaseDate;
	private String laptopSerialNo;
	private String laptopPurchasedFrom;
	private String laptopPurchasedContactDetails;
	private double laptopCost;
	private String laptopChargerNo;
	private String laptopRemarks;
	public Integer getLaptopId() {
		return laptopId;
	}
	public void setLaptopId(Integer laptopId) {
		this.laptopId = laptopId;
	}
	public LocalDate getLaptopPurchaseDate() {
		return laptopPurchaseDate;
	}
	public void setLaptopPurchaseDate(LocalDate laptopPurchaseDate) {
		this.laptopPurchaseDate = laptopPurchaseDate;
	}
	public String getLaptopSerialNo() {
		return laptopSerialNo;
	}
	public void setLaptopSerialNo(String laptopSerialNo) {
		this.laptopSerialNo = laptopSerialNo;
	}
	public String getLaptopPurchasedFrom() {
		return laptopPurchasedFrom;
	}
	public void setLaptopPurchasedFrom(String laptopPurchasedFrom) {
		this.laptopPurchasedFrom = laptopPurchasedFrom;
	}
	public String getLaptopPurchasedContactDetails() {
		return laptopPurchasedContactDetails;
	}
	public void setLaptopPurchasedContactDetails(String laptopPurchasedContactDetails) {
		this.laptopPurchasedContactDetails = laptopPurchasedContactDetails;
	}
	public double getLaptopCost() {
		return laptopCost;
	}
	public void setLaptopCost(double laptopCost) {
		this.laptopCost = laptopCost;
	}
	public String getLaptopChargerNo() {
		return laptopChargerNo;
	}
	public void setLaptopChargerNo(String laptopChargerNo) {
		this.laptopChargerNo = laptopChargerNo;
	}
	public String getLaptopRemarks() {
		return laptopRemarks;
	}
	public void setLaptopRemarks(String laptopRemarks) {
		this.laptopRemarks = laptopRemarks;
	}
	public LaptopMaster() {
		super();
	}
	public LaptopMaster(Integer laptopId, LocalDate laptopPurchaseDate, String laptopSerialNo,
			String laptopPurchasedFrom, String laptopPurchasedContactDetails, double laptopCost, String laptopChargerNo,
			String laptopRemarks) {
		super();
		this.laptopId = laptopId;
		this.laptopPurchaseDate = laptopPurchaseDate;
		this.laptopSerialNo = laptopSerialNo;
		this.laptopPurchasedFrom = laptopPurchasedFrom;
		this.laptopPurchasedContactDetails = laptopPurchasedContactDetails;
		this.laptopCost = laptopCost;
		this.laptopChargerNo = laptopChargerNo;
		this.laptopRemarks = laptopRemarks;
	}
	@Override
	public String toString() {
		return "LaptopMaster [laptopId=" + laptopId + ", laptopPurchaseDate=" + laptopPurchaseDate + ", laptopSerialNo="
				+ laptopSerialNo + ", laptopPurchasedFrom=" + laptopPurchasedFrom + ", laptopPurchasedContactDetails="
				+ laptopPurchasedContactDetails + ", laptopCost=" + laptopCost + ", laptopChargerNo=" + laptopChargerNo
				+ ", laptopRemarks=" + laptopRemarks + "]";
	}
}
