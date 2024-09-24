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
@Table(name ="tbl_city")
public class CityMaster {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer cityId;
	private String cityName;
	private String cityDesc;
	private String cityPincode;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stateId")
    @JsonIgnore
    private StateMaster stateMaster;

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityDesc() {
		return cityDesc;
	}

	public void setCityDesc(String cityDesc) {
		this.cityDesc = cityDesc;
	}

	public String getCityPincode() {
		return cityPincode;
	}

	public void setCityPincode(String cityPincode) {
		this.cityPincode = cityPincode;
	}

	public StateMaster getStateMaster() {
		return stateMaster;
	}

	public void setStateMaster(StateMaster stateMaster) {
		this.stateMaster = stateMaster;
	}

	public CityMaster() {
		super();
	}

	public CityMaster(Integer cityId, String cityName, String cityDesc, String cityPincode, StateMaster stateMaster) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
		this.cityDesc = cityDesc;
		this.cityPincode = cityPincode;
		this.stateMaster = stateMaster;
	}

	@Override
	public String toString() {
		return "CityMaster [cityId=" + cityId + ", cityName=" + cityName + ", cityDesc=" + cityDesc + ", cityPincode="
				+ cityPincode + ", stateMaster=" + stateMaster + "]";
	}
	
}