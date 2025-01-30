package Geoexplore.Notification;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Ottiene tutte le notifiche
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // Trova una notifica per ID
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    // Salva una nuova notifica
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    // Aggiorna una notifica esistente
    public Notification updateNotification(Long id, Notification notificationDetails) {
        return notificationRepository.findById(id).map(notification -> {
            notification.setUtente(notificationDetails.getUtente());
            notification.setTesto(notificationDetails.getTesto());
            notification.setStato(notificationDetails.getStato());
            return notificationRepository.save(notification);
        }).orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    // Elimina una notifica
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}