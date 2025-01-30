package Geoexplore.Content;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    // Ottiene tutti i contenuti
    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    // Trova un contenuto per ID
    public Optional<Content> getContentById(Long id) {
        return contentRepository.findById(id);
    }

    // Salva un nuovo contenuto
    public Content createContent(Content content) {
        return contentRepository.save(content);
    }

    // Aggiorna un contenuto esistente
    public Content updateContent(Long id, Content contentDetails) {
        return contentRepository.findById(id).map(content -> {
            content.setTipo(contentDetails.getTipo());
            content.setTitolo(contentDetails.getTitolo());
            content.setTesto(contentDetails.getTesto());
            content.setMediaPath(contentDetails.getMediaPath());
            content.setStop(contentDetails.getStop());
            content.setCreator(contentDetails.getCreator());
            content.setApproved(contentDetails.getApproved());
            return contentRepository.save(content);
        }).orElseThrow(() -> new RuntimeException("Content not found"));
    }

    // Elimina un contenuto
    public void deleteContent(Long id) {
        contentRepository.deleteById(id);
    }
}
