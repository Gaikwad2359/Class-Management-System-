package CMS.Service;

import java.util.List;

import CMS.Master.LaptopApply;

public interface LaptopApplyService {

	 LaptopApply saveLaptopApplyForm(LaptopApply laptopApplyForm);
	    List<LaptopApply> getAllLaptopApplyForms();
	    LaptopApply getLaptopApplyFormById(Long id);
	    void deleteLaptopApplyForm(Long id);
}
