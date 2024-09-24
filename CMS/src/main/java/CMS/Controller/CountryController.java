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

import CMS.Master.CountryMaster;
import CMS.Service.CountryService;

@Controller
public class CountryController {

    @Autowired
    CountryService countryService;

    @GetMapping("/Countries")
    public String viewCountries(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/Countrypage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        Page<CountryMaster> page = countryService.findPaginated(pageNo, pageSize);
        List<CountryMaster> listCountries = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listCountries", listCountries);
        return "/Country/Country_Main";
    }

    @GetMapping("/Country/new")
    public String createCountry(Model model) {
        model.addAttribute("country", new CountryMaster());
        return "/Country/Country_Add";
    }

    @PostMapping("/AddCountry")
    public String addCountry(@ModelAttribute("country") CountryMaster countryMaster, Model model) {
        if (countryMaster.getCountryName() == null || countryMaster.getCountryName().isEmpty()) {
            model.addAttribute("error", "Country name is required");
            return "/Country/Country_Add";
        }
        countryService.addCountry(countryMaster);
        return "redirect:/Countries";
    }

    @GetMapping("/Country/edit/{id}")
    public String editCountry(@PathVariable("id") int id, Model model) {
        CountryMaster country = countryService.findCountryById(id);
        model.addAttribute("country", country);
        return "/Country/Country_Edit";
    }

    @PostMapping("/UpdateCountry/{id}")
    public String updateCountry(@PathVariable("id") int id, @ModelAttribute("country") CountryMaster countryMaster) {
        countryService.updateCountry(countryMaster, id);
        return "redirect:/Countries";
    }

    @GetMapping("/Country/{id}")
    public String deleteCountry(@PathVariable("id") int id) {
        countryService.deleteCountry(id);
        return "redirect:/Countries";
    }

    @GetMapping("/Country/view/{id}")
    public String viewCountry(@PathVariable("id") int id, Model model) {
        CountryMaster country = countryService.findCountryById(id);
        model.addAttribute("country", country);
        return "Country/Country_View";
    }
    @GetMapping("/GenerateCountryPdfReport")
    public String generateCountryPdfReport(Model model) {
        try {
            String file_name = "D:\\donotdelete\\soft\\pdf\\CountryGenerated_pdf.pdf";
            List<CountryMaster> lstCountries = countryService.getAllCountries();
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file_name));
            doc.open();

            PdfPTable table = new PdfPTable(3); // Adjusted to match the number of columns
            table.setWidthPercentage(100);

            // Create header cells with green background
            PdfPCell headerCell1 = new PdfPCell(new Phrase("Country ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell2 = new PdfPCell(new Phrase("Country Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell3 = new PdfPCell(new Phrase("Country Description", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

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

            for (CountryMaster country : lstCountries) {
                PdfPCell cell1 = new PdfPCell(new Phrase("" + country.getCountryId()));
                PdfPCell cell2 = new PdfPCell(new Phrase(country.getCountryName()));
                PdfPCell cell3 = new PdfPCell(new Phrase(country.getCountryDesc()));

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

        return "redirect:/Countries";
    }

    
    @GetMapping("/GenerateCountryExcelReport")
    public ResponseEntity<byte[]> generateCountryExcelReport() {
        try {
            List<CountryMaster> lstCountries = countryService.getAllCountries();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Countries");

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
            cell0.setCellValue("Country ID");
            cell0.setCellStyle(headerStyle);

            Cell cell1 = header.createCell(1);
            cell1.setCellValue("Country Name");
            cell1.setCellStyle(headerStyle);

            Cell cell2 = header.createCell(2);
            cell2.setCellValue("Country Description");
            cell2.setCellStyle(headerStyle);

            int rowNum = 1;
            for (CountryMaster country : lstCountries) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(country.getCountryId());
                row.createCell(1).setCellValue(country.getCountryName());
                row.createCell(2).setCellValue(country.getCountryDesc());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("CountryReport.xlsx").build());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
