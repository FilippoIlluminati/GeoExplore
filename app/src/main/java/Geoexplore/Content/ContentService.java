package Geoexplore.Content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    public Content createContent(Content content, Long poiId, Long creatorId) {
        // qui c’è già tutta la tua logica invariata per creare e approvare/in attesa
        // ...
        return contentRepository.save(content);
    }

    public List<Content> getContentsByPOI(Long poiId) {
        return contentRepository.findByPoiId(poiId);
    }

    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    public Optional<Content> getContentById(Long id) {
        return contentRepository.findById(id);
    }

    public void deleteContent(Long id) {
        contentRepository.deleteById(id);
    }
}
