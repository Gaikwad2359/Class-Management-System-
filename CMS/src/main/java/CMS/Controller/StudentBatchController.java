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

import CMS.Master.BatchMaster;
import CMS.Master.StudentBatch;
import CMS.Master.StudentMaster;
import CMS.Service.BatchService;
import CMS.Service.StudentBatchService;
import CMS.Service.StudentService;

@Controller
public class StudentBatchController {

    @Autowired
    private StudentBatchService service;

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private BatchService batchService;

    @GetMapping("/StudentBatches")
    public String viewStudentBatches(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/StudentBatchpage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        try {
            Page<StudentBatch> page = service.findPaginated(pageNo, pageSize);
            List<StudentBatch> listStudentBatches = page.getContent();

            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("listStudentBatches", listStudentBatches);
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "error";
        }

        return "StudentBatch/StudentBatch_Main";
    }
    
    
    @GetMapping("/StudentBatch/new")
    public String createStudentBatch(Model model) {
        model.addAttribute("studentBatch", new StudentBatch());
        List<StudentMaster> listStudent = studentService.getAllStudents();
        List<BatchMaster> listBatches = batchService.getAllBatches();
        model.addAttribute("listStudent", listStudent);
        model.addAttribute("listBatches", listBatches);
        return "StudentBatch/StudentBatch_Add";
    }

    @PostMapping("/AddStudentBatch")
    public String addStudentBatch(@ModelAttribute("studentBatch") StudentBatch studentBatch) {
        service.addStudentBatch(studentBatch);
        return "redirect:/StudentBatches";
    }


    @GetMapping("/StudentBatch/edit/{id}")
    public String editStudentBatch(@PathVariable("id") int id, Model model) {
    	StudentBatch studentBatch = service.findStudentBatchById(id);
        List<StudentMaster> listStudents = studentService.getAllStudents();
        List<BatchMaster> listBatches = batchService.getAllBatches();
        model.addAttribute("studentBatch", studentBatch);
        model.addAttribute("listStudents", listStudents);
        model.addAttribute("listBatches", listBatches);
        return "/StudentBatch/StudentBatch_Edit";
    }

    @PostMapping("/UpdateStudentBatch/{id}")
    public String updateStudentBatch(@PathVariable("id") int id, @ModelAttribute("studentBatch") StudentBatch studentBatchMaster) {
    	studentBatchMaster.setId(id); // Ensure the ID is set for the update
        service.updateStudentBatch(studentBatchMaster, id);
        return "redirect:/StudentBatches";
    }


    @GetMapping("/StudentBatch/{id}")
    public String deleteStudentBatch(@PathVariable("id") int id) {
        service.deleteStudentBatch(id);
        return "redirect:/StudentBatches";
    }

    @GetMapping("/StudentBatch/view/{id}")
    public String viewStudentBatch(@PathVariable("id") int id, Model model) {
    	StudentBatch studentBatch = service.findStudentBatchById(id);
        model.addAttribute("studentBatch", studentBatch);
        return "StudentBatch/StudentBatch_View";
    }

    @GetMapping("/GenerateStudentBatchPdfReport")
    public String generateStudentBatchPdfReport(Model model) {
        try {
            String file_name = "D:\\donotdelete\\soft\\pdf\\StudentBatchGenerated_pdf.pdf";
            List<StudentBatch> lstStudentBatches = service.getAllStudentBatches();
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file_name));
            doc.open();

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            // Create header cells with green background
            PdfPCell headerCell1 = new PdfPCell(new Phrase("ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell2 = new PdfPCell(new Phrase("Remarks", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell3 = new PdfPCell(new Phrase("Join Date", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell4 = new PdfPCell(new Phrase("Student Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell5 = new PdfPCell(new Phrase("Batch Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

            headerCell1.setBorderWidth(1f);
            headerCell2.setBorderWidth(1f);
            headerCell3.setBorderWidth(1f);
            headerCell4.setBorderWidth(1f);
            headerCell5.setBorderWidth(1f);

            // Set green background color
            headerCell1.setBackgroundColor(BaseColor.GREEN);
            headerCell2.setBackgroundColor(BaseColor.GREEN);
            headerCell3.setBackgroundColor(BaseColor.GREEN);
            headerCell4.setBackgroundColor(BaseColor.GREEN);
            headerCell5.setBackgroundColor(BaseColor.GREEN);

            headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell5.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(headerCell1);
            table.addCell(headerCell2);
            table.addCell(headerCell3);
            table.addCell(headerCell4);
            table.addCell(headerCell5);

            for (StudentBatch studentBatch : lstStudentBatches) {
                PdfPCell cell1 = new PdfPCell(new Phrase("" + studentBatch.getId()));
                PdfPCell cell2 = new PdfPCell(new Phrase(studentBatch.getRemarks()));
                PdfPCell cell3 = new PdfPCell(new Phrase(studentBatch.getJoinDate().toString()));
                PdfPCell cell4 = new PdfPCell(new Phrase(studentBatch.getStudentMaster().getStudentFirstName() + " " + studentBatch.getStudentMaster().getStudentLastName()));
                PdfPCell cell5 = new PdfPCell(new Phrase(studentBatch.getBatchMaster().getBatchName()));

                cell1.setBorderWidth(1f);
                cell2.setBorderWidth(1f);
                cell3.setBorderWidth(1f);
                cell4.setBorderWidth(1f);
                cell5.setBorderWidth(1f);

                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
            }

            doc.add(table);
            doc.close();

            System.out.print("Pdf Generated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/StudentBatches";
    }
    @GetMapping("/GenerateStudentBatchExcelReport")
    public ResponseEntity<byte[]> generateStudentBatchExcelReport(Model model) {
        try {
            // Fetch the list of student batches from the service
            List<StudentBatch> lstStudentBatches = service.getAllStudentBatches();

            // Create a new workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("StudentBatch Report");

            // Create a green cell style for the header row
            CellStyle greenStyle = workbook.createCellStyle();
            greenStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Create a default cell style for data rows (no color)
            CellStyle defaultStyle = workbook.createCellStyle();

            // Create header row
            Row headerRow = sheet.createRow(0);
            Cell headerCell;

            headerCell = headerRow.createCell(0);
            headerCell.setCellValue("ID");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(1);
            headerCell.setCellValue("Remarks");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(2);
            headerCell.setCellValue("Join Date");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(3);
            headerCell.setCellValue("Student Name");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(4);
            headerCell.setCellValue("Batch Name");
            headerCell.setCellStyle(greenStyle);

            // Populate data rows
            int rowNum = 1;
            for (StudentBatch studentBatch : lstStudentBatches) {
                Row row = sheet.createRow(rowNum++);
                Cell cell;

                cell = row.createCell(0);
                cell.setCellValue(studentBatch.getId());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(1);
                cell.setCellValue(studentBatch.getRemarks());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(2);
                cell.setCellValue(studentBatch.getJoinDate().toString());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(3);
                cell.setCellValue(studentBatch.getStudentMaster().getStudentFirstName() + " " + studentBatch.getStudentMaster().getStudentLastName());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(4);
                cell.setCellValue(studentBatch.getBatchMaster().getBatchName());
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
            headers.setContentDisposition(ContentDisposition.attachment().filename("StudentBatch_Report.xlsx").build());

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
