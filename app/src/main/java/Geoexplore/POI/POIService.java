package Geoexplore.POI;

import Geoexplore.User.UserRepository;
import Geoexplore.User.UserRole;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class POIService {

    @Autowired
    private POIRepository poiRepository;

    // Crea un nuovo POI, impostando lo stato di approvazione in base al ruolo del creatore
    @Autowired
    private UserRepository userRepository;

    public POI createPOI(POI poi) {
        if (poi.getCreator() != null && poi.getCreator().getId() != null) {
            // Carica l'utente completo dal DB
            Optional<Users> creatorOpt = userRepository.findById(poi.getCreator().getId());
            if (creatorOpt.isPresent()) {
                Users creator = creatorOpt.get();
                poi.setCreator(creator);
                // Controlla il ruolo per impostare l'approvazione
                if (creator.getRuolo() == UserRole.CONTRIBUTOR_AUTORIZZATO ||
                        creator.getRuolo() == UserRole.CURATORE ||
                        creator.getRuolo() == UserRole.GESTORE_PIATTAFORMA) {
                    poi.setApprovato(true);
                } else {
                    poi.setApprovato(false);
                }
            } else {
                poi.setApprovato(false);
            }
        } else {
            poi.setApprovato(false);
        }
        return poiRepository.save(poi);
    }


    // Aggiorna un POI esistente (lo stato di approvazione non viene modificato automaticamente)
    public POI updatePOI(Long id, POI updatedPOI) {
        Optional<POI> optionalPOI = poiRepository.findById(id);
        if (optionalPOI.isPresent()) {
            POI existingPOI = optionalPOI.get();
            existingPOI.setNome(updatedPOI.getNome());
            existingPOI.setDescrizione(updatedPOI.getDescrizione());
            existingPOI.setLatitude(updatedPOI.getLatitude());
            existingPOI.setLongitude(updatedPOI.getLongitude());
            existingPOI.setCategoria(updatedPOI.getCategoria());
            existingPOI.setComune(updatedPOI.getComune());
            existingPOI.setCreator(updatedPOI.getCreator());
            return poiRepository.save(existingPOI);
        } else {
            throw new RuntimeException("POI non trovato con id " + id);
        }
    }

    // Elimina un POI
    public void deletePOI(Long id) {
        poiRepository.deleteById(id);
    }

    // Recupera un POI per ID
    public Optional<POI> getPOIById(Long id) {
        return poiRepository.findById(id);
    }

    // Recupera tutti i POI
    public List<POI> getAllPOIs() {
        return poiRepository.findAll();
    }

    // Approva un POI (da chiamare da un endpoint riservato a ruoli autorizzati, ad esempio Curatore)
    public POI approvePOI(Long id) {
        Optional<POI> optionalPOI = poiRepository.findById(id);
        if(optionalPOI.isPresent()) {
            POI poi = optionalPOI.get();
            poi.setApprovato(true);
            return poiRepository.save(poi);
        } else {
            throw new RuntimeException("POI non trovato con id " + id);
        }
    }
}

