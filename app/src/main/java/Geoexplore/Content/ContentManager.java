package Geoexplore.Content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentManager {

    private final ContentRepository contentRepository;

    @Autowired
    public ContentManager(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public Content saveContent(Content content) {
        return contentRepository.save(content);
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
