package Geoexplore.POI;

import Geoexplore.User.UserRepository;
import Geoexplore.User.UserRole;
import Geoexplore.User.Users;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;



@Service
public class POIService {

    @Autowired
    private POIRepository poiRepository;

    @Autowired
    private UserRepository userRepository;

    public List<POI> getAllPOIs() {
        return poiRepository.findAll();
    }

    public Optional<POI> getPOIById(Long id) {
        return poiRepository.findById(id);
    }

    public POI addPOI(POI poi, Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        poi.setOwner(user);
        if (user.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO) {
            poi.setApproved(true);
        } else {
            poi.setApproved(false);
        }
        return poiRepository.save(poi);
    }

    public POI updatePOI(Long id, POI updatedPOI, Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return poiRepository.findById(id).map(poi -> {
            if (poi.getOwner().equals(user) || user.getRuolo() == UserRole.CURATORE || user.getRuolo() == UserRole.GESTORE_PIATTAFORMA) {
                poi.setName(updatedPOI.getName());
                poi.setDescription(updatedPOI.getDescription());
                poi.setLatitude(updatedPOI.getLatitude());
                poi.setLongitude(updatedPOI.getLongitude());
                poi.setCategory(updatedPOI.getCategory());
                return poiRepository.save(poi);
            } else {
                throw new SecurityException("Permessi insufficienti per modificare questo POI");
            }
        }).orElseThrow(() -> new RuntimeException("POI not found"));
    }

    public void deletePOI(Long id, Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        poiRepository.findById(id).ifPresentOrElse(poi -> {
            if (poi.getOwner().equals(user) || user.getRuolo() == UserRole.CURATORE || user.getRuolo() == UserRole.GESTORE_PIATTAFORMA) {
                poiRepository.deleteById(id);
            } else {
                throw new SecurityException("Permessi insufficienti per eliminare questo POI");
            }
        }, () -> {
            throw new RuntimeException("POI not found");
        });
    }

    public List<POI> getUnapprovedPOIs() {
        return poiRepository.findByApproved(false);
    }

    public POI approvePOI(Long id, Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRuolo() == UserRole.CURATORE || user.getRuolo() == UserRole.GESTORE_PIATTAFORMA) {
            return poiRepository.findById(id).map(poi -> {
                poi.setApproved(true);
                return poiRepository.save(poi);
            }).orElseThrow(() -> new RuntimeException("POI not found"));
        } else {
            throw new SecurityException("Permessi insufficienti per approvare POI");
        }
    }
}
