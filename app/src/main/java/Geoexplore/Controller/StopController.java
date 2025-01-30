package Geoexplore.Controller;

import Geoexplore.Journey.Stop;
import Geoexplore.Journey.StopManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stops")
public class StopController {

    private final StopManager stopManager;

    @Autowired
    public StopController(StopManager stopManager) {
        this.stopManager = stopManager;
    }

    // Crea un nuovo Stop
    @PostMapping
    public ResponseEntity<Stop> createStop(@RequestBody Stop stop) {
        Stop savedStop = stopManager.saveStop(stop);
        return ResponseEntity.ok(savedStop);
    }

    // Recupera tutti gli Stop
    @GetMapping
    public ResponseEntity<List<Stop>> getAllStops() {
        List<Stop> stops = stopManager.getAllStops();
        return ResponseEntity.ok(stops);
    }

    // Recupera uno Stop per ID
    @GetMapping("/{id}")
    public ResponseEntity<Stop> getStopById(@PathVariable Long id) {
        Optional<Stop> stop = stopManager.getStopById(id);
        return stop.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Elimina uno Stop per ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStop(@PathVariable Long id) {
        if (stopManager.getStopById(id).isPresent()) {
            stopManager.deleteStop(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
