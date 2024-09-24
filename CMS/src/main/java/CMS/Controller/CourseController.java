package CMS.Controller;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import CMS.Master.CourseMaster;
import CMS.Service.CourseService;
import CMS.config.EmailService;
import CMS.config.SmsService;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private SmsService smsService;

    private static final String RAZORPAY_KEY_ID = "rzp_test_HCTUbGUU3bXXQ6";
    private static final String RAZORPAY_KEY_SECRET = "DRTmi8DXC3M3WUQonV7UizQj";

    @GetMapping("/Course")
    public String viewAllCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "Course/Course_list";
    }

    @GetMapping("/Course/new")
    public String addCourseForm(Model model) {
        model.addAttribute("course", new CourseMaster());
        return "Course/Course_Add";
    }

    @PostMapping("/Course")
    public String saveCourse(@ModelAttribute("course") CourseMaster course) {
        courseService.addCourse(course);
        return "redirect:/Course";
    }

    @GetMapping("/Course/edit/{id}")
    public String editCourseForm(@PathVariable Integer id, Model model) {
        model.addAttribute("course", courseService.findCourseById(id));
        return "Course/Course_Edit";
    }

    @PostMapping("/Course/update/{id}")
    public String updateCourse(@PathVariable Integer id, @ModelAttribute("course") CourseMaster course) {
        CourseMaster existingCourse = courseService.findCourseById(id);
        if (existingCourse != null) {
            existingCourse.setCourseName(course.getCourseName());
            existingCourse.setCourseDesc(course.getCourseDesc());
            existingCourse.setCourseFees(course.getCourseFees());
            existingCourse.setCourseStartDate(course.getCourseStartDate());
            existingCourse.setCourseEndDate(course.getCourseEndDate());
            existingCourse.setStatus(course.getStatus());
            courseService.addCourse(existingCourse);
        }
        return "redirect:/Course";
    }

    @GetMapping("/Course/delete/{id}")
    public String deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);
        return "redirect:/Course";
    }

    @GetMapping("/Course/view/{id}")
    public String viewCourse(@PathVariable Integer id, Model model) {
        model.addAttribute("course", courseService.findCourseById(id));
        return "Course/Course_View";
    }
    
    @GetMapping("/Course/purchase/{id}")
    public String purchaseCourse(@PathVariable Integer id, Model model) {
        model.addAttribute("course", courseService.findCourseById(id));
        return "Course/Course_Purchase";
    }

    @PostMapping("/Course/purchase/{id}")
    public String handlePurchase(@PathVariable Integer id, @RequestParam String name, @RequestParam String qualification,
                                 @RequestParam String email, @RequestParam String mobile, Model model, RedirectAttributes redirectAttributes) {
        CourseMaster course = courseService.findCourseById(id);

        // Create Razorpay Order
        try {
            RazorpayClient client = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);

            JSONObject options = new JSONObject();
            options.put("amount", Integer.parseInt(course.getCourseFees()) * 100); // amount in paise
            options.put("currency", "INR");
            options.put("receipt", "txn_123456");

            Order order = client.Orders.create(options);
            String orderId = order.get("id");

            // Add attributes for Razorpay payment
            model.addAttribute("razorpayKeyId", RAZORPAY_KEY_ID);
            model.addAttribute("orderId", orderId);
            model.addAttribute("amount", Integer.parseInt(course.getCourseFees()));
            model.addAttribute("name", name);
            model.addAttribute("email", email);
            model.addAttribute("mobile", mobile);
            model.addAttribute("course", course);

            // Send email notification
            String subject = "Course Purchase Confirmation";
            String text = "Dear " + name + ",\n\n" +
                          "Thank you for purchasing the course: " + course.getCourseName() + ".\n" +
                          "Your order ID is: " + orderId + ".\n" +
                          "Amount: " + course.getCourseFees() + " INR\n\n" +
                          "Best regards,\n" +
                          "The Course Team";
            emailService.sendSimpleMessage(email, subject, text);

            // Send SMS notification
            String smsMessage = "Thank you for purchasing the course: " + course.getCourseName() + ". Your order ID is: " + orderId + ". Amount: " + course.getCourseFees() + " INR.";
            smsService.sendSms(mobile, smsMessage);

            return "Course/Course_Payment";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error processing your purchase. Please try again.");
            return "redirect:/Course";
        }
    }

    @PostMapping("/Course/payment/success")
    public String paymentSuccess(@RequestParam String razorpayPaymentId, @RequestParam String razorpayOrderId,
                                 @RequestParam String razorpaySignature, Model model) {
        // Handle payment success
        String message = "Payment successful. Payment ID: " + razorpayPaymentId;
        model.addAttribute("message", message);
        return "Course/Payment_Success";
    }

    
    @GetMapping("/GenerateCoursePdfReport")
    public String generateCoursePdfReport(Model model) {
        try {
            String fileName = "D:\\donotdelete\\soft\\pdf\\CourseGenerated_pdf.pdf";
            List<CourseMaster> lstCourses = courseService.getAllCourses(); // Adjust service method
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(fileName));
            doc.open();

            PdfPTable table = new PdfPTable(7); // Adjusted to match the number of columns
            table.setWidthPercentage(100);

            // Create header cells with green background
            PdfPCell headerCell1 = new PdfPCell(new Phrase("Course ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell2 = new PdfPCell(new Phrase("Course Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell3 = new PdfPCell(new Phrase("Description", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell4 = new PdfPCell(new Phrase("Fees", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell5 = new PdfPCell(new Phrase("Start Date", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell6 = new PdfPCell(new Phrase("End Date", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell headerCell7 = new PdfPCell(new Phrase("Status", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

            // Set green background color
            headerCell1.setBackgroundColor(BaseColor.GREEN);
            headerCell2.setBackgroundColor(BaseColor.GREEN);
            headerCell3.setBackgroundColor(BaseColor.GREEN);
            headerCell4.setBackgroundColor(BaseColor.GREEN);
            headerCell5.setBackgroundColor(BaseColor.GREEN);
            headerCell6.setBackgroundColor(BaseColor.GREEN);
            headerCell7.setBackgroundColor(BaseColor.GREEN);

            headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell7.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(headerCell1);
            table.addCell(headerCell2);
            table.addCell(headerCell3);
            table.addCell(headerCell4);
            table.addCell(headerCell5);
            table.addCell(headerCell6);
            table.addCell(headerCell7);

            for (CourseMaster course : lstCourses) {
                PdfPCell cell1 = new PdfPCell(new Phrase("" + course.getCourseId()));
                PdfPCell cell2 = new PdfPCell(new Phrase(course.getCourseName()));
                PdfPCell cell3 = new PdfPCell(new Phrase(course.getCourseDesc()));
                PdfPCell cell4 = new PdfPCell(new Phrase(course.getCourseFees()));
                PdfPCell cell5 = new PdfPCell(new Phrase(course.getCourseStartDate().toString()));
                PdfPCell cell6 = new PdfPCell(new Phrase(course.getCourseEndDate().toString()));
                PdfPCell cell7 = new PdfPCell(new Phrase(course.getStatus()));

                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell7.setHorizontalAlignment(Element.ALIGN_CENTER);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
                table.addCell(cell6);
                table.addCell(cell7);
            }

            doc.add(table);
            doc.close();

            System.out.print("Pdf Generated Successfully");

        } catch (Exception e) {
            e.printStackTrace();
            // Redirect to error page or display an error message
            return "error"; // Ensure you have an error.html page
        }

        return "redirect:/Course"; // Ensure this view exists or adjust to a valid URL
    }
    @GetMapping("/GenerateCourseExcelReport")
    public ResponseEntity<byte[]> generateCourseExcelReport() {
        try {
            List<CourseMaster> lstCourses = courseService.getAllCourses();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Courses");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row header = sheet.createRow(0);
            String[] headers = {"Course ID", "Course Name", "Description", "Fees", "Start Date", "End Date", "Status"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (CourseMaster course : lstCourses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(course.getCourseId());
                row.createCell(1).setCellValue(course.getCourseName());
                row.createCell(2).setCellValue(course.getCourseDesc());
                row.createCell(3).setCellValue(course.getCourseFees());
                row.createCell(4).setCellValue(course.getCourseStartDate().toString());
                row.createCell(5).setCellValue(course.getCourseEndDate().toString());
                row.createCell(6).setCellValue(course.getStatus());
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            workbook.close();

            HttpHeaders headers1 = new HttpHeaders();
            headers1.setContentDisposition(ContentDisposition.builder("attachment").filename("Courses.xlsx").build());
            headers1.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity<>(baos.toByteArray(), headers1, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            // Redirect to error page or display an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    
    
    private void addTableHeader(PdfPTable table) {
        String[] headers = {"ID", "Course Name", "Course Description", "Course Fees", "Course Start Date", "Course End Date", "Status"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
    }

    private void addTableRow(PdfPTable table, CourseMaster course) {
        table.addCell(String.valueOf(course.getCourseId()));
        table.addCell(course.getCourseName());
        table.addCell(course.getCourseDesc());
        table.addCell(course.getCourseFees());
        table.addCell(course.getCourseStartDate().toString());
        table.addCell(course.getCourseEndDate().toString());
        table.addCell(course.getStatus());
    }

    private void createHeaderRow(Row headerRow) {
        String[] headers = {"ID", "Course Name", "Course Description", "Course Fees", "Course Start Date", "Course End Date", "Status"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private void createDataRow(Row row, CourseMaster course) {
        row.createCell(0).setCellValue(course.getCourseId());
        row.createCell(1).setCellValue(course.getCourseName());
        row.createCell(2).setCellValue(course.getCourseDesc());
        row.createCell(3).setCellValue(course.getCourseFees());
        row.createCell(4).setCellValue(course.getCourseStartDate().toString());
        row.createCell(5).setCellValue(course.getCourseEndDate().toString());
        row.createCell(6).setCellValue(course.getStatus());
    }
    
}