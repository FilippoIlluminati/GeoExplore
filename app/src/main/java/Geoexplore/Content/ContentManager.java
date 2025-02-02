package Geoexplore.Content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class ContentManager {

    @Autowired
    private ContentService contentService;

    public Content createContent(Content content) {
        return contentService.createContent(content);
    }

    public Content updateContent(Long id, Content content) {
        return contentService.updateContent(id, content);
    }

    public void deleteContent(Long id) {
        contentService.deleteContent(id);
    }

    public Optional<Content> getContentById(Long id) {
        return contentService.getContentById(id);
    }

    public List<Content> getAllContents() {
        return contentService.getAllContents();
    }

    public Content approveContent(Long contentId, Long approverId) {
        return contentService.approveContent(contentId, approverId);
    }
}
