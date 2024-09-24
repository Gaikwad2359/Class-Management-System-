package CMS.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import org.springframework.web.multipart.MultipartFile;

import CMS.Master.BatchMaster;
import CMS.Master.Notes;
import CMS.Service.NotesService;

@Controller
public class NotesController {

    private final Path rootLocation = Paths.get("pdf-uploads");

    @Autowired
    private NotesService notesService;

    @GetMapping("/notes")
    public String listNotes(Model model) {
        List<Notes> notesList = notesService.getAllNotes();
        model.addAttribute("notes", notesList);
        return "Notes/notes";
    }

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("notes", new Notes()); // Add Notes object to the model
        List<BatchMaster> listBatches = notesService.getAllBatches();
        model.addAttribute("listBatches", listBatches);
        
        return "Notes/upload"; // Returns the upload.html template
    }

    @PostMapping("/upload")
    public String uploadNotes(@ModelAttribute Notes notes, 
                              @RequestParam("file") MultipartFile file) {
        // Call the service to save the note and file
        notesService.saveNotes(notes.getTitle(), file);
        
        return "redirect:/notes";
    }

   
    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> viewPdf(@PathVariable Long id) {
        Optional<Notes> notesOptional = notesService.getAllNotes().stream()
            .filter(note -> note.getId().equals(id))
            .findFirst();

        if (notesOptional.isPresent()) {
            Notes notes = notesOptional.get();
            Path filePath = Paths.get(notes.getPdfFilePath());

            if (Files.exists(filePath)) {
                try {
                    Resource resource = new UrlResource(filePath.toUri());
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName() + "\"")
                            .body(resource);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    
    
    @PostMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id) {
        notesService.deleteNoteById(id);
        return "redirect:/notes";
    }
}
