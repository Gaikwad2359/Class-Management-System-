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
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import CMS.Master.LaptopMaster;
import CMS.Service.LaptopService;

@Controller
public class LaptopController {

    @Autowired
    private LaptopService laptopService;    

    @GetMapping("/Laptops")
    public String viewLaptops(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/Laptoppage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        try {
            Page<LaptopMaster> page = laptopService.findPaginated(pageNo, pageSize);
            List<LaptopMaster> listLaptops = page.getContent();

            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("listLaptops", listLaptops);
            
            System.out.println("Current Page: " + pageNo);
            System.out.println("Total Pages: " + page.getTotalPages());
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "error";
        }

        return "Laptop/Laptop_Main";
    }



    @GetMapping("/Laptop/new")
    public String createLaptop(Model model) {
        model.addAttribute("laptop", new LaptopMaster());
        return "/Laptop/Laptop_Add";
    }

    @PostMapping("/AddLaptop")
    public String addLaptop(@ModelAttribute("laptop") LaptopMaster laptopMaster) {
        laptopService.addLaptop(laptopMaster);
        return "redirect:/Laptops";
    }

    @GetMapping("/Laptop/edit/{id}")
	public String editLaptop(@PathVariable("id") int id, Model model) {
    	LaptopMaster laptop = laptopService.findLaptopById(id);
		model.addAttribute("laptop", laptop);
		return "/Laptop/Laptop_Edit";
	}

	@PostMapping("/UpdateLaptop/{id}")
	public String updateLaptop(@PathVariable("id") int id, @ModelAttribute("laptop") LaptopMaster laptopMaster) {
		laptopService.updateLaptop(laptopMaster, id);
		return "redirect:/Laptops";
	}

    @GetMapping("/Laptop/{id}")
    public String deleteLaptop(@PathVariable("id") int id) {
        laptopService.deleteLaptop(id);
        return "redirect:/Laptops";
    }

    @GetMapping("/Laptop/view/{id}")
    public String viewLaptop(@PathVariable("id") int id, Model model) {
    	LaptopMaster laptop = laptopService.findLaptopById(id);
        model.addAttribute("laptop", laptop);
        return "Laptop/Laptop_View";
    }

    @GetMapping("/GenerateLaptopPdfReport")
    public String generateLaptopPdfReport(Model model) {
        try {
            String file_name = "D:\\donotdelete\\soft\\pdf\\LaptopGenerated_pdf.pdf";
            List<LaptopMaster> lstLaptops = laptopService.getAllLaptops();
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file_name));
            doc.open();

            PdfPTable table = new PdfPTable(8); // Adjusted to match the number of columns
            table.setWidthPercentage(100);

            // Create header cells with green background
            PdfPCell headerCell1 = new PdfPCell(new Phrase("Laptop ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell2 = new PdfPCell(new Phrase("Purchase Date", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell3 = new PdfPCell(new Phrase("Serial No", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell4 = new PdfPCell(new Phrase("Purchased From", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell5 = new PdfPCell(new Phrase("Contact Details", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell6 = new PdfPCell(new Phrase("Cost", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell7 = new PdfPCell(new Phrase("Charger No", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell8 = new PdfPCell(new Phrase("Remarks", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

            headerCell1.setBorderWidth(1f);
            headerCell2.setBorderWidth(1f);
            headerCell3.setBorderWidth(1f);
            headerCell4.setBorderWidth(1f);
            headerCell5.setBorderWidth(1f);
            headerCell6.setBorderWidth(1f);
            headerCell7.setBorderWidth(1f);
            headerCell8.setBorderWidth(1f);

            // Set green background color
            headerCell1.setBackgroundColor(BaseColor.GREEN);
            headerCell2.setBackgroundColor(BaseColor.GREEN);
            headerCell3.setBackgroundColor(BaseColor.GREEN);
            headerCell4.setBackgroundColor(BaseColor.GREEN);
            headerCell5.setBackgroundColor(BaseColor.GREEN);
            headerCell6.setBackgroundColor(BaseColor.GREEN);
            headerCell7.setBackgroundColor(BaseColor.GREEN);
            headerCell8.setBackgroundColor(BaseColor.GREEN);

            headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell7.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell8.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(headerCell1);
            table.addCell(headerCell2);
            table.addCell(headerCell3);
            table.addCell(headerCell4);
            table.addCell(headerCell5);
            table.addCell(headerCell6);
            table.addCell(headerCell7);
            table.addCell(headerCell8);

            for (LaptopMaster laptop : lstLaptops) {
                PdfPCell cell1 = new PdfPCell(new Phrase("" + laptop.getLaptopId()));
                PdfPCell cell2 = new PdfPCell(new Phrase(laptop.getLaptopPurchaseDate().toString()));
                PdfPCell cell3 = new PdfPCell(new Phrase(laptop.getLaptopSerialNo()));
                PdfPCell cell4 = new PdfPCell(new Phrase(laptop.getLaptopPurchasedFrom()));
                PdfPCell cell5 = new PdfPCell(new Phrase(laptop.getLaptopPurchasedContactDetails()));
                PdfPCell cell6 = new PdfPCell(new Phrase("" + laptop.getLaptopCost()));
                PdfPCell cell7 = new PdfPCell(new Phrase(laptop.getLaptopChargerNo()));
                PdfPCell cell8 = new PdfPCell(new Phrase(laptop.getLaptopRemarks()));

                cell1.setBorderWidth(1f);
                cell2.setBorderWidth(1f);
                cell3.setBorderWidth(1f);
                cell4.setBorderWidth(1f);
                cell5.setBorderWidth(1f);
                cell6.setBorderWidth(1f);
                cell7.setBorderWidth(1f);
                cell8.setBorderWidth(1f);

                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell8.setHorizontalAlignment(Element.ALIGN_CENTER);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
                table.addCell(cell6);
                table.addCell(cell7);
                table.addCell(cell8);
            }

            doc.add(table);
            doc.close();

            System.out.print("Pdf Generated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception here, maybe redirect to an error page or log the error
        }

        return "redirect:/Laptops";
    }
    @GetMapping("/GenerateLaptopExcelReport")
    public ResponseEntity<byte[]> generateLaptopExcelReport() {
        try {
            List<LaptopMaster> lstLaptops = laptopService.getAllLaptops();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Laptops");

            // Create a cell style for the header row
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Set the background color of the header row to light green
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            Cell cell0 = header.createCell(0);
            cell0.setCellValue("Laptop ID");
            cell0.setCellStyle(headerStyle);

            Cell cell1 = header.createCell(1);
            cell1.setCellValue("Purchase Date");
            cell1.setCellStyle(headerStyle);

            Cell cell2 = header.createCell(2);
            cell2.setCellValue("Serial No");
            cell2.setCellStyle(headerStyle);

            Cell cell3 = header.createCell(3);
            cell3.setCellValue("Purchased From");
            cell3.setCellStyle(headerStyle);

            Cell cell4 = header.createCell(4);
            cell4.setCellValue("Contact Details");
            cell4.setCellStyle(headerStyle);

            Cell cell5 = header.createCell(5);
            cell5.setCellValue("Cost");
            cell5.setCellStyle(headerStyle);

            Cell cell6 = header.createCell(6);
            cell6.setCellValue("Charger No");
            cell6.setCellStyle(headerStyle);

            Cell cell7 = header.createCell(7);
            cell7.setCellValue("Remarks");
            cell7.setCellStyle(headerStyle);

            int rowNum = 1;
            for (LaptopMaster laptop : lstLaptops) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(laptop.getLaptopId());
                row.createCell(1).setCellValue(laptop.getLaptopPurchaseDate().toString());
                row.createCell(2).setCellValue(laptop.getLaptopSerialNo());
                row.createCell(3).setCellValue(laptop.getLaptopPurchasedFrom());
                row.createCell(4).setCellValue(laptop.getLaptopPurchasedContactDetails());
                row.createCell(5).setCellValue(laptop.getLaptopCost());
                row.createCell(6).setCellValue(laptop.getLaptopChargerNo());
                row.createCell(7).setCellValue(laptop.getLaptopRemarks());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("LaptopReport.xlsx").build());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/searchLaptops")
    public String searchLaptops(@RequestParam("laptopSerialNo") String laptopSerialNo, Model model) {
        List<LaptopMaster> searchResults = laptopService.searchByLaptopSerialNo(laptopSerialNo);
        model.addAttribute("listLaptops", searchResults);
        model.addAttribute("currentPage", 1); // Reset to page 1 for new search
        model.addAttribute("totalPages", 1); // Only one page for search results
        model.addAttribute("totalItems", searchResults.size()); // Total items from search results
        return "Laptop/Laptop_Main"; // Redirect to the main page to display search results
    }


}
