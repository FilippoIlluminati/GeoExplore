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

    // Recupera i contenuti approvati (cioè che hanno un'Approval con isApproved true)
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

    // Crea un nuovo contenuto (inizialmente senza approvazione)
    public Content createContent(Content content) {
        content.setApproval(null);
        return contentRepository.save(content);
    }

    // Aggiorna un contenuto esistente
    public Content updateContent(Long id, Content contentDetails) {
        return contentRepository.findById(id).map(content -> {
            content.setTitolo(contentDetails.getTitolo());
            content.setDescrizione(contentDetails.getDescrizione());
            content.setPoi(contentDetails.getPoi());
            content.setCreator(contentDetails.getCreator());
            content.setContentType((content.getPoi() != null) ? ContentType.POI : ContentType.GENERIC);
            return contentRepository.save(content);
        }).orElseThrow(() -> new RuntimeException("Content not found"));
    }

    // Elimina un contenuto
    public void deleteContent(Long id) {
        contentRepository.deleteById(id);
    }

    // Approva un contenuto: l'utente approvatore deve avere i permessi (VALIDATE_CONTENT o MANAGE_CONTENT)
    public Content approveContent(Long contentId, Long approverId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content not found"));

        Users approver = userRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        // Verifica se l'utente ha il permesso di validare i contenuti
        if (!approver.getRuolo().getPermissions().contains("VALIDATE_CONTENT")
                && !approver.getRuolo().getPermissions().contains("MANAGE_CONTENT")) {
            throw new RuntimeException("User does not have permission to approve content");
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
