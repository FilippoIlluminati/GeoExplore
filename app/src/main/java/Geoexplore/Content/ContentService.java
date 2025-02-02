package Geoexplore.Content;

import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private UserRepository userRepository;

    // Recupera tutti i contenuti
    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    // Recupera i contenuti approvati (con approval e isApproved true)
    public List<Content> getApprovedContents() {
        return contentRepository.findAll().stream()
                .filter(c -> c.getApproval() != null && Boolean.TRUE.equals(c.getApproval().getIsApproved()))
                .toList();
    }

    // Recupera i contenuti in attesa di approvazione
    public List<Content> getPendingContents() {
        return contentRepository.findAll().stream()
                .filter(c -> c.getApproval() == null || !Boolean.TRUE.equals(c.getApproval().getIsApproved()))
                .toList();
    }

    // Recupera un contenuto per ID
    public Optional<Content> getContentById(Long id) {
        return contentRepository.findById(id);
    }

    // Crea un nuovo contenuto (sia generico che associato a un POI)
    public Content createContent(Content content) {
        Long creatorId = content.getCreator().getId();
        Users creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Utente creatore non trovato con id: " + creatorId));

        // Verifica che il creatore abbia il permesso di caricare contenuti
        if (!creator.getRuolo().getPermissions().contains("UPLOAD_CONTENT")) {
            throw new RuntimeException("L'utente non ha il permesso di caricare contenuti");
        }
        content.setCreator(creator);

        // Se contentType è nullo, lo imposta automaticamente in base alla presenza di un POI
        if (content.getContentType() == null) {
            content.setContentType(content.getPoi() != null ? ContentType.POI : ContentType.GENERIC);
        }
        content.setApproval(null);
        return contentRepository.save(content);
    }

    // Aggiorna un contenuto esistente
    public Content updateContent(Long id, Content contentDetails) {
        return contentRepository.findById(id).map(content -> {
            content.setTitolo(contentDetails.getTitolo());
            content.setDescrizione(contentDetails.getDescrizione());
            content.setPoi(contentDetails.getPoi());
            Long creatorId = contentDetails.getCreator().getId();
            Users creator = userRepository.findById(creatorId)
                    .orElseThrow(() -> new RuntimeException("Utente creatore non trovato con id: " + creatorId));
            content.setCreator(creator);
            content.setContentType((content.getPoi() != null) ? ContentType.POI : ContentType.GENERIC);
            return contentRepository.save(content);
        }).orElseThrow(() -> new RuntimeException("Contenuto non trovato"));
    }

    // Elimina un contenuto
    public void deleteContent(Long id) {
        contentRepository.deleteById(id);
    }

    // Approva un contenuto: l'utente approvatore deve avere i permessi VALIDATE_CONTENT o MANAGE_CONTENT
    public Content approveContent(Long contentId, Long approverId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Contenuto non trovato"));

        Users approver = userRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Utente approvatore non trovato"));

        if (!approver.getRuolo().getPermissions().contains("VALIDATE_CONTENT") &&
                !approver.getRuolo().getPermissions().contains("MANAGE_CONTENT")) {
            throw new RuntimeException("L'utente non ha il permesso di approvare contenuti");
        }

        Approval approval = content.getApproval();
        if (approval == null) {
            approval = new Approval(content, approver, true);
            content.setApproval(approval);
        } else {
            approval.setApprover(approver);
            approval.setIsApproved(true);
        }
        approvalRepository.save(approval);
        return contentRepository.save(content);
    }
}
