package CMS.Controller;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import CMS.Master.CityMaster;
import CMS.Master.CountryMaster;
import CMS.Master.FacultyMaster;
import CMS.Master.QualificationMaster;
import CMS.Master.StateMaster;
import CMS.Service.CityService;
import CMS.Service.CountryService;
import CMS.Service.FacultyService;
import CMS.Service.QualificationService;
import CMS.Service.StateService;

@Controller
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private CountryService countryService;
    
    @Autowired
    private StateService stateService;
    
    @Autowired
    private CityService cityService;
    
    @Autowired
    private QualificationService qualificationService;

    
    
    @GetMapping("/Faculties")
    public String viewCities(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/Facultypage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        Page<FacultyMaster> page = facultyService.findPaginated(pageNo, pageSize);
        List<FacultyMaster> listFaculties = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listFaculties", listFaculties);

        return "/Faculty/Faculty_Main";
    }
    @GetMapping("/Faculty/new")
    public String createFaculty(Model model) {
        model.addAttribute("faculty", new FacultyMaster());
        List<CountryMaster> listCountries = countryService.getAllCountries();
        List<StateMaster> listStates = stateService.getAllStates();
        List<CityMaster> listCities = cityService.getAllCities();
        List<QualificationMaster> listQualifications = qualificationService.getAllQualifications();
        model.addAttribute("listCountries", listCountries);
        model.addAttribute("listStates", listStates);
        model.addAttribute("listCities", listCities);
        model.addAttribute("listQualifications", listQualifications);
        return "/Faculty/Faculty_Add";
    }

    @PostMapping("/AddFaculty")
    public String addFaculty(@ModelAttribute("faculty") FacultyMaster facultyMaster) {
        facultyService.addFaculty(facultyMaster);
        return "redirect:/Faculties";
    }


    @GetMapping("/Faculty/edit/{id}")
    public String editFaculty(@PathVariable("id") int id, Model model) {
    	FacultyMaster faculty = facultyService.findFacultyById(id);
    	List<CountryMaster> listCountries = countryService.getAllCountries();
        List<StateMaster> listStates = stateService.getAllStates();
        List<CityMaster> listCities= cityService.getAllCities();
        List<QualificationMaster> listQualifications = qualificationService.getAllQualifications();
        model.addAttribute("faculty", faculty);
        model.addAttribute("listCountries", listCountries);
        model.addAttribute("listStates", listStates);
        model.addAttribute("listCities", listCities);
        model.addAttribute("listQualifications", listQualifications);
        
        return "/Faculty/Faculty_Edit";
    }

    @PostMapping("/UpdateFaculty/{id}")
    public String updateFaculty(@PathVariable("id") int id, @ModelAttribute("faculty") FacultyMaster facultyMaster) {
        facultyMaster.setFacultyId(id); // Ensure the ID is set for the update
        facultyService.updateFaculty(facultyMaster, id);
        return "redirect:/Faculties";
    }




    @GetMapping("/Faculty/{id}")
    public String deleteFaculty(@PathVariable("id") int id) {
        facultyService.deleteFaculty(id);
        return "redirect:/Faculties";
    }

    @GetMapping("/Faculty/view/{id}")
    public String viewFaculty(@PathVariable("id") int id, Model model) {
    	FacultyMaster faculty = facultyService.findFacultyById(id);
        model.addAttribute("faculty", faculty);
        return "Faculty/Faculty_View";
    }
    
    
    @GetMapping("/GenerateFacultyPdfReport")
    public String generateFacultyPdfReport(Model model) {
        try {
            String file_name = "D:\\donotdelete\\soft\\pdf\\FacultyGenerated_pdf.pdf";
            List<FacultyMaster> lstFaculties = facultyService.getAllFaculties();
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file_name));
            doc.open();

            PdfPTable table = new PdfPTable(14); // Adjusted to match the number of columns
            table.setWidthPercentage(100);

            // Create header cells with green background
            String[] headers = {"Faculty ID", "First Name", "Middle Name", "Last Name", "Mobile No", "Email ID",
                                "Alternate Mobile No", "Reference", "Status", "Balance", "City", "State", 
                                "Country", "Qualification"};

            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                headerCell.setBorderWidth(1f);
                headerCell.setBackgroundColor(BaseColor.GREEN);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(headerCell);
            }

            for (FacultyMaster faculty : lstFaculties) {
                PdfPCell[] cells = new PdfPCell[14];
                cells[0] = new PdfPCell(new Phrase("" + faculty.getFacultyId()));
                cells[1] = new PdfPCell(new Phrase(faculty.getFacultyFirstName()));
                cells[2] = new PdfPCell(new Phrase(faculty.getFacultyMiddleName()));
                cells[3] = new PdfPCell(new Phrase(faculty.getFacultyLastName()));
                cells[4] = new PdfPCell(new Phrase(faculty.getFacultyMobileNo()));
                cells[5] = new PdfPCell(new Phrase(faculty.getFacultyEmailId()));
                cells[6] = new PdfPCell(new Phrase(faculty.getFacultyAlternateMobileNumber()));
                cells[7] = new PdfPCell(new Phrase(faculty.getFacultyReference()));
                cells[8] = new PdfPCell(new Phrase(faculty.getFacultyStatus()));
                cells[9] = new PdfPCell(new Phrase());
                cells[10] = new PdfPCell(new Phrase(faculty.getCityMaster() != null ? faculty.getCityMaster().getCityName() : ""));
                cells[11] = new PdfPCell(new Phrase(faculty.getStateMaster() != null ? faculty.getStateMaster().getStateName() : ""));
                cells[12] = new PdfPCell(new Phrase(faculty.getCountryMaster() != null ? faculty.getCountryMaster().getCountryName() : ""));
                cells[13] = new PdfPCell(new Phrase(faculty.getQualificationMaster() != null ? faculty.getQualificationMaster().getQualName() : ""));

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

        return "redirect:/Faculties";
    }

    @GetMapping("/GenerateFacultyExcelReport")
    public ResponseEntity<byte[]> generateFacultyExcelReport() {
        try {
            List<FacultyMaster> lstFaculties = facultyService.getAllFaculties();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Faculties");

            // Create a cell style for the header row
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Set the background color of the header row to light green
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            String[] headers = {"Faculty ID", "First Name", "Middle Name", "Last Name", "Mobile No", "Email ID",
                                "Alternate Mobile No", "Reference", "Status", "Balance", "City", "State", 
                                "Country", "Qualification"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (FacultyMaster faculty : lstFaculties) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(faculty.getFacultyId());
                row.createCell(1).setCellValue(faculty.getFacultyFirstName());
                row.createCell(2).setCellValue(faculty.getFacultyMiddleName());
                row.createCell(3).setCellValue(faculty.getFacultyLastName());
                row.createCell(4).setCellValue(faculty.getFacultyMobileNo());
                row.createCell(5).setCellValue(faculty.getFacultyEmailId());
                row.createCell(6).setCellValue(faculty.getFacultyAlternateMobileNumber());
                row.createCell(7).setCellValue(faculty.getFacultyReference());
                row.createCell(8).setCellValue(faculty.getFacultyStatus());
                row.createCell(9).setCellValue(faculty.getFacultyBalance());
                row.createCell(10).setCellValue(faculty.getCityMaster() != null ? faculty.getCityMaster().getCityName() : "");
                row.createCell(11).setCellValue(faculty.getStateMaster() != null ? faculty.getStateMaster().getStateName() : "");
                row.createCell(12).setCellValue(faculty.getCountryMaster() != null ? faculty.getCountryMaster().getCountryName() : "");
                row.createCell(13).setCellValue(faculty.getQualificationMaster() != null ? faculty.getQualificationMaster().getQualName() : "");
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            HttpHeaders headersResponse = new HttpHeaders();
            headersResponse.setContentDisposition(ContentDisposition.builder("attachment").filename("FacultyReport.xlsx").build());
            headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity<>(outputStream.toByteArray(), headersResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

}