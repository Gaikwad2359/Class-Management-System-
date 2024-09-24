package CMS.Service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import CMS.Master.BatchMaster;
import CMS.Master.Notes;

public interface NotesService {
    Notes saveNotes(String title, MultipartFile file);
    List<Notes> getAllNotes();
    void deleteNoteById(Long id);
    List<BatchMaster> getAllBatches();
    void saveNotes(Notes notes, MultipartFile file);
    
    //Student Notes
    List<Notes> getNotesByBatches(List<BatchMaster> batchMasters);
}
	