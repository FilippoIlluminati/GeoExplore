package Geoexplore.Journey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {
    // Aggiunto il metodo per cercare un Journey in base al nome
    Journey findByNome(String nome);
}
