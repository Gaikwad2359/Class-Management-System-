package CMS.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import CMS.Master.CountryMaster;

public interface CountryService {
    List<CountryMaster> getAllCountries();
    Page<CountryMaster> findPaginated(int pageNo, int pageSize);
    CountryMaster addCountry(CountryMaster countryMaster);
    CountryMaster updateCountry(CountryMaster countryMaster, int id);
    void deleteCountry(int id);
    CountryMaster findCountryById(int id);
    List<CountryMaster> findAllCountries();
}
