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
import CMS.Master.CourseMaster;
import CMS.Master.FacultyMaster;
import CMS.Service.BatchService;
import CMS.Service.CourseService;
import CMS.Service.FacultyService;

@Controller
public class BatchController {

    @Autowired
    private BatchService service;

    @Autowired
    private CourseService courseService;
    
    @Autowired
    private FacultyService facultyService;

    @GetMapping("/Batches")
    public String viewBatches(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/Batchpage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        Page<BatchMaster> page = service.findPaginated(pageNo, pageSize);
        List<BatchMaster> listBatches = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listBatches", listBatches);

        return "Batch/Batch_Main";
    }
    
    
    @GetMapping("/Batch/new")
    public String createBatch(Model model) {
        model.addAttribute("batch", new BatchMaster());

        // Fetch all courses and faculties
        List<CourseMaster> listCourses = courseService.getAllCourses();
        List<FacultyMaster> listFaculties = facultyService.getAllFaculties();

        // Add the lists to the model
        model.addAttribute("listCourses", listCourses);
        model.addAttribute("listFaculties", listFaculties);

        return "/Batch/Batch_Add";
    }

    @PostMapping("/AddBatch")
    public String addBatch(@ModelAttribute("batch") BatchMaster batchMaster) {
        service.addBatch(batchMaster);
        return "redirect:/Batches";
    }

    @GetMapping("/Batch/edit/{id}")
    public String editBatch(@PathVariable("id") int id, Model model) {
        BatchMaster batch = service.findBatchById(id);
        List<CourseMaster> listCourses = courseService.getAllCourses();
        List<FacultyMaster> listFaculties = facultyService.getAllFaculties();
        model.addAttribute("batch", batch);
        model.addAttribute("listCourses", listCourses);
        model.addAttribute("listFaculties", listFaculties);
        return "/Batch/Batch_Edit";
    }


    @PostMapping("/UpdateBatch/{id}")
    public String updateBatch(@PathVariable("id") Integer id, @ModelAttribute("batch") BatchMaster batchMaster) {
        batchMaster.setBatchId(id); // Ensure the ID is set correctly
        service.updateBatch(batchMaster, 0);
        return "redirect:/Batches";
    }


    @GetMapping("/Batch/{id}")
    public String deleteBatch(@PathVariable("id") int id) {
        service.deleteBatch(id);
        return "redirect:/Batches";
    }

    @GetMapping("/Batch/view/{id}")
    public String viewBatch(@PathVariable("id") int id, Model model) {
    	BatchMaster batch = service.findBatchById(id);
        model.addAttribute("batch", batch);
        return "Batch/Batch_View";
    }
    
    @GetMapping("/GenerateBatchPdfReport")
    public String generateBatchPdfReport(Model model) {
        try {
            String file_name = "D:\\donotdelete\\soft\\pdf\\BatchGenerated_pdf.pdf";
            List<BatchMaster> lstBatches = service.getAllBatches();
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file_name));
            doc.open();

            PdfPTable table = new PdfPTable(9); // 9 columns for BatchMaster attributes
            table.setWidthPercentage(100);

            // Create header cells with green background
            String[] headers = {"Batch ID", "Batch Name", "Batch Start Date", "Batch End Date", 
                                "Batch Details", "Batch Remarks", "Batch Status", "Course Name", "Faculty Name"};

            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                headerCell.setBorderWidth(1f);
                headerCell.setBackgroundColor(BaseColor.GREEN);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(headerCell);
            }

            for (BatchMaster batch : lstBatches) {
                PdfPCell[] cells = new PdfPCell[9];
                cells[0] = new PdfPCell(new Phrase("" + batch.getBatchId()));
                cells[1] = new PdfPCell(new Phrase(batch.getBatchName()));
                cells[2] = new PdfPCell(new Phrase(batch.getBatchStartDate().toString()));
                cells[3] = new PdfPCell(new Phrase(batch.getBatchEndDate().toString()));
                cells[4] = new PdfPCell(new Phrase(batch.getBatchDetails()));
                cells[5] = new PdfPCell(new Phrase(batch.getBatchRemarks()));
                cells[6] = new PdfPCell(new Phrase(batch.getBatchStatus()));
                cells[7] = new PdfPCell(new Phrase(batch.getCourseMaster() != null ? batch.getCourseMaster().getCourseName() : ""));
                cells[8] = new PdfPCell(new Phrase(batch.getFacultyMaster() != null ? batch.getFacultyMaster().getFacultyFirstName() : ""));

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

        return "redirect:/Batches";
    }


    @GetMapping("/GenerateBatchExcelReport")
    public ResponseEntity<byte[]> generateCityExcelReport(Model model) {
        try {
            // Fetch the list of cities from the service
            List<BatchMaster> lstbatches = service.getAllBatches();

            // Create a new workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Batch Report");

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
            headerCell.setCellValue("Batch ID");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(1);
            headerCell.setCellValue("Batch Name");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(2);
            headerCell.setCellValue("Batch Start Date");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(3);
            headerCell.setCellValue("Batch End Date ");
            headerCell.setCellStyle(greenStyle);
            
            headerCell = headerRow.createCell(4);
            headerCell.setCellValue("Batch Details ");
            headerCell.setCellStyle(greenStyle);
            
            headerCell = headerRow.createCell(5);
            headerCell.setCellValue("Batch Remarks ");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(6);
            headerCell.setCellValue("Batch Status ");
            headerCell.setCellStyle(greenStyle);

            
            headerCell = headerRow.createCell(7);
            headerCell.setCellValue("Course Name ");
            headerCell.setCellStyle(greenStyle);
            
            headerCell = headerRow.createCell(8);
            headerCell.setCellValue("Faculty Name ");
            headerCell.setCellStyle(greenStyle);

            // Populate data rows
            int rowNum = 1;
            for (BatchMaster batchMaster : lstbatches) {
                Row row = sheet.createRow(rowNum++);
                Cell cell;

                cell = row.createCell(0);
                cell.setCellValue(batchMaster.getBatchId());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(1);
                cell.setCellValue(batchMaster.getBatchName());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(2);
                cell.setCellValue(batchMaster.getBatchStartDate());
                cell.setCellStyle(defaultStyle);
                
                cell = row.createCell(3);
                cell.setCellValue(batchMaster.getBatchEndDate());
                cell.setCellStyle(defaultStyle);
                
                cell = row.createCell(4);
                cell.setCellValue(batchMaster.getBatchDetails());
                cell.setCellStyle(defaultStyle);
                
                cell = row.createCell(5);
                cell.setCellValue(batchMaster.getBatchRemarks());
                cell.setCellStyle(defaultStyle);
                
                cell = row.createCell(6);
                cell.setCellValue(batchMaster.getBatchStatus());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(7);
                cell.setCellValue(batchMaster.getCourseMaster().getCourseName());
                cell.setCellStyle(defaultStyle);
                
                cell = row.createCell(8);
                cell.setCellValue(batchMaster.getFacultyMaster().getFacultyFirstName());
                cell.setCellStyle(defaultStyle);
            }

            // Auto-size columns
            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the workbook content to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close(); // Close the workbook to free up resources

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment().filename("Batch_Report.xlsx").build());

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
    
//    
//    @GetMapping("/batches/between-dates")
//    public String getBatchesBetweenDates(
//        @RequestParam("startDate") String startDateStr,
//        @RequestParam("endDate") String endDateStr,
//        Model model) {
//
//        LocalDate startDate = LocalDate.parse(startDateStr);
//        LocalDate endDate = LocalDate.parse(endDateStr);
//
//        List<BatchMaster> batches = service.getBatchesBetweenDates(startDate, endDate);
//        model.addAttribute("batches", batches);
//        return "batchesBetweenDates";
//    }
    
  
    
}
