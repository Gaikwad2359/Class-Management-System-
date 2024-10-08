//package CMS.Controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import CMS.Master.ApplyForm;
//import CMS.Master.BatchMaster;
//import CMS.Master.CityMaster;
//import CMS.Master.CountryMaster;
//import CMS.Master.CourseMaster;
//import CMS.Master.DashboardMaster;
//import CMS.Master.FacultyMaster;
//import CMS.Master.LaptopAllocationMaster;
//import CMS.Master.LaptopMaster;
//import CMS.Master.Notes;
//import CMS.Master.QualificationMaster;
//import CMS.Master.StateMaster;
//import CMS.Master.StudentBatch;
//import CMS.Master.StudentInstallmentMaster;
//import CMS.Master.StudentMaster;
//import CMS.Master.StudentReference;
//import CMS.Service.ApplicationFormService;
//import CMS.Service.BatchService;
//import CMS.Service.CityService;
//import CMS.Service.CountryService;
//import CMS.Service.CourseService;
//import CMS.Service.FacultyService;
//import CMS.Service.LaptopAllocationService;
//import CMS.Service.LaptopService;
//import CMS.Service.NotesService;
//import CMS.Service.QualificationService;
//import CMS.Service.StateService;
//import CMS.Service.StudentBatchService;
//import CMS.Service.StudentInstallmentService;
//import CMS.Service.StudentReferenceService;
//import CMS.Service.StudentService;
//
//@Controller
//public class DashboardController {
//
//	@Autowired
//	CountryService countryService;
//	@Autowired
//	StateService stateService;
//	@Autowired
//	CityService cityService;
//	@Autowired
//	BatchService batchService;
//	@Autowired
//	CourseService courseService;
//	@Autowired
//	FacultyService facultyService;
//	@Autowired
//	LaptopAllocationService laptopAllocationService;
//	@Autowired
//	LaptopService laptopService;
//	@Autowired
//	QualificationService qualificationService;
//	@Autowired
//	StudentBatchService studentBatchService;
//	@Autowired
//	StudentService studentService;
//	@Autowired
//	StudentInstallmentService studentInstallmentService;
//	@Autowired
//	StudentReferenceService studentReferenceService;
//	@Autowired
//	NotesService notesService;
//	@Autowired
//	ApplicationFormService applicationFormService;
//	 
//
//@GetMapping("/dashboard")
//	public String showDashboard(Model model)
//	{
//		DashboardMaster dmaster= new DashboardMaster();
//		
//		List<CountryMaster> lstCountries = countryService.getAllCountries();
//		
//		List<StateMaster> lstStates = stateService.getAllStates();
//		
//		List<CityMaster> lstCities = cityService.getAllCities();
//		
//		List<BatchMaster> lstBatches = batchService.getAllBatches();
//		
//		List<CourseMaster> lstCourse = courseService.getAllCourses();
//		
//		List<FacultyMaster> lstFaculties = facultyService.getAllFaculties();
//		
//		List<LaptopAllocationMaster> lstLapotpAllocations = laptopAllocationService.getAllLaptopAllocations();
//		
//		List<LaptopMaster> lstLaptops = laptopService.getAllLaptops();
//		
//		List<QualificationMaster> lstQualifications = qualificationService.getAllQualifications();
//		
//		List<StudentBatch> lstStudentBatchs = studentBatchService.getAllStudentBatches();
//		
//		List<StudentMaster> lstStudents = studentService.getAllStudents();
//		
//		List<StudentInstallmentMaster> lstStudentInstallments = studentInstallmentService.getAllInstallments();
//		
//		List<StudentReference> lstStudentReferences = studentReferenceService.getAllStudentReferences();
//		
//		List<Notes> lstStudentNotes = notesService.getAllNotes();
//		
//		List<ApplyForm> lstApplyForms = applicationFormService.getAllApplyForms();
//		
//				
//		
//		 
//		 dmaster.setTotalCountries(lstCountries.size());
//		 dmaster.setTotalStates(lstStates.size());
//		 dmaster.setTotalCities(lstCities.size());
//		 dmaster.setTotalBatches(lstBatches.size());
//		 dmaster.setTotalCourses(lstCourse.size());
//		 dmaster.setTotalFaculties(lstFaculties.size());
//		 dmaster.setTotalLaptopAllocations(lstLapotpAllocations.size());
//		 dmaster.setTotalLaptops(lstLaptops.size()); 
//		 dmaster.setTotalQualifications(lstQualifications.size());
//		 dmaster.setTotalStudentBatches(lstStudentBatchs.size());
//		 dmaster.setTotalStudents(lstStudents.size());
//		 dmaster.setTotalStudentInstallments(lstStudentInstallments.size());
//		 dmaster.setTotalStudentReference(lstStudentReferences.size());
//		 dmaster.setTotalStudentNotes(lstStudentNotes.size());
//		 dmaster.setTotalApplyForm(lstStudentNotes.size());
//		 
//         
//		 model.addAttribute("dashboard",dmaster );
//		return "Dashboard/index";
//	}
//	
//}