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
@Table(name = "DailyUpdate")
public class DailyUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer updateId;
    
    private String message;
    
    private LocalDate updateDate;
   
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batchId")
    @JsonIgnore
    private BatchMaster batchMaster;

	public Integer getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDate getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDate updateDate) {
		this.updateDate = updateDate;
	}

	public BatchMaster getBatchMaster() {
		return batchMaster;
	}

	public void setBatchMaster(BatchMaster batchMaster) {
		this.batchMaster = batchMaster;
	}

	public DailyUpdate() {
		super();
	}

	public DailyUpdate(Integer updateId, String message, LocalDate updateDate, BatchMaster batchMaster) {
		super();
		this.updateId = updateId;
		this.message = message;
		this.updateDate = updateDate;
		this.batchMaster = batchMaster;
	}

	@Override
	public String toString() {
		return "DailyUpdate [updateId=" + updateId + ", message=" + message + ", updateDate=" + updateDate
				+ ", batchMaster=" + batchMaster + "]";
	}  
    
}
