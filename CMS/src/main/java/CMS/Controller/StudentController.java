package CMS.Controller;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import CMS.DAO.StudentDAO;
import CMS.Master.CityMaster;
import CMS.Master.CountryMaster;
import CMS.Master.QualificationMaster;
import CMS.Master.StateMaster;
import CMS.Master.StudentMaster;
import CMS.Service.CityService;
import CMS.Service.CountryService;
import CMS.Service.QualificationService;
import CMS.Service.StateService;
import CMS.Service.StudentService;

@Controller
//@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CountryService countryService;
    
    @Autowired
    private StateService stateService;
    
    @Autowired
    private CityService cityService;
    
    @Autowired
    private QualificationService qualificationService;
    
    //login
    
//    @Autowired
//	private StudentDAO studentRepo;
//
//	@ModelAttribute
//	public void commonUser(Principal p, Model m) {
//		if (p != null) {
//			String email = p.getName();
//			StudentMaster student = studentRepo.findByEmail(email);
//			m.addAttribute("student", student);
//		}
//
//	}	
//	
//	@GetMapping("/profile")
//	public String profile() {
//		return "profile";
//	}
    
    
    @GetMapping("/Students")
    public String viewStudents(Model model) {
        return findPaginated(1, model);
    }

    
    @GetMapping("/Studentpage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        try {
            Page<StudentMaster> page = studentService.findPaginated(pageNo, pageSize);
            List<StudentMaster> listStudents = page.getContent();

            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("listStudents", listStudents);
            
            System.out.println("Current Page: " + pageNo);
            System.out.println("Total Pages: " + page.getTotalPages());
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "error";
        }

        return "Student/Student_Main";
    }
    

    @GetMapping("/Student/new")
    public String createStudent(Model model) {
        model.addAttribute("student", new StudentMaster());
        List<CountryMaster> listCountries = countryService.getAllCountries();
        List<StateMaster> listStates = stateService.getAllStates();
        List<CityMaster> listCities = cityService.getAllCities();
        List<QualificationMaster> listQualifications = qualificationService.getAllQualifications();
        model.addAttribute("listCountries", listCountries);
        model.addAttribute("listStates", listStates);
        model.addAttribute("listCities", listCities);
        model.addAttribute("listQualifications", listQualifications);
        return "/Student/Student_Add";
    }

    @PostMapping("/AddStudent")
    public String addStudent(@ModelAttribute("student") StudentMaster studentMaster) {
    	studentService.addStudent(studentMaster);
        return "redirect:/Students";
    }




    @GetMapping("/Student/edit/{id}")
    public String editStudent(@PathVariable("id") int id, Model model) {
    	StudentMaster student = studentService.findStudentById(id);
        List<CityMaster> cities = cityService.getAllCities();
        List<StateMaster> states = stateService.getAllStates();
        List<CountryMaster> countries = countryService.getAllCountries();
        List<QualificationMaster> qualifications = qualificationService.getAllQualifications();

        model.addAttribute("student", student);
        model.addAttribute("cities", cities);
        model.addAttribute("states", states);
        model.addAttribute("countries", countries);
        model.addAttribute("qualifications", qualifications);

        return "/Student/Student_Edit";
    }
    @PostMapping("/UpdateStudent/{id}")
    public String updateStudent(@PathVariable("id") int id, @ModelAttribute("student") StudentMaster studentMaster) {
        // Ensure the id from the path variable is set in the student object
        studentMaster.setStudentId(id);
        studentService.updateStudent(studentMaster, id);
        return "redirect:/Students";
    }

    @GetMapping("/Student/{id}")
    public String deleteStudent(@PathVariable("id") int id) {
        studentService.deleteStudent(id);
        return "redirect:/Students";
    }

    @GetMapping("/Student/view/{id}")
    public String viewStudent(@PathVariable("id") int id, Model model) {
        StudentMaster student = studentService.findStudentById(id);
        model.addAttribute("student", student);
        return "Student/Student_View";
    }
    @GetMapping("/GenerateStudentPdfReport")
    public String generateStudentPdfReport(Model model) {
        String fileName = "D:\\donotdelete\\soft\\pdf\\StudentGenerated_pdf.pdf";
        
        try {
            List<StudentMaster> lstStudents = studentService.getAllStudents();
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(fileName));
            doc.open();

            PdfPTable table = new PdfPTable(13); // Adjusted to match the number of columns
            table.setWidthPercentage(100);

            // Create header cells with green background
            String[] headers = {"Student ID", "First Name", "Middle Name", "Last Name", "Mobile No", "Email ID",
                    "Reference", "Balance", "Status", "City", "State", 
                    "Country", "Qualification"};

            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                headerCell.setBorderWidth(1f);
                headerCell.setBackgroundColor(BaseColor.GREEN);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(headerCell);
            }

            for (StudentMaster student : lstStudents) {
                PdfPCell[] cells = new PdfPCell[13];
                cells[0] = new PdfPCell(new Phrase("" + student.getStudentId()));
                cells[1] = new PdfPCell(new Phrase(student.getStudentFirstName()));
                cells[2] = new PdfPCell(new Phrase(student.getStudentMiddleName()));
                cells[3] = new PdfPCell(new Phrase(student.getStudentLastName()));
                cells[4] = new PdfPCell(new Phrase(student.getStudentMobileNo()));
                cells[5] = new PdfPCell(new Phrase(student.getStudentEmailId()));
                cells[6] = new PdfPCell(new Phrase(student.getStudentReference()));
                cells[7] = new PdfPCell(new Phrase("" + student.getStudentbalance()));
                cells[8] = new PdfPCell(new Phrase(student.getStatus()));
                cells[9] = new PdfPCell(new Phrase(student.getCityMaster() != null ? student.getCityMaster().getCityName() : ""));
                cells[10] = new PdfPCell(new Phrase(student.getStateMaster() != null ? student.getStateMaster().getStateName() : ""));
                cells[11] = new PdfPCell(new Phrase(student.getCountryMaster() != null ? student.getCountryMaster().getCountryName() : ""));
                cells[12] = new PdfPCell(new Phrase(student.getQualificationMaster() != null ? student.getQualificationMaster().getQualName() : ""));

                for (PdfPCell cell : cells) {
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }

            doc.add(table);
            doc.close();

            System.out.print("Pdf Generated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception here, maybe redirect to an error page or log the error
        }

        return "redirect:/Students";
    }
    
    @GetMapping("/GenerateStudentExcelReport")
    public ResponseEntity<byte[]> generateStudentExcelReport() {
        try {
            List<StudentMaster> lstStudents = studentService.getAllStudents();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Students");

            // Create a cell style for the header row
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Set the background color of the header row to light green
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            String[] headers = {"Student ID", "First Name", "Middle Name", "Last Name", "Mobile No", "Email ID",
                                "Reference", "Balance", "Status", "City", "State", 
                                "Country", "Qualification"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (StudentMaster student : lstStudents) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(student.getStudentId());
                row.createCell(1).setCellValue(student.getStudentFirstName());
                row.createCell(2).setCellValue(student.getStudentMiddleName());
                row.createCell(3).setCellValue(student.getStudentLastName());
                row.createCell(4).setCellValue(student.getStudentMobileNo());
                row.createCell(5).setCellValue(student.getStudentEmailId());
                row.createCell(6).setCellValue(student.getStudentReference());
                row.createCell(7).setCellValue(student.getStudentbalance());
                row.createCell(8).setCellValue(student.getStatus());
                row.createCell(9).setCellValue(student.getCityMaster() != null ? student.getCityMaster().getCityName() : "");
                row.createCell(10).setCellValue(student.getStateMaster() != null ? student.getStateMaster().getStateName() : "");
                row.createCell(11).setCellValue(student.getCountryMaster() != null ? student.getCountryMaster().getCountryName() : "");
                row.createCell(12).setCellValue(student.getQualificationMaster() != null ? student.getQualificationMaster().getQualName() : "");
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            HttpHeaders headersResponse = new HttpHeaders();
            headersResponse.setContentDisposition(ContentDisposition.builder("attachment").filename("StudentReport.xlsx").build());
            headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity<>(outputStream.toByteArray(), headersResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/searchStudents")
    public String searchStudents(
            @RequestParam(value = "studentFirstName", required = false) String studentFirstName,
            @RequestParam(value = "studentLastName", required = false) String studentLastName,
            Model model) {

        List<StudentMaster> searchResults;
        
        if ((studentFirstName != null && !studentFirstName.isEmpty()) ||
            (studentLastName != null && !studentLastName.isEmpty())) {
            searchResults = studentService.searchByStudentName(studentFirstName, studentLastName);
        } else {
            searchResults = new ArrayList<>(); // Return empty list if no search criteria
        }
        
        model.addAttribute("listStudents", searchResults);
        model.addAttribute("currentPage", 1); // Reset to page 1 for new search
        model.addAttribute("totalPages", 1); // Only one page for search results
        model.addAttribute("totalItems", searchResults.size()); // Total items from search results
        return "Student/Student_Main"; // Redirect to the main page to display search results
    }
}
