package CMS.ServiceImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import CMS.DAO.CountryDAO;
import CMS.Master.CountryMaster;
import CMS.Service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    CountryDAO countryDAO;

    @Override
    public List<CountryMaster> getAllCountries() {
        return countryDAO.findAll();
    }

    @Override
    public Page<CountryMaster> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.countryDAO.findAll(pageable);
    }

    @Override
    public CountryMaster addCountry(CountryMaster countryMaster) {
        return countryDAO.save(countryMaster);
    }

    @Override
    public CountryMaster updateCountry(CountryMaster countryMaster, int id) {
        try {
            countryMaster.setCountryId(id);
            return countryDAO.save(countryMaster);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteCountry(int id) {
        try {
            countryDAO.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Country. Please try again later.");
        }
    }

    @Override
    public CountryMaster findCountryById(int id) {
        Optional<CountryMaster> countryMaster = countryDAO.findById(id);
        return countryMaster.orElse(null);
    }

    @Override
    public List<CountryMaster> findAllCountries() {
        return countryDAO.findAll();
    }
}
