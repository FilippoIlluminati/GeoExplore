package Geoexplore.Content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    // Recupera contenuti associati a un POI e con uno specifico stato
    List<Content> findByPoiIdAndStatus(Long poiId, ContentStatus status);

    // Recupera tutti i contenuti con uno specifico stato
    List<Content> findByStatus(ContentStatus status);
}
