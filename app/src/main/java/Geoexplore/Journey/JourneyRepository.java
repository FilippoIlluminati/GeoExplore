package Geoexplore.Journey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {

    // Restituisce un journey dato il nome esatto
    Journey findByNome(String nome);
}
