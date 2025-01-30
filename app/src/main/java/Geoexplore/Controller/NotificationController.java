package Geoexplore.Controller;

import Geoexplore.Notification.Notification;
import Geoexplore.Notification.NotificationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationManager notificationManager;

    @Autowired
    public NotificationController(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    // Crea una nuova notifica
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification savedNotification = notificationManager.saveNotification(notification);
        return ResponseEntity.ok(savedNotification);
    }

    // Recupera tutte le notifiche
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationManager.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    // Recupera una notifica per ID
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Optional<Notification> notification = notificationManager.getNotificationById(id);
        return notification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Elimina una notifica per ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        if (notificationManager.getNotificationById(id).isPresent()) {
            notificationManager.deleteNotification(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
