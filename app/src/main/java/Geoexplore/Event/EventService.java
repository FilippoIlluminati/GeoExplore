package Geoexplore.Event;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Ottiene tutti gli eventi
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Trova un evento per ID
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    // Salva un nuovo evento
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // Aggiorna un evento esistente
    public Event updateEvent(Long id, Event eventDetails) {
        return eventRepository.findById(id).map(event -> {
            event.setNome(eventDetails.getNome());
            event.setDescrizione(eventDetails.getDescrizione());
            event.setData(eventDetails.getData());
            event.setLuogo(eventDetails.getLuogo());
            event.setCategoria(eventDetails.getCategoria());
            event.setOrganizzatore(eventDetails.getOrganizzatore());
            return eventRepository.save(event);
        }).orElseThrow(() -> new RuntimeException("Event not found"));
    }

    // Elimina un evento
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}