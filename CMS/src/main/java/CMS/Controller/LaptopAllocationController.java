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

import CMS.Master.LaptopAllocationMaster;
import CMS.Master.LaptopMaster;
import CMS.Master.StudentMaster;
import CMS.Service.LaptopAllocationService;
import CMS.Service.LaptopService;
import CMS.Service.StudentService;

@Controller
public class LaptopAllocationController {

    @Autowired
    private LaptopAllocationService laptopAllocationService;

    @Autowired
    private LaptopService laptopService;
    
    @Autowired
    private StudentService studentService;

    @GetMapping("/LaptopAllocations")
    public String viewLaptopAllocations(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/LaptopAllocationpage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        Page<LaptopAllocationMaster> page = laptopAllocationService.findPaginated(pageNo, pageSize);
        List<LaptopAllocationMaster> listLaptopAllocations = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listLaptopAllocations", listLaptopAllocations);

        return "/LaptopAllocation/LaptopAllocation_Main";
    }

    @GetMapping("/LaptopAllocation/new")
    public String createLaptopAllocation(Model model) {
        model.addAttribute("laptopAllocation", new LaptopAllocationMaster());
        List<LaptopMaster> listLaptops = laptopService.getAllLaptops();
        List<StudentMaster> listStudents = studentService.getAllStudents();
        model.addAttribute("listLaptops", listLaptops);
        model.addAttribute("listStudents", listStudents);
        return "/LaptopAllocation/LaptopAllocation_Add";
    }


    @PostMapping("/AddLaptopAllocation")
    public String addLaptopAllocation(@ModelAttribute("laptopAllocation") LaptopAllocationMaster laptopAllocationMaster) {
    	laptopAllocationService.addLaptopAllocation(laptopAllocationMaster);
        return "redirect:/LaptopAllocations";
    }
    
    @GetMapping("/LaptopAllocation/edit/{id}")
    public String editLaptopAllocation(@PathVariable("id") int id, Model model) {
        LaptopAllocationMaster laptopAllocation = laptopAllocationService.findLaptopAllocationById(id);
        List<LaptopMaster> listLaptops = laptopService.getAllLaptops();
        List<StudentMaster> listStudents = studentService.getAllStudents();
        
        // Add the necessary model attributes
        model.addAttribute("laptopAllocation", laptopAllocation);
        model.addAttribute("listLaptops", listLaptops);
        model.addAttribute("listStudents", listStudents);
        
        return "/LaptopAllocation/LaptopAllocation_Edit";
    }


    @PostMapping("/UpdateLaptopAllocation/{id}")
    public String updateLaptopAllocation(@PathVariable("id") int id, @ModelAttribute("laptopAllocation") LaptopAllocationMaster laptopAllocationMaster) {
    	laptopAllocationMaster.setId(id); // Ensure the ID is set for the update
    	laptopAllocationService.updateLaptopAllocation(laptopAllocationMaster);
        return "redirect:/LaptopAllocations";
    }


    @GetMapping("/LaptopAllocation/{id}")
    public String deleteLaptopAllocation(@PathVariable("id") int id) {
    	laptopAllocationService.deleteLaptopAllocation(id);
        return "redirect:/LaptopAllocations";
    }

    @GetMapping("/LaptopAllocation/view/{id}")
    public String viewLaptopAllocation(@PathVariable("id") int id, Model model) {
    	LaptopAllocationMaster laptopAllocation = laptopAllocationService.findLaptopAllocationById(id);
        model.addAttribute("laptopAllocation", laptopAllocation);
        return "LaptopAllocation/LaptopAllocation_View";
    }
    
    @GetMapping("/GenerateLaptopAllocationPdfReport")
    public String generateLaptopAllocationPdfReport(Model model) {
        try {
            String file_name = "D:\\donotdelete\\soft\\pdf\\LaptopAllocationGenerated_pdf.pdf";
            List<LaptopAllocationMaster> lstlaptopAllocations = laptopAllocationService.getAllLaptopAllocations();
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file_name));
            doc.open();

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            // Create header cells with green background
            PdfPCell headerCell1 = new PdfPCell(new Phrase("Laptop Allocation ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell2 = new PdfPCell(new Phrase("Laptop Allocation Date", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell3 = new PdfPCell(new Phrase("Laptop Allocation Cost", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell4 = new PdfPCell(new Phrase("Laptop Allocation Remarks", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell5 = new PdfPCell(new Phrase("Laptop Serial No", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell6 = new PdfPCell(new Phrase("Student First Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

            headerCell1.setBorderWidth(1f);
            headerCell2.setBorderWidth(1f);
            headerCell3.setBorderWidth(1f);
            headerCell4.setBorderWidth(1f);
            headerCell5.setBorderWidth(1f);
            headerCell6.setBorderWidth(1f);

            // Set green background color
            headerCell1.setBackgroundColor(BaseColor.GREEN);
            headerCell2.setBackgroundColor(BaseColor.GREEN);
            headerCell3.setBackgroundColor(BaseColor.GREEN);
            headerCell4.setBackgroundColor(BaseColor.GREEN);
            headerCell5.setBackgroundColor(BaseColor.GREEN);
            headerCell6.setBackgroundColor(BaseColor.GREEN);

            headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell6.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(headerCell1);
            table.addCell(headerCell2);
            table.addCell(headerCell3);
            table.addCell(headerCell4);
            table.addCell(headerCell5);
            table.addCell(headerCell6);

            for (LaptopAllocationMaster laptopAllocationMaster : lstlaptopAllocations) {
                PdfPCell cell1 = new PdfPCell(new Phrase("" + laptopAllocationMaster.getId()));
                PdfPCell cell2 = new PdfPCell(new Phrase());
                PdfPCell cell3 = new PdfPCell(new Phrase());
                PdfPCell cell4 = new PdfPCell(new Phrase(laptopAllocationMaster.getRemarks()));
                PdfPCell cell5 = new PdfPCell(new Phrase(laptopAllocationMaster.getLaptopMaster().getLaptopSerialNo()));
                PdfPCell cell6 = new PdfPCell(new Phrase(laptopAllocationMaster.getStudentMaster().getStudentFirstName()));


                cell1.setBorderWidth(1f);
                cell2.setBorderWidth(1f);
                cell3.setBorderWidth(1f);
                cell4.setBorderWidth(1f);
                cell5.setBorderWidth(1f);
                cell6.setBorderWidth(1f);

                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell6.setHorizontalAlignment(Element.ALIGN_CENTER);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
                table.addCell(cell6);
            }

            doc.add(table);
            doc.close();

            System.out.print("Pdf Generated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/LaptopAllocations";
    }
    @GetMapping("/GenerateLaptopAllocationExcelReport")
    public ResponseEntity<byte[]> generateLaptopAllocationExcelReport(Model model) {
        try {
            // Fetch the list of cities from the service
            List<LaptopAllocationMaster> lstlaptopAllocations = laptopAllocationService.getAllLaptopAllocations();

            // Create a new workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("LaptopAllocation Report");

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
            headerCell.setCellValue("Laptop Allocation ID");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(1);
            headerCell.setCellValue("Laptop Allocation Date");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(2);
            headerCell.setCellValue("Laptop Allocation Cost");
            headerCell.setCellStyle(greenStyle);
            
            headerCell = headerRow.createCell(3);
            headerCell.setCellValue("Laptop Allocation Remarks");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(4);
            headerCell.setCellValue("Lapotp Serial No");
            headerCell.setCellStyle(greenStyle);
            
            headerCell = headerRow.createCell(5);
            headerCell.setCellValue("Student First Name ");
            headerCell.setCellStyle(greenStyle);

            // Populate data rows
            int rowNum = 1;
            for (LaptopAllocationMaster laptopAllocationMaster : lstlaptopAllocations) {
                Row row = sheet.createRow(rowNum++);
                Cell cell;

                cell = row.createCell(0);
                cell.setCellValue(laptopAllocationMaster.getId());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(1);
                cell.setCellValue(laptopAllocationMaster.getAllocationDate());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(2);
                cell.setCellValue(laptopAllocationMaster.getRemarks());
                cell.setCellStyle(defaultStyle);
                
                cell = row.createCell(3);
                cell.setCellValue(laptopAllocationMaster.getLaptopCost());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(4);
                cell.setCellValue(laptopAllocationMaster.getLaptopMaster().getLaptopSerialNo());
                cell.setCellStyle(defaultStyle);
                
                cell = row.createCell(5);
                cell.setCellValue(laptopAllocationMaster.getStudentMaster().getStudentFirstName());
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
            headers.setContentDisposition(ContentDisposition.attachment().filename("LaptopAllocation.xlsx").build());

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
