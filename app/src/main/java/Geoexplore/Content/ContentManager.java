package Geoexplore.Content;

import Geoexplore.User.Users;
import Geoexplore.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentManager {

    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final ApprovalRepository approvalRepository; // Aggiunto per gestire l'approvazione

    @Autowired
    public ContentManager(ContentRepository contentRepository, UserRepository userRepository, ApprovalRepository approvalRepository) {
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
        this.approvalRepository = approvalRepository;
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

    // **NUOVO METODO: Approva un contenuto**
    public Content approveContent(Long contentId, Long approverId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content not found"));

        Users approver = userRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        // Verifica se l'utente ha il permesso di approvare contenuti
        if (!approver.getRuolo().equals("CURATORE") && !approver.getRuolo().equals("GESTORE_PIATTAFORMA")) {
            throw new RuntimeException("User does not have permission to approve content");
        }

        // Creiamo l'approvazione e la salviamo nel database
        Approval approval = new Approval(content, approver, true);
        approvalRepository.save(approval);

        // Aggiorniamo il contenuto con l'approvazione
        content.setApproval(approval);
        return contentRepository.save(content);
    }
}
