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
@Table(name="tblstate")

public class StateMaster {
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Integer stateId;
	private String stateName;
	private String stateDescription;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    @JsonIgnore
    private CountryMaster countryMaster;

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getStateDescription() {
		return stateDescription;
	}

	public void setStateDescription(String stateDescription) {
		this.stateDescription = stateDescription;
	}

	public CountryMaster getCountryMaster() {
		return countryMaster;
	}

	public void setCountryMaster(CountryMaster countryMaster) {
		this.countryMaster = countryMaster;
	}

	public StateMaster() {
		super();
	}

	public StateMaster(Integer stateId, String stateName, String stateDescription, CountryMaster countryMaster) {
		super();
		this.stateId = stateId;
		this.stateName = stateName;
		this.stateDescription = stateDescription;
		this.countryMaster = countryMaster;
	}

	@Override
	public String toString() {
		return "StateMaster [stateId=" + stateId + ", stateName=" + stateName + ", stateDescription=" + stateDescription
				+ ", countryMaster=" + countryMaster + "]";
	}
}