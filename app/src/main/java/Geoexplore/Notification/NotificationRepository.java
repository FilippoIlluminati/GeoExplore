package Geoexplore.Notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Restituisce tutte le notifiche per uno specifico utente
    List<Notification> findByUtenteId(Long utenteId);

    // Restituisce solo le notifiche non lette per uno specifico utente
    List<Notification> findByUtenteIdAndStato(Long utenteId, NotificationStatus stato);
}
