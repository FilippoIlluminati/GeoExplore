package Geoexplore.POI;

import Geoexplore.User.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface POIRepository extends JpaRepository<POI, Long> {
    List<POI> findByApproved(boolean approved);
    List<POI> findByOwner(Users owner);
}

