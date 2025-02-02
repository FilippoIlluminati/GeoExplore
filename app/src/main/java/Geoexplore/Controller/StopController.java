package Geoexplore.Controller;

import Geoexplore.Journey.Stop;
import Geoexplore.Journey.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stops")
public class StopController {

    @Autowired
    private StopService stopService;

    // Crea un nuovo Stop
    @PostMapping
    public ResponseEntity<Stop> createStop(@RequestBody Stop stop) {
        Stop createdStop = stopService.createStop(stop);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStop);
    }

    // Recupera tutti gli Stop
    @GetMapping
    public ResponseEntity<List<Stop>> getAllStops() {
        List<Stop> stops = stopService.getAllStops();
        return ResponseEntity.ok(stops);
    }

    // Recupera uno Stop per ID
    @GetMapping("/{id}")
    public ResponseEntity<Stop> getStopById(@PathVariable Long id) {
        Optional<Stop> stop = stopService.getStopById(id);
        return stop.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Aggiorna uno Stop esistente
    @PutMapping("/{id}")
    public ResponseEntity<Stop> updateStop(@PathVariable Long id, @RequestBody Stop stop) {
        try {
            Stop updatedStop = stopService.updateStop(id, stop);
            return ResponseEntity.ok(updatedStop);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Elimina uno Stop
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStop(@PathVariable Long id) {
        stopService.deleteStop(id);
        return ResponseEntity.noContent().build();
    }
}
