package Geoexplore.Notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Recupera tutte le notifiche di un utente specifico
    List<Notification> findByUtenteId(Long utenteId);

    // Recupera solo le notifiche NON LETTE di un utente specifico
    List<Notification> findByUtenteIdAndStato(Long utenteId, NotificationStatus stato);
}
