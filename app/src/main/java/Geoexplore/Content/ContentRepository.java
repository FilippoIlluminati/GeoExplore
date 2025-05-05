package Geoexplore.Content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByPoiIdAndStatus(Long poiId, ContentStatus status);
    List<Content> findByStatus(ContentStatus status);
}
