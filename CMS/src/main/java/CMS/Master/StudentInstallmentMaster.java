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
@Table(name = "tblStudentInstallments")
public class StudentInstallmentMaster  implements Master {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer instId;
    private LocalDate instDate; 
    private double instAmount;
    private String InstPaymentType;
    private String InstPayChequeNo;
    private LocalDate InstPayChequeDate;
    private String InstPayChequeBank;
	
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="studentId")
    @JsonIgnore
    private StudentMaster studentMaster;

	public Integer getInstId() {
		return instId;
	}

	public void setInstId(Integer instId) {
		this.instId = instId;
	}

	public LocalDate getInstDate() {
		return instDate;
	}

	public void setInstDate(LocalDate instDate) {
		this.instDate = instDate;
	}

	public double getInstAmount() {
		return instAmount;
	}

	public void setInstAmount(double instAmount) {
		this.instAmount = instAmount;
	}

	public String getInstPaymentType() {
		return InstPaymentType;
	}

	public void setInstPaymentType(String instPaymentType) {
		InstPaymentType = instPaymentType;
	}

	public String getInstPayChequeNo() {
		return InstPayChequeNo;
	}

	public void setInstPayChequeNo(String instPayChequeNo) {
		InstPayChequeNo = instPayChequeNo;
	}

	public LocalDate getInstPayChequeDate() {
		return InstPayChequeDate;
	}

	public void setInstPayChequeDate(LocalDate instPayChequeDate) {
		InstPayChequeDate = instPayChequeDate;
	}

	public String getInstPayChequeBank() {
		return InstPayChequeBank;
	}

	public void setInstPayChequeBank(String instPayChequeBank) {
		InstPayChequeBank = instPayChequeBank;
	}

	public StudentMaster getStudentMaster() {
		return studentMaster;
	}

	public void setStudentMaster(StudentMaster studentMaster) {
		this.studentMaster = studentMaster;
	}

	public StudentInstallmentMaster() {
		super();
	}

	public StudentInstallmentMaster(Integer instId, LocalDate instDate, double instAmount, String instPaymentType,
			String instPayChequeNo, LocalDate instPayChequeDate, String instPayChequeBank,
			StudentMaster studentMaster) {
		super();
		this.instId = instId;
		this.instDate = instDate;
		this.instAmount = instAmount;
		InstPaymentType = instPaymentType;
		InstPayChequeNo = instPayChequeNo;
		InstPayChequeDate = instPayChequeDate;
		InstPayChequeBank = instPayChequeBank;
		this.studentMaster = studentMaster;
	}

	@Override
	public String toString() {
		return "StudentInstallmentMaster [instId=" + instId + ", instDate=" + instDate + ", instAmount=" + instAmount
				+ ", InstPaymentType=" + InstPaymentType + ", InstPayChequeNo=" + InstPayChequeNo
				+ ", InstPayChequeDate=" + InstPayChequeDate + ", InstPayChequeBank=" + InstPayChequeBank
				+ ", studentMaster=" + studentMaster + "]";
	}
}