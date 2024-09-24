package CMS.Master;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "country")

public class CountryMaster {
	  @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Integer countryId;

	    @NotBlank(message = "Country name is required")
	    @Pattern(regexp = "[a-zA-Z]+", message = "Country name must contain only alphabets")
	    private String countryName;

	    @NotBlank(message = "Country description is required")
	    @Pattern(regexp = "[a-zA-Z]+", message = "Country description must contain only alphabets")
	    private String countryDesc;

	    public Integer getCountryId() {
	        return countryId;
	    }

	    public void setCountryId(Integer countryId) {
	        this.countryId = countryId;
	    }

	    public String getCountryName() {
	        return countryName;
	    }

	    public void setCountryName(String countryName) {
	        this.countryName = countryName;
	    }

	    public String getCountryDesc() {
	        return countryDesc;
	    }

	    public void setCountryDesc(String countryDesc) {
	        this.countryDesc = countryDesc;
	    }

	    public CountryMaster() {
	        super();
	    }

	    public CountryMaster(Integer countryId, String countryName, String countryDesc) {
	        super();
	        this.countryId = countryId;
	        this.countryName = countryName;
	        this.countryDesc = countryDesc;
	    }

	    @Override
	    public String toString() {
	        return "CountryMaster [countryId=" + countryId + ", countryName=" + countryName + ", countryDesc=" + countryDesc + "]";
	    }	
}
