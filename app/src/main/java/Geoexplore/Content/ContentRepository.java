package Geoexplore.Content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    // Recupera tutti i contenuti approvati
    List<Content> findByApprovalIsNotNull();

    // Recupera tutti i contenuti NON approvati (quelli senza approvazione)
    List<Content> findByApprovalIsNull();
}
