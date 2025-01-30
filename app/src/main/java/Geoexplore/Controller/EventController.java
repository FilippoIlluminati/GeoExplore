package Geoexplore.Controller;

import Geoexplore.Event.Event;
import Geoexplore.Event.EventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventManager eventManager;

    @Autowired
    public EventController(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    // Crea un nuovo evento
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event savedEvent = eventManager.saveEvent(event);
        return ResponseEntity.ok(savedEvent);
    }

    // Recupera tutti gli eventi
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventManager.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // Recupera un evento per ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventManager.getEventById(id);
        return event.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Elimina un evento per ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventManager.getEventById(id).isPresent()) {
            eventManager.deleteEvent(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
