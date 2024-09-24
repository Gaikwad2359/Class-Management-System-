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

import CMS.Master.StudentInstallmentMaster;
import CMS.Master.StudentMaster;
import CMS.Service.StudentInstallmentService;
import CMS.Service.StudentService;

@Controller
public class StudentInstallmentController {

    @Autowired
    private StudentInstallmentService studentInstallmentService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/StudentInstallments")
    public String viewStudentInstallments(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/StudentInstallmentPage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        Page<StudentInstallmentMaster> page = studentInstallmentService.findPaginated(pageNo, pageSize);
        List<StudentInstallmentMaster> listInstallments = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listInstallments", listInstallments);

        return "/StudentInstallment/StudentInstallment_Main";
    }

    @GetMapping("/StudentInstallment/new")
    public String createStudentInstallment(Model model) {
        model.addAttribute("studentInstallment", new StudentInstallmentMaster());
        List<StudentMaster> listStudents = studentService.getAllStudents();
        model.addAttribute("listStudents", listStudents);
        return "/StudentInstallment/StudentInstallment_Add";
    }

    @PostMapping("/AddStudentInstallment")
    public String addStudentInstallment(@ModelAttribute("studentInstallment") StudentInstallmentMaster studentInstallmentMaster) {
        studentInstallmentService.addInstallment(studentInstallmentMaster);
        return "redirect:/StudentInstallments";
    }

    @GetMapping("/StudentInstallment/edit/{id}")
    public String editStudentInstallment(@PathVariable("id") int id, Model model) {
        StudentInstallmentMaster installment = studentInstallmentService.findInstallmentById(id);
        List<StudentMaster> listStudents = studentService.getAllStudents();
        model.addAttribute("studentInstallment", installment);
        model.addAttribute("listStudents", listStudents);
        return "/StudentInstallment/StudentInstallment_Edit";
    }

    @PostMapping("/UpdateStudentInstallment/{id}")
    public String updateStudentInstallment(@PathVariable("id") int id, @ModelAttribute("studentInstallment") StudentInstallmentMaster studentInstallmentMaster) {
        studentInstallmentMaster.setInstId(id); // Ensure the ID is set for the update
        studentInstallmentService.updateInstallment(studentInstallmentMaster, id);
        return "redirect:/StudentInstallments";
    }

    @GetMapping("/StudentInstallment/{id}")
    public String deleteStudentInstallment(@PathVariable("id") int id) {
        studentInstallmentService.deleteInstallment(id);
        return "redirect:/StudentInstallments";
    }

    @GetMapping("/StudentInstallment/view/{id}")
    public String viewStudentInstallment(@PathVariable("id") int id, Model model) {
        StudentInstallmentMaster installment = studentInstallmentService.findInstallmentById(id);
        model.addAttribute("studentInstallment", installment);
        return "StudentInstallment/StudentInstallment_View";
    }

    @GetMapping("/GenerateStudentInstallmentPdfReport")
    public String generateStudentInstallmentPdfReport(Model model) {
        try {
            String fileName = "D:\\donotdelete\\soft\\pdf\\StudentInstallmentGenerated_pdf.pdf";
            List<StudentInstallmentMaster> installments = studentInstallmentService.getAllInstallments();
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(fileName));
            doc.open();

            // Define columns for StudentInstallment
            PdfPTable table = new PdfPTable(8); // Adjusted to match the number of columns
            table.setWidthPercentage(100);

            // Create header cells with green background
            String[] headers = {"Installment ID", "Installment Date", "Amount", "Payment Type", "Cheque No", "Cheque Date", "Student First Name", "Student Last Name "};

            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                headerCell.setBorderWidth(1f);
                headerCell.setBackgroundColor(BaseColor.GREEN);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(headerCell);
            }

            for (StudentInstallmentMaster installment : installments) {
                PdfPCell[] cells = new PdfPCell[8];
                cells[0] = new PdfPCell(new Phrase(String.valueOf(installment.getInstId())));
                cells[1] = new PdfPCell(new Phrase(installment.getInstDate() != null ? installment.getInstDate().toString() : ""));
                cells[2] = new PdfPCell(new Phrase(String.valueOf(installment.getInstAmount())));
                cells[3] = new PdfPCell(new Phrase(installment.getInstPaymentType()));
                cells[4] = new PdfPCell(new Phrase(installment.getInstPayChequeNo()));
                cells[5] = new PdfPCell(new Phrase(installment.getInstPayChequeDate() != null ? installment.getInstPayChequeDate().toString() : ""));
                cells[6] = new PdfPCell(new Phrase(installment.getStudentMaster() != null ? String.valueOf(installment.getStudentMaster().getStudentFirstName()) : ""));
                cells[7] = new PdfPCell(new Phrase(installment.getStudentMaster() != null ? String.valueOf(installment.getStudentMaster().getStudentLastName()) : ""));

                for (PdfPCell cell : cells) {
                    cell.setBorderWidth(1f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }

            doc.add(table);
            doc.close();

            System.out.println("PDF Generated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception here, maybe redirect to an error page or log the error
        }

        return "redirect:/StudentInstallments";
    }


    @GetMapping("/GenerateStudentInstallmentExcelReport")
    public ResponseEntity<byte[]> generateStudentInstallmentExcelReport(Model model) {
        try {
            // Fetch the list of student installments from the service
            List<StudentInstallmentMaster> installments = studentInstallmentService.getAllInstallments();

            // Create a new workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Student Installment Report");

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
            headerCell.setCellValue("Installment ID");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(1);
            headerCell.setCellValue("Installment Date");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(2);
            headerCell.setCellValue("Amount");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(3);
            headerCell.setCellValue("Payment Type");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(4);
            headerCell.setCellValue("Cheque No");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(5);
            headerCell.setCellValue("Cheque Date");
            headerCell.setCellStyle(greenStyle);

            // Populate data rows
            int rowNum = 1;
            for (StudentInstallmentMaster installment : installments) {
                Row row = sheet.createRow(rowNum++);
                Cell cell;

                cell = row.createCell(0);
                cell.setCellValue(installment.getInstId());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(1);
                cell.setCellValue(installment.getInstDate() != null ? installment.getInstDate().toString() : "");
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(2);
                cell.setCellValue(installment.getInstAmount());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(3);
                cell.setCellValue(installment.getInstPaymentType());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(4);
                cell.setCellValue(installment.getInstPayChequeNo());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(5);
                cell.setCellValue(installment.getInstPayChequeDate() != null ? installment.getInstPayChequeDate().toString() : "");
                cell.setCellStyle(defaultStyle);
            }

            // Auto-size columns
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the workbook content to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close(); // Close the workbook to free up resources

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment().filename("StudentInstallment_Report.xlsx").build());

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
    
    
    
  //Student Installment Bar Diagram 
//    @GetMapping("/StudentInstallmentReport")
//    public String viewStudentInstallmentReport(Model model) {
//        List<Object[]> yearlyInstallments = studentInstallmentService.getYearlyInstallments();
//        List<Object[]> monthlyInstallments = studentInstallmentService.getMonthlyInstallments();
//
//        model.addAttribute("yearlyInstallments", yearlyInstallments);
//        model.addAttribute("monthlyInstallments", monthlyInstallments);
//
//        return "StudentInstallment/StudentInstallment_Report";
//    }

}
