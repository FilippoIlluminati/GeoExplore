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

    // Crea e salva una nuova notifica
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    // Restituisce tutte le notifiche presenti nel sistema
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // Restituisce una notifica per ID
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    // Restituisce tutte le notifiche associate a un utente
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUtenteId(userId);
    }

    // Marca come letta una notifica specifica
    public Notification markNotificationAsRead(Long id) {
        return notificationRepository.findById(id).map(notification -> {
            notification.setStato(NotificationStatus.LETTA);
            return notificationRepository.save(notification);
        }).orElseThrow(() -> new RuntimeException("Notifica non trovata"));
    }

    // Elimina una notifica per ID
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
