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
import CMS.Master.StateMaster;
import CMS.Service.CityService;
import CMS.Service.StateService;

@Controller
public class CityController {

    @Autowired
    private CityService cityService;

    @Autowired
    private StateService stateService;

    @GetMapping("/Cities")
    public String viewCities(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/Citypage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        Page<CityMaster> page = cityService.findPaginated(pageNo, pageSize);
        List<CityMaster> listCities = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listCities", listCities);

        return "/City/City_Main";
    }

    @GetMapping("/City/new")
    public String createCity(Model model) {
        model.addAttribute("city", new CityMaster());
        List<StateMaster> listStates = stateService.getAllStates();
        model.addAttribute("listStates", listStates);
        return "/City/City_Add";
    }


    @PostMapping("/AddCity")
    public String addCity(@ModelAttribute("city") CityMaster cityMaster) {
        cityService.addCity(cityMaster);
        return "redirect:/Cities";
    }
    
    @GetMapping("/City/edit/{id}")
    public String editCity(@PathVariable("id") int id, Model model) {
        CityMaster city = cityService.findCityById(id);
        List<StateMaster> listStates = stateService.getAllStates();
        model.addAttribute("city", city);
        model.addAttribute("listStates", listStates);
        return "/City/City_Edit";
    }

    @PostMapping("/UpdateCity/{id}")
    public String updateCity(@PathVariable("id") int id, @ModelAttribute("city") CityMaster cityMaster) {
        cityMaster.setCityId(id); // Ensure the ID is set for the update
        cityService.updateCity(cityMaster);
        return "redirect:/Cities";
    }


    @GetMapping("/City/{id}")
    public String deleteCity(@PathVariable("id") int id) {
        cityService.deleteCity(id);
        return "redirect:/Cities";
    }

    @GetMapping("/City/view/{id}")
    public String viewCity(@PathVariable("id") int id, Model model) {
        CityMaster city = cityService.findCityById(id);
        model.addAttribute("city", city);
        return "City/City_View";
    }
    
    @GetMapping("/GenerateCityPdfReport")
    public String generateCityPdfReport(Model model) {
        try {
            String file_name = "D:\\donotdelete\\soft\\pdf\\CityGenerated_pdf.pdf";
            List<CityMaster> lstcities = cityService.getAllCities();
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file_name));
            doc.open();

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            // Create header cells with green background
            PdfPCell headerCell1 = new PdfPCell(new Phrase("City ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell2 = new PdfPCell(new Phrase("City Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell3 = new PdfPCell(new Phrase("City Description", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell4 = new PdfPCell(new Phrase("State Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

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

            for (CityMaster cityMaster : lstcities) {
                PdfPCell cell1 = new PdfPCell(new Phrase("" + cityMaster.getCityId()));
                PdfPCell cell2 = new PdfPCell(new Phrase(cityMaster.getCityName()));
                PdfPCell cell3 = new PdfPCell(new Phrase(cityMaster.getCityDesc()));
                PdfPCell cell4 = new PdfPCell(new Phrase(cityMaster.getStateMaster().getStateName()));

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

        return "redirect:/Cities";
    }
    @GetMapping("/GenerateCityExcelReport")
    public ResponseEntity<byte[]> generateCityExcelReport(Model model) {
        try {
            // Fetch the list of cities from the service
            List<CityMaster> lstcities = cityService.getAllCities();

            // Create a new workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("City Report");

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
            headerCell.setCellValue("City ID");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(1);
            headerCell.setCellValue("City Name");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(2);
            headerCell.setCellValue("City Description");
            headerCell.setCellStyle(greenStyle);

            headerCell = headerRow.createCell(3);
            headerCell.setCellValue("State Name");
            headerCell.setCellStyle(greenStyle);

            // Populate data rows
            int rowNum = 1;
            for (CityMaster cityMaster : lstcities) {
                Row row = sheet.createRow(rowNum++);
                Cell cell;

                cell = row.createCell(0);
                cell.setCellValue(cityMaster.getCityId());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(1);
                cell.setCellValue(cityMaster.getCityName());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(2);
                cell.setCellValue(cityMaster.getCityDesc());
                cell.setCellStyle(defaultStyle);

                cell = row.createCell(3);
                cell.setCellValue(cityMaster.getStateMaster().getStateName());
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
            headers.setContentDisposition(ContentDisposition.attachment().filename("City_Report.xlsx").build());

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
