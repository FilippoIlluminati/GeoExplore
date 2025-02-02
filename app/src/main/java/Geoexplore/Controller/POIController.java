package Geoexplore.Controller;

import Geoexplore.POI.POI;
import Geoexplore.POI.POIService;
import Geoexplore.User.Users;
import Geoexplore.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/poi")
public class POIController {

    private final POIService poiService;
    private final UserRepository userRepository;

    @Autowired
    public POIController(POIService poiService, UserRepository userRepository) {
        this.poiService = poiService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<POI> getAllPOIs() {
        return poiService.getAllPOIs();
    }

    @GetMapping("/{id}")
    public Optional<POI> getPOIById(@PathVariable Long id) {
        return poiService.getPOIById(id);
    }

    @PostMapping
    public POI addPOI(@RequestBody POI poi, Principal principal) {
        Long userId = getUserFromPrincipal(principal).getId();
        return poiService.addPOI(poi, userId);
    }

    @PutMapping("/{id}")
    public POI updatePOI(@PathVariable Long id, @RequestBody POI poi, Principal principal) {
        Long userId = getUserFromPrincipal(principal).getId();
        return poiService.updatePOI(id, poi, userId);
    }

    @DeleteMapping("/{id}")
    public void deletePOI(@PathVariable Long id, Principal principal) {
        Long userId = getUserFromPrincipal(principal).getId();
        poiService.deletePOI(id, userId);
    }

    @GetMapping("/unapproved")
    public List<POI> getUnapprovedPOIs() {
        return poiService.getUnapprovedPOIs();
    }

    @PutMapping("/approve/{id}")
    public POI approvePOI(@PathVariable Long id, Principal principal) {
        Long userId = getUserFromPrincipal(principal).getId();
        return poiService.approvePOI(id, userId);
    }

    private Users getUserFromPrincipal(Principal principal) {
        Users user = userRepository.findByUsername(principal.getName());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }
}
