package Geoexplore.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Restituisce tutte le notifiche presenti nel sistema
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // Restituisce una notifica specifica per ID
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    // Crea e salva una nuova notifica
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
        }).orElseThrow(() -> new RuntimeException("Notifica non trovata"));
    }

    // Elimina una notifica specifica
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    // Restituisce tutte le notifiche associate a un utente
    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUtenteId(userId);
    }

    // Restituisce solo le notifiche non lette associate a un utente
    public List<Notification> getUnreadNotificationsByUser(Long userId) {
        return notificationRepository.findByUtenteIdAndStato(userId, NotificationStatus.NON_LETTA);
    }

    // Marca come letta una notifica specifica
    public Notification markNotificationAsRead(Long id) {
        return notificationRepository.findById(id).map(notification -> {
            notification.setStato(NotificationStatus.LETTA);
            return notificationRepository.save(notification);
        }).orElseThrow(() -> new RuntimeException("Notifica non trovata"));
    }
}
