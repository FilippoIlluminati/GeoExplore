package Geoexplore.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationManager {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationManager(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Salva una nuova notifica
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    // Recupera tutte le notifiche
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // Recupera una notifica per ID
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    // Recupera tutte le notifiche di un utente specifico
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUtenteId(userId);
    }

    // Marca una notifica come letta
    public Notification markNotificationAsRead(Long id) {
        return notificationRepository.findById(id).map(notification -> {
            notification.setStato(NotificationStatus.LETTA);
            return notificationRepository.save(notification);
        }).orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    // Elimina una notifica
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
