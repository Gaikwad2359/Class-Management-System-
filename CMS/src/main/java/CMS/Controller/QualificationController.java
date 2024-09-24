package CMS.Controller;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
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

import CMS.Master.QualificationMaster;
import CMS.Service.QualificationService;

@Controller
public class QualificationController {

    @Autowired
    private QualificationService service;

    @GetMapping("/Qualifications")
    public String viewQualifications(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/Qualificationpage/{pageNo}")
	public String findPaginated(@PathVariable(value="pageNo") int pageNo, Model model) {
		int pageSize=5; 
		
		Page<QualificationMaster> page = service.findPaginated(pageNo, pageSize);
		
		List<QualificationMaster> listQualifications = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listQualifications",listQualifications);
		return "/Qualification/Qualification_Main";
	}

    @GetMapping("/Qualification/new")
    public String createQualification(Model model) {
        model.addAttribute("qualification", new QualificationMaster());
        return "Qualification/Qualification_Add";
    }

    @PostMapping("/AddQualification")
    public String addQualification(@ModelAttribute("qualification") QualificationMaster qualificationMaster) {
        service.addQualification(qualificationMaster);
        return "redirect:/Qualifications";
    }

    @GetMapping("/Qualification/edit/{id}")
    public String editQualification(@PathVariable("id") int id, Model model) {
        QualificationMaster qualification = service.findQualificationById(id);
        model.addAttribute("qualification", qualification);
        return "Qualification/Qualification_Edit";
    }

    @PostMapping("/UpdateQualification/{id}")
    public String updateQualification(@PathVariable("id") int id, @ModelAttribute("qualification") QualificationMaster qualificationMaster) {
        QualificationMaster existingQualification = service.findQualificationById(id);
        if (existingQualification != null) {
            existingQualification.setQualName(qualificationMaster.getQualName());
            existingQualification.setQualDesc(qualificationMaster.getQualDesc());
            service.updateQualification(existingQualification);
        }
        return "redirect:/Qualifications";
    }



    @GetMapping("/Qualification/delete/{id}")
    public String deleteQualification(@PathVariable("id") int id) {
        try {
            service.deleteQualification(id);
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
        return "redirect:/Qualifications"; // Redirect to the qualification list page
    }


    @GetMapping("/Qualification/view/{id}")
    public String viewQualification(@PathVariable("id") int id, Model model) {
        QualificationMaster qualification = service.findQualificationById(id);
        model.addAttribute("qualification", qualification);
        return "Qualification/Qualification_View";
    }
    @GetMapping("/GenerateQualificationPdfReport")
    public String generateQualificationPdfReport(Model model) {
        try {
            String file_name = "D:\\donotdelete\\soft\\pdf\\QualificationGenerated_pdf.pdf";
            List<QualificationMaster> lstQualifications = service.getAllQualifications();
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file_name));
            doc.open();

            PdfPTable table = new PdfPTable(3); // Adjusted to match the number of columns
            table.setWidthPercentage(100);

            // Create header cells with green background
            PdfPCell headerCell1 = new PdfPCell(new Phrase("Qualification ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell2 = new PdfPCell(new Phrase("Qualification Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell3 = new PdfPCell(new Phrase("Qualification Description", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

            headerCell1.setBorderWidth(1f);
            headerCell2.setBorderWidth(1f);
            headerCell3.setBorderWidth(1f);

            // Set green background color
            headerCell1.setBackgroundColor(BaseColor.GREEN);
            headerCell2.setBackgroundColor(BaseColor.GREEN);
            headerCell3.setBackgroundColor(BaseColor.GREEN);

            headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(headerCell1);
            table.addCell(headerCell2);
            table.addCell(headerCell3);

            for (QualificationMaster qualification : lstQualifications) {
                PdfPCell cell1 = new PdfPCell(new Phrase("" + qualification.getQualId()));
                PdfPCell cell2 = new PdfPCell(new Phrase(qualification.getQualName()));
                PdfPCell cell3 = new PdfPCell(new Phrase(qualification.getQualDesc()));

                cell1.setBorderWidth(1f);
                cell2.setBorderWidth(1f);
                cell3.setBorderWidth(1f);

                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
            }

            doc.add(table);
            doc.close();

            System.out.print("Pdf Generated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception here, maybe redirect to an error page or log the error
        }

        return "redirect:/Qualifications";
    }

    
    
    @GetMapping("/GenerateQualificationExcelReport")
    public ResponseEntity<byte[]> generateQualificationExcelReport(Model model) {
        try {
            List<QualificationMaster> lstQualifications = service.getAllQualifications();

            // Create a new workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Qualification Report");

            // Create header row
            Row headerRow = sheet.createRow(0);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Qualification ID");
            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Qualification Name");
            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("Qualification Description");

            // Style the header row
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCell1.setCellStyle(headerStyle);
            headerCell2.setCellStyle(headerStyle);
            headerCell3.setCellStyle(headerStyle);

            // Populate data rows
            int rowNum = 1;
            for (QualificationMaster qualification : lstQualifications) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(qualification.getQualId());
                row.createCell(1).setCellValue(qualification.getQualName());
                row.createCell(2).setCellValue(qualification.getQualDesc());
            }

            // Auto-size columns
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the workbook content to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment().filename("QualificationReport.xlsx").build());

            // Return Excel file as ResponseEntity
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception here, maybe redirect to an error page or log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
