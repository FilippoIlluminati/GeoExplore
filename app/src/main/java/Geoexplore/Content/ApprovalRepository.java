package Geoexplore.Content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    // Trova l'approvazione relativa a un determinato contenuto
    Optional<Approval> findByContentId(Long contentId);
}
