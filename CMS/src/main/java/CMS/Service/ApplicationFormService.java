package CMS.Service;

import java.util.List;

import CMS.Master.ApplyForm;

public interface ApplicationFormService {
	
	// Admin Dashboard Records 
    ApplyForm saveApplyForm(ApplyForm applyForm);
    List<ApplyForm> getApplicationsByStatus(String status);
	ApplyForm getApplyFormById(Long id);
    List<ApplyForm> getAllApplyForms();
    void deleteApplyForm(Long id);
    
}
