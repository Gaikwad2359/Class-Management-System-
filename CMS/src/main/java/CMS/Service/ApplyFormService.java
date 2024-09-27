package CMS.Service;

import java.util.List;

import CMS.Master.ApplyForm;

public interface ApplyFormService {

    ApplyForm saveApplyForm(ApplyForm applyForm);
    List<ApplyForm> getAllApplyForms();
    ApplyForm getApplyFormById(Long id);
    void deleteApplyForm(Long id);
}
