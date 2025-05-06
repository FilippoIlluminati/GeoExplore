package Geoexplore.POI;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interfaccia repository per i POI
@Repository
public interface POIRepository extends JpaRepository<POI, Long> {

    // Trova un POI per nome
    POI findByNome(String nome);
}
