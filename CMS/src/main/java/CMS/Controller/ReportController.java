
package CMS.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import CMS.Master.BatchMaster;
import CMS.Master.CourseMaster;
import CMS.Master.FacultyMaster;
import CMS.Master.StudentInstallmentMaster;
import CMS.Service.BatchService;
import CMS.Service.CourseService;
import CMS.Service.FacultyService;
import CMS.Service.StudentInstallmentService;

@Controller
public class ReportController {

    @Autowired
    private BatchService batchService;
    
    @Autowired
    private CourseService courseService;

    @GetMapping("/report/form")
    public ModelAndView showDateSelectionForm() {
        ModelAndView modelAndView = new ModelAndView("report/reportForm");
        return modelAndView;
    }
    
    @GetMapping("/report/batch")
    public ModelAndView generateBatchReport(
            @RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr) {

        ModelAndView modelAndView = new ModelAndView("report/batchesWithinDatesReport");

        if (startDateStr == null || endDateStr == null) {
            modelAndView.setViewName("report/error");
            modelAndView.addObject("message", "Start date and end date are required.");
            return modelAndView;
        }

        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            List<BatchMaster> batches = batchService.findBatchesWithinDates(startDate, endDate);
            modelAndView.addObject("batches", batches);
        } catch (DateTimeParseException e) {
            modelAndView.setViewName("report/error");
            modelAndView.addObject("message", "Invalid date format. Please use yyyy-MM-dd.");
        } catch (Exception e) {
            modelAndView.setViewName("report/error");
            modelAndView.addObject("message", "An unexpected error occurred: " + e.getMessage());
        }

        return modelAndView;
    }
    
    
    
    //courses 
    
    @GetMapping("/report/form/course")
    public ModelAndView showDateSelectionForm1() {
        ModelAndView modelAndView = new ModelAndView("report/reportFormCourse");
        return modelAndView;
    }
    
    @GetMapping("/report/course")
    public ModelAndView generateCourseReport(
            @RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr) {

        ModelAndView modelAndView = new ModelAndView("report/coursesWithinDatesReport");

        if (startDateStr == null || endDateStr == null) {
            modelAndView.setViewName("report/error");
            modelAndView.addObject("message", "Start date and end date are required.");
            return modelAndView;
        }

        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            List<CourseMaster> courses = courseService.findCoursesBetweenDates(startDate, endDate);
            modelAndView.addObject("courses", courses);
        } catch (DateTimeParseException e) {
            modelAndView.setViewName("report/error");
            modelAndView.addObject("message", "Invalid date format. Please use yyyy-MM-dd.");
        } catch (Exception e) {
            modelAndView.setViewName("report/error");
            modelAndView.addObject("message", "An unexpected error occurred: " + e.getMessage());
        }

        return modelAndView;
    }
    
    
    @Autowired
    private FacultyService facultyService;

    @GetMapping("/report/facultyBalance")
    public ModelAndView generateFacultyBalanceReport() {
        ModelAndView modelAndView = new ModelAndView("report/facultyBalanceReport");

        try {
            List<FacultyMaster> faculties = facultyService.findFacultiesWithBalanceGreaterThanZero();
            modelAndView.addObject("faculties", faculties);
        } catch (Exception e) {
            modelAndView.setViewName("report/error");
            modelAndView.addObject("message", "An unexpected error occurred: " + e.getMessage());
        }

        return modelAndView;
    }
    
    
    
//    @Autowired
//    private StudentService studentService;
//
//    @GetMapping("/report/studentBalance")
//    public ModelAndView generateStudentBalanceReport() {
//        ModelAndView modelAndView = new ModelAndView("report/studentBalanceReport");
//
//        try {
//            List<StudentMaster> students = studentService.findStudentsWithBalanceGreaterThanZero();
//            modelAndView.addObject("students", students);
//        } catch (Exception e) {
//            modelAndView.setViewName("report/error");
//            modelAndView.addObject("message", "An unexpected error occurred: " + e.getMessage());
//        }
//	
//        return modelAndView;
//    }
    
    
    
    //Student Installment

    @Autowired
    private StudentInstallmentService studentInstallmentMasterService;

    // Show the date selection form
    @GetMapping("/installments/select-dates")
    public String showDateSelectionForm2() {
        return "report/installment-date-selection";
    }

    // Show the report for the selected dates
    @GetMapping("/report/studentInstallment")
    public String showInstallmentsReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        List<StudentInstallmentMaster> installments = studentInstallmentMasterService.getInstallmentsBetweenDates(startDate, endDate);
        model.addAttribute("installments", installments);
        return "report/installment-report";
    }

}
	
	