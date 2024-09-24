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
import CMS.Master.DailyUpdate;
import CMS.Service.BatchService;
import CMS.Service.DailyUpdateService;

@Controller
public class DailyUpdateController {

    @Autowired
    private DailyUpdateService dailyUpdateService;
    
    @Autowired
    BatchService batchService;

    @GetMapping("/DailyUpdates")
    public String viewDailyUpdates(Model model) {
        return findPaginated(1, model);
    }
    @GetMapping("/DailyUpdatePage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        try {
            Page<DailyUpdate> page = dailyUpdateService.findPaginated(pageNo, pageSize);
            List<DailyUpdate> listUpdates = page.getContent();

            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("listUpdates", listUpdates);
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "error";
        }

        return "DailyUpdate/DailyUpdate_Main";
    }


    @GetMapping("/DailyUpdate/new")
    public String createDailyUpdate(Model model) {
        model.addAttribute("dailyUpdate", new DailyUpdate());
        List<BatchMaster> listBatches = batchService.getAllBatches();
        model.addAttribute("listBatches", listBatches);
        return "/DailyUpdate/DailyUpdate_Add";
    }

    @PostMapping("/AddDailyUpdate")
    public String addDailyUpdate(@ModelAttribute("dailyUpdate") DailyUpdate dailyUpdate) {
        dailyUpdateService.addUpdate(dailyUpdate);
        return "redirect:/DailyUpdates";
    }
    
    @GetMapping("/DailyUpdate/edit/{id}")
    public String editDailyUpdate(@PathVariable("id") int id, Model model) {
        DailyUpdate update = dailyUpdateService.findUpdateById(id);
        model.addAttribute("dailyUpdate", update);
        List<BatchMaster> listBatches = batchService.getAllBatches();
        model.addAttribute("listBatches", listBatches);
        return "/DailyUpdate/DailyUpdate_Edit";
    }

    @PostMapping("/UpdateDailyUpdate/{id}")
    public String updateDailyUpdate(@PathVariable("id") int id, @ModelAttribute("dailyUpdate") DailyUpdate dailyUpdate) {
        dailyUpdate.setUpdateId(id); // Ensure the ID is set for the update
        dailyUpdateService.updateUpdate(dailyUpdate);
        return "redirect:/DailyUpdates";
    }

    @GetMapping("/DailyUpdate/{id}")
    public String deleteDailyUpdate(@PathVariable("id") int id) {
        dailyUpdateService.deleteUpdate(id);
        return "redirect:/DailyUpdates";
    }

    @GetMapping("/DailyUpdate/view/{id}")
    public String viewDailyUpdate(@PathVariable("id") int id, Model model) {
        DailyUpdate update = dailyUpdateService.findUpdateById(id);
        model.addAttribute("dailyUpdate", update);
        return "/DailyUpdate/DailyUpdate_View";
    }
    
    @GetMapping("/GenerateDailyUpdatePdfReport")
    public String generateDailyUpdatePdfReport(Model model) {
        try {
            String fileName = "D:\\donotdelete\\soft\\pdf\\DailyUpdateGenerated_pdf.pdf";
            List<DailyUpdate> lstUpdates = dailyUpdateService.getAllUpdates();
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(fileName));
            doc.open();

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            // Create header cells with green background
            PdfPCell headerCell1 = new PdfPCell(new Phrase("Update ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell2 = new PdfPCell(new Phrase("Message", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell3 = new PdfPCell(new Phrase("Date", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell4 = new PdfPCell(new Phrase("Batch", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

            headerCell1.setBorderWidth(1f);
            headerCell2.setBorderWidth(1f);
            headerCell3.setBorderWidth(1f);
            headerCell4.setBorderWidth(1f);

            // Set green background color
            headerCell1.setBackgroundColor(BaseColor.GREEN);
            headerCell2.setBackgroundColor(BaseColor.GREEN);
            headerCell3.setBackgroundColor(BaseColor.GREEN);
            headerCell4.setBackgroundColor(BaseColor.GREEN);

            headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell4.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(headerCell1);
            table.addCell(headerCell2);
            table.addCell(headerCell3);
            table.addCell(headerCell4);

            for (DailyUpdate update : lstUpdates) {
                PdfPCell cell1 = new PdfPCell(new Phrase("" + update.getUpdateId()));
                PdfPCell cell2 = new PdfPCell(new Phrase(update.getMessage()));
                PdfPCell cell3 = new PdfPCell(new Phrase(update.getUpdateDate().toString())); // Assuming Date.toString() is appropriate
                PdfPCell cell4 = new PdfPCell(new Phrase(update.getBatchMaster().getBatchName())); // Assuming getBatchName() exists

                cell1.setBorderWidth(1f);
                cell2.setBorderWidth(1f);
                cell3.setBorderWidth(1f);
                cell4.setBorderWidth(1f);

                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
            }

            doc.add(table);
            doc.close();

            System.out.print("Pdf Generated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/DailyUpdates";
    }

    @GetMapping("/GenerateDailyUpdateExcelReport")
    public ResponseEntity<byte[]> generateDailyUpdateExcelReport(Model model) {
        try {
            // Fetch the list of daily updates from the service
            List<DailyUpdate> lstUpdates = dailyUpdateService.getAllUpdates();

            // Create a new workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Daily Update Report");

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
            headerCell.setCellValue("Update ID");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(1);
            headerCell.setCellValue("Message");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(2);
            headerCell.setCellValue("Date");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(3);
            headerCell.setCellValue("Batch");
            headerCell.setCellStyle(greenStyle);

            // Populate data rows
            int rowNum = 1;
            for (DailyUpdate update : lstUpdates) {
                Row row = sheet.createRow(rowNum++);
                Cell cell;

                cell = row.createCell(0);
                cell.setCellValue(update.getUpdateId());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(1);
                cell.setCellValue(update.getMessage());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(2);
                cell.setCellValue(update.getUpdateDate().toString()); // Assuming Date.toString() is appropriate
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(3);
                cell.setCellValue(update.getBatchMaster().getBatchName()); // Assuming getBatchName() exists
                cell.setCellStyle(defaultStyle);
            }

            // Auto-size columns
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the workbook content to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close(); // Close the workbook to free up resources

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment().filename("DailyUpdate_Report.xlsx").build());

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
