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
import CMS.Master.StateMaster;
import CMS.Service.CountryService;
import CMS.Service.StateService;

@Controller
public class StateController {
	
	 @Autowired
	    StateService stateService;
	 
	 @Autowired
	 CountryService countryService;


		@GetMapping("/States")
		public String viewStates(Model model) {
			return findPaginated(1, model);
		}
		
		@GetMapping("/Statepage/{pageNo}")
		public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
			int pageSize = 5;
			Page<StateMaster> page = stateService.findPaginated(pageNo, pageSize);
			List<StateMaster> listStates = page.getContent();

			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			model.addAttribute("listStates", listStates);

			return "/State/State_Main";
		}
		@GetMapping("/State/new")
		public String createState(Model model) {
		    model.addAttribute("state", new StateMaster());
		    model.addAttribute("countries", countryService.getAllCountries()); // Add this line
		    return "/State/State_Add";
		}

		@PostMapping("/AddState")
		public String addState(@ModelAttribute("state") StateMaster stateMaster) {
		    stateService.addState(stateMaster);
		    return "redirect:/States";
		}

		@GetMapping("/State/edit/{id}")
		public String editState(@PathVariable("id") int id, Model model) {
		    StateMaster state = stateService.findStateById(id);
		    List<CountryMaster> countries = countryService.findAllCountries(); // Assuming countryService is available
		    model.addAttribute("state", state);
		    model.addAttribute("countries", countries);
		    return "/State/State_Edit";
		}


		@PostMapping("/UpdateState/{id}")
		public String updateState(@PathVariable("id") int id, @ModelAttribute("state") StateMaster stateMaster) {
			stateService.updateState(stateMaster, id);
			return "redirect:/States";
		}

		@GetMapping("/State/{id}")
		public String deleteState(@PathVariable("id") int id) {
			stateService.deleteState(id);
			return "redirect:/States";
		}

		@GetMapping("/State/view/{id}")
		public String viewState(@PathVariable("id") int id, Model model) {
			StateMaster state = stateService.findStateById(id);
			model.addAttribute("state", state);
			return "State/State_View";
		}
		@GetMapping("/GenerateStatePdfReport")
		public String generateStatePdfReport(Model model) {
		    try {
		        String file_name = "D:\\donotdelete\\soft\\pdf\\StateGenerated_pdf.pdf";
		        List<StateMaster> lststates = stateService.getAllStates();
		        Document doc = new Document();
		        PdfWriter.getInstance(doc, new FileOutputStream(file_name));
		        doc.open();

		        // Create table with 4 columns
		        PdfPTable table = new PdfPTable(4);
		        table.setWidthPercentage(100); // Set table width to 100% of page width

		        // Add table header
		        PdfPCell headerCell1 = new PdfPCell(new Phrase("State ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
		        PdfPCell headerCell2 = new PdfPCell(new Phrase("State Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
		        PdfPCell headerCell3 = new PdfPCell(new Phrase("State Description", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
		        PdfPCell headerCell4 = new PdfPCell(new Phrase("Country Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

		        // Set header cell properties
		        headerCell1.setBorderWidth(1f); // Set border width
		        headerCell2.setBorderWidth(1f);
		        headerCell3.setBorderWidth(1f);
		        headerCell4.setBorderWidth(1f);

		        // Set header background color to green
		        headerCell1.setBackgroundColor(BaseColor.GREEN);
		        headerCell2.setBackgroundColor(BaseColor.GREEN);
		        headerCell3.setBackgroundColor(BaseColor.GREEN);
		        headerCell4.setBackgroundColor(BaseColor.GREEN);

		        // Set header text color to white for better contrast
		        headerCell1.setPhrase(new Phrase("State ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
		        headerCell2.setPhrase(new Phrase("State Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
		        headerCell3.setPhrase(new Phrase("State Description", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
		        headerCell4.setPhrase(new Phrase("Country Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));

		        // Set header text alignment to center
		        headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		        headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		        headerCell4.setHorizontalAlignment(Element.ALIGN_CENTER);

		        // Add header cells to table
		        table.addCell(headerCell1);
		        table.addCell(headerCell2);
		        table.addCell(headerCell3);
		        table.addCell(headerCell4);

		        // Populate table with data from database
		        for (StateMaster stateMaster : lststates) {
		            PdfPCell cell1 = new PdfPCell(new Phrase("" + stateMaster.getStateId()));
		            PdfPCell cell2 = new PdfPCell(new Phrase(stateMaster.getStateName()));
		            PdfPCell cell3 = new PdfPCell(new Phrase(stateMaster.getStateDescription()));
		            PdfPCell cell4 = new PdfPCell(new Phrase("" + (stateMaster.getCountryMaster() != null ? stateMaster.getCountryMaster().getCountryName() : "N/A")));

		            // Set cell properties
		            cell1.setBorderWidth(1f);
		            cell2.setBorderWidth(1f);
		            cell3.setBorderWidth(1f);
		            cell4.setBorderWidth(1f);

		            // Set cell text alignment to center
		            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);

		            // Add cells to table
		            table.addCell(cell1);
		            table.addCell(cell2);
		            table.addCell(cell3);
		            table.addCell(cell4);
		        }

		        // Add table to document
		        doc.add(table);

		        doc.close();

		        System.out.print("Pdf Generated Successfully");

		    } catch (Exception e) {
		        e.printStackTrace();
		        // Handle exception here, maybe redirect to an error page or log the error
		    }

		    return "redirect:/States";
		}

		@GetMapping("/GenerateStateExcelReport")
		public ResponseEntity<byte[]> generateStateExcelReport(Model model) {
		    try {
		        List<StateMaster> lststates = stateService.getAllStates();

		        // Create a new workbook
		        Workbook workbook = new XSSFWorkbook();
		        Sheet sheet = workbook.createSheet("State Report");

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
		        headerCell.setCellValue("State ID");
		        headerCell.setCellStyle(greenStyle);

		        headerCell = headerRow.createCell(1);
		        headerCell.setCellValue("State Name");
		        headerCell.setCellStyle(greenStyle);

		        headerCell = headerRow.createCell(2);
		        headerCell.setCellValue("State Desc");
		        headerCell.setCellStyle(greenStyle);

		        headerCell = headerRow.createCell(3);
		        headerCell.setCellValue("Country Name");
		        headerCell.setCellStyle(greenStyle);

		        // Populate data rows
		        int rowNum = 1;
		        for (StateMaster stateMaster : lststates) {
		            Row row = sheet.createRow(rowNum++);
		            Cell cell;

		            cell = row.createCell(0);
		            cell.setCellValue(stateMaster.getStateId());
		            cell.setCellStyle(defaultStyle);

		            cell = row.createCell(1);
		            cell.setCellValue(stateMaster.getStateName());
		            cell.setCellStyle(defaultStyle);

		            cell = row.createCell(2);
		            cell.setCellValue(stateMaster.getStateDescription());
		            cell.setCellStyle(defaultStyle);

		            cell = row.createCell(3);
		            cell.setCellValue(stateMaster.getCountryMaster().getCountryName());
		            cell.setCellStyle(defaultStyle);
		        }

		        // Auto-size columns
		        for (int i = 0; i < 4; i++) {
		            sheet.autoSizeColumn(i);
		        }

		        // Write the workbook content to a byte array
		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		        workbook.write(outputStream);

		        // Set response headers
		        HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		        headers.setContentDisposition(ContentDisposition.attachment().filename("State_Report.xlsx").build());

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