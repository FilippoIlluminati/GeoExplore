package Geoexplore.Controller;

import Geoexplore.Notification.Notification;
import Geoexplore.Notification.NotificationService;
import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    @Autowired private UserRepository userRepository;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Crea una nuova notifica
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification savedNotification = notificationService.createNotification(notification);
        return ResponseEntity.ok(savedNotification);
    }

    // Restituisce tutte le notifiche
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    // Restituisce una notifica specifica per ID
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Optional<Notification> notification = notificationService.getNotificationById(id);
        return notification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Elimina una notifica specifica per ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        if (notificationService.getNotificationById(id).isPresent()) {
            notificationService.deleteNotification(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Restituisce le notifiche dell’utente autenticato
    @GetMapping("/user")
    public ResponseEntity<List<Notification>> getNotificationsByUser(
            @AuthenticationPrincipal UserDetails principal) {

        Users user = userRepository.findByUsername(principal.getUsername());
        List<Notification> notifications = notificationService.getNotificationsByUser(user.getId());
        return ResponseEntity.ok(notifications);
    }

    // Restituisce solo le notifiche NON lette dell’utente autenticato
    @GetMapping("/user/unread")
    public ResponseEntity<List<Notification>> getUnreadNotificationsByUser(
            @AuthenticationPrincipal UserDetails principal) {

        Users user = userRepository.findByUsername(principal.getUsername());
        List<Notification> notifications = notificationService.getUnreadNotificationsByUser(user.getId());
        return ResponseEntity.ok(notifications);
    }

    // Marca una notifica come letta
    @PutMapping("/{id}/mark-as-read")
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable Long id) {
        Notification updatedNotification = notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok(updatedNotification);
    }
}
