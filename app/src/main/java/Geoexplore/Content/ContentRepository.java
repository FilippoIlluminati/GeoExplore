package Geoexplore.Content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    // Recupera i contenuti associati a un determinato POI per id
    List<Content> findByPoi_Id(Long poiId);

    // Recupera i contenuti associati a un POI per nome (se necessario)
    List<Content> findByPoi_Nome(String nome);

    // Recupera i contenuti in base al ContentType
    List<Content> findByContentType(ContentType contentType);
}
