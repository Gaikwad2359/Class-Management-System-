package CMS.ServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import CMS.DAO.BatchDAO;
import CMS.DAO.NotesRepository;
import CMS.DAO.StudentBatchDAO;
import CMS.Master.BatchMaster;
import CMS.Master.Notes;
import CMS.Service.NotesService;

@Service
public class NotesServiceImpl implements NotesService {

    private final Path rootLocation = Paths.get("pdf-uploads");

    @Autowired
    private NotesRepository notesRepository;
    
    @Autowired
    StudentBatchDAO studentBatchDAO;
    
    @Autowired
    BatchDAO batchDAO;
    

    public NotesServiceImpl() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!", e);
        }
    }

    @Override
    public Notes saveNotes(String title, MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                Path destinationFile = rootLocation.resolve(fileName);
                Files.copy(file.getInputStream(), destinationFile);
                Notes notes = new Notes(title, destinationFile.toString());
                return notesRepository.save(notes);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file!", e);
        }
        return null;
    }

    @Override
    public List<Notes> getAllNotes() {
        return notesRepository.findAll();
    }
    
    
    @Override
    public void deleteNoteById(Long id) {
        Optional<Notes> noteOptional = notesRepository.findById(id);

        if (noteOptional.isPresent()) {
            Notes note = noteOptional.get();
            // Delete the file from the file system
            Path filePath = Paths.get(note.getPdfFilePath());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file", e);
            }

            // Delete the note from the database
            notesRepository.deleteById(id);
        } else {
            throw new RuntimeException("Note not found with id: " + id);
        }
    }
    
    @Override
    public List<BatchMaster> getAllBatches() {
        // Fetch all BatchMaster records
        return batchDAO.findAll();
    }
    
    @Override
    public void saveNotes(Notes notes, MultipartFile file) {
        // Logic to save the file (e.g., saving it to the file system or database)
        
        // Save the Notes entity to the database
        notesRepository.save(notes);
    }
    
    //Student Notes 
    @Override
    public List<Notes> getNotesByBatches(List<BatchMaster> batchMasters) {
        return notesRepository.findByBatchMasterIn(batchMasters);
    }
}

