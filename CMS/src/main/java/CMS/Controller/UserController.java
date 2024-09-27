package CMS.Controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import CMS.DAO.UserRepo;
import CMS.Master.ApplyForm;
import CMS.Master.BatchMaster;
import CMS.Master.Contact;
import CMS.Master.CourseMaster;
import CMS.Master.DailyUpdate;
import CMS.Master.LaptopAllocationMaster;
import CMS.Master.LaptopApply;
import CMS.Master.Notes;
import CMS.Master.StudentBatch;
import CMS.Master.StudentInstallmentMaster;
import CMS.Master.StudentMaster;
import CMS.Master.StudentReference;
import CMS.Service.ApplyFormService;
import CMS.Service.ContactService;
import CMS.Service.CourseService;
import CMS.Service.DailyUpdateService;
import CMS.Service.LaptopAllocationService;
import CMS.Service.LaptopApplyService;
import CMS.Service.NotesService;
import CMS.Service.NotificationService;
import CMS.Service.StudentBatchService;
import CMS.Service.StudentReferenceService;
import CMS.Service.StudentService;
import CMS.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	StudentService studentService;
	
	@Autowired
	UserService userService;
	
	@Autowired
    private StudentBatchService studentBatchService;
	
	@Autowired
	LaptopAllocationService allocationService;
	
	@Autowired
	StudentReferenceService studentReferenceService;
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	DailyUpdateService dailyUpdateService;
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	NotesService notesService;
	
	@Autowired
    private ContactService contactService;

    @Autowired
    private JavaMailSender mailSender;

    private final String adminEmail = "gaikwadpravin667@gmail.com";
	
	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			StudentMaster student = userRepo.findByEmail(email);
			if (student != null) {
				m.addAttribute("user", student);
			} else {
				m.addAttribute("error", "User not found");
			}
		}
	}

	@GetMapping("/user-dashboard")
	public String dashboard() {
		return "/user/user-dashboard"; 
	}
	
	@GetMapping("/profile")
	public String profile() {
		return "/user/profile"; 
	}
	

	@GetMapping("/StudentReports")
    public String showReportDashboard(Model model) {
        return "/user/StudentReportDashboard"; // This corresponds to the ReportDashboard.html template
    }
	
	//Student Pendnig Balance 
	@GetMapping("/reportLogin/studentBalance")
	public ModelAndView generateStudentBalanceReport() {
	    ModelAndView modelAndView = new ModelAndView("user/studentPendingBalanceReport");

	    try {
	        StudentMaster currentStudent = getCurrentStudent();

	        if (currentStudent != null && currentStudent.getStudentbalance() > 0) {
	            modelAndView.addObject("student", currentStudent);
	            modelAndView.addObject("totalPendingBalance", currentStudent.getStudentbalance());
	        } else {
	            modelAndView.addObject("message", "You have no pending balance.");
	        }
	    } catch (Exception e) {
	        modelAndView.setViewName("report/error");
	        modelAndView.addObject("message", "An unexpected error occurred: " + e.getMessage());
	    }

	    return modelAndView;
	}

	private StudentMaster getCurrentStudentMaster() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String email = userDetails.getUsername(); 
	        return studentService.getStudentByEmail(email); 
	    }
	    return null; 
	}

	//Installments
	 @GetMapping("/student/installments")
	    public String viewStudentInstallments(Model model) {
	        StudentMaster currentStudent = getCurrentStudent(); // Retrieve the logged-in student
	        List<StudentInstallmentMaster> installments = userService.getInstallmentsByStudent(currentStudent);
	        
	        model.addAttribute("installments", installments);
	        return "user/student-installments"; 
	    }

	    private StudentMaster getCurrentStudent() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String email = userDetails.getUsername(); 
	        return userService.findStudentByEmail(email);
	    }
	    
	    
	    
	    //Student Installment Pdf Download 
	    @GetMapping("/student/installment/download/{instId}")
	    public ResponseEntity<byte[]> downloadInstallmentPDF(@PathVariable("instId") Integer instId) throws IOException, DocumentException {
	        // Fetch the current student and installments
	        StudentMaster currentStudent = getCurrentStudent();
	        List<StudentInstallmentMaster> installments = userService.getInstallmentsByStudent(currentStudent);

	        // Find the selected installment
	        StudentInstallmentMaster selectedInstallment = installments.stream()
	            .filter(installment -> installment.getInstId().equals(instId))
	            .findFirst()
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Installment not found for id: " + instId));

	        // Get current pending fees
	        double studentBalance = currentStudent.getStudentbalance();

	        String downloadDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

	        // Prepare PDF document
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        Document document = new Document();
	        PdfWriter.getInstance(document, out);
	        document.open();

	        // Add the header image
	        ClassPathResource headerResource = new ClassPathResource("static/images/InstallmentHeader.png");
	        Image headerImage = Image.getInstance(headerResource.getURL());
	        headerImage.scaleToFit(document.getPageSize().getWidth(), 100);
	        headerImage.setAbsolutePosition(0, document.getPageSize().getHeight() - 100);
	        document.add(headerImage);

	        // Add margin after the header
	        document.add(new Paragraph("\n\n"));

	        // Define fonts
	        Font headerFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
	        Font subHeaderFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
	        Font bodyFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

	        // Add title
	        Paragraph title = new Paragraph("Installment Receipt", headerFont);
	        title.setAlignment(Element.ALIGN_CENTER);
	        title.setSpacingBefore(20);
	        document.add(title);

	        // Add student name and receipt download date after the title
	        Paragraph studentInfo = new Paragraph("Student Name: " + currentStudent.getName(), bodyFont);
	        studentInfo.setAlignment(Element.ALIGN_CENTER);
	        studentInfo.setSpacingBefore(10);
	        studentInfo.setSpacingAfter(5);
	        document.add(studentInfo);

	        Paragraph downloadDateInfo = new Paragraph("Receipt Download Date: " + downloadDate, bodyFont);
	        downloadDateInfo.setAlignment(Element.ALIGN_CENTER);
	        downloadDateInfo.setSpacingAfter(20);
	        document.add(downloadDateInfo);

	        // Add a line separator
	        LineSeparator separator = new LineSeparator();
	        separator.setPercentage(80);
	        separator.setLineWidth(1f);
	        separator.setAlignment(Element.ALIGN_CENTER);
	        document.add(new Chunk(separator));

	        // Add some vertical space to visually center the content (optional, adjust as needed)
	        document.add(new Paragraph("\n\n"));

	        // Create table with installment details
	        PdfPTable table = new PdfPTable(2); // 2 columns
	        table.setWidthPercentage(100); // Width of the table
	        table.setSpacingBefore(20);
	        table.setWidths(new float[]{1f, 2f}); // Adjust column widths

	        // Add table headers with background color
	        PdfPCell header1 = new PdfPCell(new Phrase("Detail", subHeaderFont));
	        header1.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header1.setPadding(10);
	        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(header1);

	        PdfPCell header2 = new PdfPCell(new Phrase("Description", subHeaderFont));
	        header2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        header2.setPadding(10);
	        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(header2);

	        // Add installment details to the table with better padding
	        addTableCell(table, "Installment Amount", String.format("%.2f", selectedInstallment.getInstAmount()), bodyFont);
	        addTableCell(table, "Installment Date", selectedInstallment.getInstDate().toString(), bodyFont);
	        addTableCell(table, "Payment Type", selectedInstallment.getInstPaymentType(), bodyFont);

	        // Payment details based on payment type
	        if ("online".equalsIgnoreCase(selectedInstallment.getInstPaymentType())) {
	            addTableCell(table, "Cheque Number", "N/A", bodyFont);
	            addTableCell(table, "Cheque Date", "N/A", bodyFont);
	            addTableCell(table, "Cheque Bank", "N/A", bodyFont);
	        } else if ("cheque".equalsIgnoreCase(selectedInstallment.getInstPaymentType())) {
	            addTableCell(table, "Cheque Number", selectedInstallment.getInstPayChequeNo(), bodyFont);
	            addTableCell(table, "Cheque Date", selectedInstallment.getInstPayChequeDate().toString(), bodyFont);
	            addTableCell(table, "Cheque Bank", selectedInstallment.getInstPayChequeBank(), bodyFont);
	        } else if ("cash".equalsIgnoreCase(selectedInstallment.getInstPaymentType())) {
	            addTableCell(table, "Cheque Number", "N/A", bodyFont);
	            addTableCell(table, "Cheque Date", "N/A", bodyFont);
	            addTableCell(table, "Cheque Bank", "N/A", bodyFont);
	        }

	        // Add student balance to the table
	        addTableCell(table, "Pending Balance", String.format("%.2f", studentBalance), bodyFont);

	        // Add table to document
	        document.add(table);

	        // Add footer image
	        ClassPathResource footerResource = new ClassPathResource("static/images/InstallmentFooter.png");
	        Image footerImage = Image.getInstance(footerResource.getURL());
	        footerImage.scaleToFit(document.getPageSize().getWidth(), 100);
	        footerImage.setAbsolutePosition(0, 0);
	        document.add(footerImage);

	        // Add footer text
	        Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
	        Paragraph footer = new Paragraph("* This is a computer-generated receipt, no signature is required", footerFont);
	        footer.setAlignment(Element.ALIGN_CENTER);
	        footer.setSpacingBefore(20);
	        document.add(footer);

	        document.close();

	        // Set response headers and return the PDF
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_PDF);
	        headers.setContentDispositionFormData("inline", "installment_receipt_" + instId + ".pdf");

	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(out.toByteArray());
	    }

	    // Utility function to add cells with padding to the table
	    private void addTableCell(PdfPTable table, String detail, String description, Font font) {
	        PdfPCell cell1 = new PdfPCell(new Phrase(detail, font));
	        cell1.setPadding(10); // Add padding for better spacing
	        table.addCell(cell1);

	        PdfPCell cell2 = new PdfPCell(new Phrase(description, font));
	        cell2.setPadding(10);
	        table.addCell(cell2);
	    }






	    
	    
	    //Student Batch
	    @GetMapping("/student/batches")
	    public String viewStudentBatches(Authentication authentication, Model model) {
	        String email = authentication.getName();
	        
	        StudentMaster studentMaster = studentService.getStudentByEmail(email);
	        
	        List<StudentBatch> studentBatches = studentBatchService.getStudentBatchByStudent(studentMaster);
	        
	        model.addAttribute("studentBatches", studentBatches);
	        
	        return "user/student_batches"; 
	    }
	    
	    
	    
	    //Student Laptop
	    public void StudentLaptopController(StudentService studentService, LaptopAllocationService laptopAllocationService) {
	        this.studentService = studentService;
	        this.allocationService = laptopAllocationService;
	    }

	    @GetMapping("/student/laptops")
	    public String viewStudentLaptops(Authentication authentication, Model model) {
	        String email = authentication.getName();

	        StudentMaster studentMaster = studentService.getStudentByEmail(email);

	        List<LaptopAllocationMaster> laptopAllocations = allocationService.getLaptopAllocationsByStudent(studentMaster);

	        model.addAttribute("laptopAllocations", laptopAllocations);

	        return "user/student_laptops";
	    }
	    
	    
	    
	    //Apply Courses 
	    @Autowired
	    private ApplyFormService applyFormService;

	    @GetMapping("/applyCourse")
	    public String showApplyForm(Model model) {
	        model.addAttribute("applyForm", new ApplyForm());
	        return "StudentCourses/applyCourseForm"; // Return the apply form view
	    }

	    @PostMapping("/submitApplication")
	    public String submitApplication(@ModelAttribute("applyForm") ApplyForm applyForm) {
	        applyFormService.saveApplyForm(applyForm);
	        return "redirect:/user/success"; // Redirect to the success page after submission
	    }

	    @GetMapping("/success")
	    public String showSuccessPage(Model model) {
	        model.addAttribute("message", "Your application has been submitted successfully!");
	        return "StudentCourses/success"; // Returns the success view name
	    }
	    
	    
	    //Laptop Apply 
	    
	    @Autowired
	    private LaptopApplyService laptopApplyService;

	    @GetMapping("/laptopApply")
	    public String showLaptopApplyForm(Model model) {
	        model.addAttribute("applyForm", new LaptopApply());
	        return "user/laptop-apply-form";  
	    }

	    @PostMapping("/laptopApplyForm")
	    public String submitLaptopApplyForm(@Valid @ModelAttribute("applyForm") LaptopApply applyForm, BindingResult result, Model model) {
	        if (result.hasErrors()) {
	            return "user/laptop-apply-form";  
	        }
	        laptopApplyService.saveLaptopApplyForm(applyForm);
	        return "redirect:/user/apply-success";
	    }

	    @GetMapping("/apply-success")
	    public String showLaptopSuccessPage() {
	        return "user/apply-success";  
	    }
	    
	    
	    
	    //Student Reference 
	    @GetMapping("/student/references")
	    public String showStudentReferences(Model model) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName(); 

	        StudentMaster loggedInStudent = studentService.getStudentByEmail(username);
	        
	        if (loggedInStudent != null) {
	            List<StudentReference> studentReferences = studentReferenceService.getStudentReferencesByStudentId(loggedInStudent.getStudentId());

	            if (studentReferences.isEmpty()) {
	                model.addAttribute("message", "No references found for this student.");
	            } else {
	                model.addAttribute("studentReferences", studentReferences);
	            }
	        } else {
	        	model.addAttribute("message", "No reference found the students. You have not been referenced by any other student.");

	        }

	        return "user/studentReferences"; 
	    } 
	    
	    
	    
	    //Student Updates
	 // For students to view daily updates related to their batches
	    @GetMapping("/student/updates")
	    public String viewStudentUpdates(Authentication authentication, Model model) {
	        String email = authentication.getName(); // Get the email of the logged-in student
	        
	        StudentMaster studentMaster = studentService.getStudentByEmail(email); // Fetch student data based on email
	        
	        List<StudentBatch> studentBatches = studentBatchService.getStudentBatchByStudent(studentMaster); // Get the batches the student is enrolled in
	        
	        // Extract batchMaster objects from studentBatches
	        List<BatchMaster> batchMasters = studentBatches.stream()
	            .map(StudentBatch::getBatchMaster)
	            .collect(Collectors.toList());
	        
	        // Fetch updates for the batches
	        List<DailyUpdate> updates = dailyUpdateService.getUpdatesForBatches(batchMasters);
	        
	        model.addAttribute("updates", updates); // Add updates to the model
	        
	        return "user/student_updates"; // Return the view for displaying updates
	    }
	    
	    
	    //Student Notifications 
	    @GetMapping("/Student/notifications")
	    public String getNotifications(Model model) {
	        List<BatchMaster> upcomingBatches = notificationService.getUpcomingBatches();
	        List<CourseMaster> upcomingCourses = notificationService.getUpcomingCourses();

	        model.addAttribute("upcomingBatches", upcomingBatches);
	        model.addAttribute("upcomingCourses", upcomingCourses);

	        return "user/notifications";
	    }

	    
	    //Student Notes
	    @GetMapping("/student/Notes")
	    public String getStudentNotes(Authentication authentication, Model model) {
	        String email = authentication.getName();
	        
	        StudentMaster studentMaster = studentService.getStudentByEmail(email);
	        
	        // Get student batches
	        List<StudentBatch> studentBatches = studentBatchService.getStudentBatchByStudent(studentMaster);
	        List<BatchMaster> batchMasters = studentBatches.stream()
	            .map(StudentBatch::getBatchMaster)
	            .collect(Collectors.toList());

	        // Get notes for these batches
	        List<Notes> notes = notesService.getNotesByBatches(batchMasters);

	        // Add notes to the model and return the view
	        model.addAttribute("notes", notes);
	        return "user/student_notes";
	    }

	    
	    
	    //Home Page 
	    @GetMapping("/student/home")
	    public String showHomePage(HttpSession session, Model model) {
	        if (session.getAttribute("user") != null) {
	            // User is logged in, show all pages
	            model.addAttribute("showAllPages", true);
	        } else {
	            // User is not logged in, restrict access
	            model.addAttribute("showAllPages", false);
	        }
	        return "/StudentHome/home";  // return your home page
	    }
	    
	    //About Us Page 
	    @GetMapping("/student/about")
	    public String aboutUs() {
	        return "StudentAboutUs/aboutUs";
	    }

	    
	    //ContactUs Page
	    @GetMapping("/student/contact")
	    public String showContactForm(Model model) {
	        model.addAttribute("contact", new Contact());
	        return "StudentContactUs/ContactUs";
	    }

	    @PostMapping("/student/submitContact")
	    public String submitContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result, Model model) {
	        if (result.hasErrors()) {
	            return "StudentContactUs/ContactUs";
	        }

	        try {
	            contactService.saveContact(contact);
	            sendContactEmail(contact);
	        } catch (Exception e) {
	            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
	            return "Error";
	        }

	        return "redirect:/user/student/contactSuccess";
	    }
	    @GetMapping("/student/contactSuccess")
	    public String contactSuccess() {
	        return "StudentContactUs/contactSuccess";
	    }

	    private void sendContactEmail(Contact contact) {
	        // Email to admin
	        SimpleMailMessage adminMessage = new SimpleMailMessage();
	        adminMessage.setTo(adminEmail);
	        adminMessage.setSubject("Zplus STS Contact Form Submission");
	        adminMessage.setText("A new Zplus contact has been submitted with the following details:\n\n" +
	                             "Name: " + contact.getName() + "\n" +
	                             "Email: " + contact.getEmail() + "\n" +
	                             "Phone: " + contact.getPhone() + "\n" +
	                             "Message: " + contact.getMessage());

	        mailSender.send(adminMessage);

	        // Email to applicant
	        SimpleMailMessage applicantMessage = new SimpleMailMessage();
	        applicantMessage.setTo(contact.getEmail());
	        applicantMessage.setSubject("Thank You for Contacting Zplus STS");
	        applicantMessage.setText("Dear " + contact.getName() + ",\n\n" +
	                                 "Thank you for getting in touch with us. We have received your message and will respond to you as soon as possible.\n\n" +
	                                 "Here is a copy of your message:\n\n" +
	                                 "Name: " + contact.getName() + "\n" +
	                                 "Email: " + contact.getEmail() + "\n" +
	                                 "Phone: " + contact.getPhone() + "\n" +
	                                 "Message: " + contact.getMessage() + "\n\n" +
	                                 "Best regards,\n" +
	                                 "Zplus STS Team");

	        mailSender.send(applicantMessage);
	    }


	    
	    //Student Services Page 
	    @GetMapping("/student/services/software-development")
	    public String showSoftwareDevelopment() {
	        return "StudentServices/software-development";
	    }

	    @GetMapping("/student/services/hardware-sales")
	    public String showHardwareSales() {
	        return "StudentServices/hardware-sales";
	    }

	    @GetMapping("/student/services/training-certifications")
	    public String showTrainingCertifications() {
	        return "StudentServices/training-certifications";
	    }

	    @GetMapping("/student/services/student-augmentation")
	    public String showStudentAugmentation() {
	        return "StudentServices/student-augmentation";
	    }

	    @GetMapping("/student/services/project-guidance")
	    public String showProjectGuidance() {
	        return "StudentServices/project-guidance";
	    }
	    
	    
	    //Courses Code 
	    @GetMapping("/student/courses/FullStackDevelopment")
	    public String fullStackDevelopment(Model model) {
	        model.addAttribute("courseName", "Java Full Stack Development");
	        // Add more course-specific attributes if needed
	        return "StudentCourses/FullStackDevelopment";
	    }

	    @GetMapping("/student/courses/DataStructure")
	    public String dataStructure(Model model) {
	        model.addAttribute("courseName", "Data Structure and Algorithms");
	        return "StudentCourses/DataStructure";
	    }

	    @GetMapping("/student/courses/Angular")
	    public String angular(Model model) {
	        model.addAttribute("courseName", "Angular Development");
	        return "StudentCourses/Angular";
	    }

	    @GetMapping("/student/courses/CProgramming")
	    public String cProgramming(Model model) {
	        model.addAttribute("courseName", "C Programming");
	        return "StudentCourses/CProgramming";
	    }

	    @GetMapping("/student/courses/coreJAVA")
	    public String coreJava(Model model) {
	        model.addAttribute("courseName", "Core Java");
	        return "StudentCourses/CoreJava";
	    }

	    @GetMapping("/student/courses/AdvancedJava")
	    public String advancedJava(Model model) {
	        model.addAttribute("courseName", "Advanced Java");
	        return "StudentCourses/AdvancedJava";
	    }
	    
	    
}
