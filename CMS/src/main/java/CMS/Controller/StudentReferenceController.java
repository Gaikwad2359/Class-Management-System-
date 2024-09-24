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

import CMS.Master.CourseMaster;
import CMS.Master.FacultyMaster;
import CMS.Master.StateMaster;
import CMS.Master.StudentMaster;
import CMS.Master.StudentReference;
import CMS.Service.StudentReferenceService;
import CMS.Service.StudentService;

@Controller
public class StudentReferenceController {

    @Autowired
    private StudentReferenceService service;
    
    @Autowired
    private StudentService studentService;

    @GetMapping("/StudentReferences")
    public String viewStudentReferences(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/StudentReferencePage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        Page<StudentReference> page = service.findPaginated(pageNo, pageSize);
        List<StudentReference> listStudentReferences = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listStudentReferences", listStudentReferences);

        return "StudentReference/StudentReference_Main";
    }

    @GetMapping("/StudentReference/new")
    public String createStudentReference(Model model) {
        model.addAttribute("studentReference", new StudentReference());
        List<StudentMaster> listStudents = studentService.getAllStudents();
        model.addAttribute("listStudents", listStudents);
        return "/StudentReference/StudentReference_Add";
    }

    @PostMapping("/StudentReference/add")
    public String addStudentReference(@ModelAttribute("studentReference") StudentReference studentReference) {
        service.addStudentReference(studentReference);
        return "redirect:/StudentReferences";
    }

    @GetMapping("/StudentReference/edit/{id}")
    public String editStudentReference(@PathVariable("id") int id, Model model) {
        StudentReference studentReference = service.findStudentReferenceById(id);
        model.addAttribute("studentReference", studentReference);
        List<StudentMaster> listStudents = studentService.getAllStudents();
        model.addAttribute("listStudents", listStudents);
        return "/StudentReference/StudentReference_Edit";
    }

    @PostMapping("/UpdateStudentReference/{id}")
    public String updateStudentReference(@PathVariable("id") Integer id, @ModelAttribute("studentReference") StudentReference studentReference) {
        studentReference.setReferenceId(id); // Ensure the ID is set correctly
        service.updateStudentReference(studentReference, id);
        return "redirect:/StudentReferences";
    }

    @GetMapping("/StudentReference/{id}")
    public String deleteStudentReference(@PathVariable("id") int id) {
        service.deleteStudentReference(id);
        return "redirect:/StudentReferences";
    }

    @GetMapping("/StudentReference/view/{id}")
    public String viewStudentReference(@PathVariable("id") int id, Model model) {
        StudentReference studentReference = service.findStudentReferenceById(id);
        model.addAttribute("studentReference", studentReference);
        return "StudentReference/StudentReference_View";
    }

    @GetMapping("/GenerateStudentReferencePdfReport")
    public String generateStudentReferencePdfReport(Model model) {
        try {
            String file_name = "D:\\donotdelete\\soft\\pdf\\StudentReferenceGenerated_pdf.pdf";
            List<StudentReference> lstStudentReferences = service.getAllStudentReferences();
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file_name));
            doc.open();

            PdfPTable table = new PdfPTable(5); 
            table.setWidthPercentage(100);

            String[] headers = {"Student Reference ID", "Student Reference Name", "Student Name"};

            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                headerCell.setBorderWidth(1f);
                headerCell.setBackgroundColor(BaseColor.GREEN);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(headerCell);
            }

            for (StudentReference batch : lstStudentReferences) {
                PdfPCell[] cells = new PdfPCell[9];
                cells[0] = new PdfPCell(new Phrase("" + batch.getReferenceId()));
                cells[1] = new PdfPCell(new Phrase(batch.getReferenceStudentName()));
                cells[2] = new PdfPCell(new Phrase(batch.getStudentMaster() != null ? batch.getStudentMaster().getStudentFirstName() : ""));
                cells[3] = new PdfPCell(new Phrase(batch.getStudentMaster() != null ? batch.getStudentMaster().getStudentLastName() : ""));

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
        }

        return "redirect:/StudentReferences";
    }

    @GetMapping("/GenerateStudentReferenceExcelReport")
    public ResponseEntity<byte[]> generateStudentReferenceExcelReport(Model model) {
        try {
            List<StudentReference> lststudentreferences = service.getAllStudentReferences();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Student Reference Report");

            CellStyle greenStyle = workbook.createCellStyle();
            greenStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle defaultStyle = workbook.createCellStyle();

            Row headerRow = sheet.createRow(0);
            Cell headerCell;

            headerCell = headerRow.createCell(0);
            headerCell.setCellValue("Student Reference ID");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(1);
            headerCell.setCellValue("Student Reference Name ");
            headerCell.setCellStyle(greenStyle);

            
            headerCell = headerRow.createCell(2);
            headerCell.setCellValue("Student First Name ");
            headerCell.setCellStyle(greenStyle);
            
            headerCell = headerRow.createCell(3);
            headerCell.setCellValue("Student Last Name");
            headerCell.setCellStyle(greenStyle);

            // Populate data rows
            int rowNum = 1;
            for (StudentReference studentReference : lststudentreferences) {
                Row row = sheet.createRow(rowNum++);
                Cell cell;

                cell = row.createCell(0);
                cell.setCellValue(studentReference.getReferenceId());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(1);
                cell.setCellValue(studentReference.getReferenceStudentName());
                cell.setCellStyle(defaultStyle);

                
                cell = row.createCell(2);
                cell.setCellValue(studentReference.getStudentMaster().getStudentFirstName());
                cell.setCellStyle(defaultStyle);
                
                cell = row.createCell(3);
                cell.setCellValue(studentReference.getStudentMaster().getStudentLastName());
                cell.setCellStyle(defaultStyle);
            }

            // Auto-size columns
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the workbook content to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close(); // Close the workbook to free up resources

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment().filename("StudentReference_Report.xlsx").build());

            // Return Excel file as ResponseEntity
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception here, maybe redirect to an error page or log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
}
