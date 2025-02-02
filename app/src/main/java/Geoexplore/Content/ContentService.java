package Geoexplore.Content;

import Geoexplore.User.Users;
import Geoexplore.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    // Ottiene tutti i contenuti
    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    // Ottiene solo i contenuti approvati
    public List<Content> getApprovedContents() {
        return contentRepository.findByApprovalIsNotNull();
    }

    // Ottiene solo i contenuti in attesa di approvazione
    public List<Content> getPendingContents() {
        return contentRepository.findByApprovalIsNull();
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
            return contentRepository.save(content);
        }).orElseThrow(() -> new RuntimeException("Content not found"));
    }

    // Elimina un contenuto
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
