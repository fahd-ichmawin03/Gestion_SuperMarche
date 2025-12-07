package net.projetmgsi.gestionsupermarche.alerts;


import net.projetmgsi.gestionsupermarche.entity.Notification;
import net.projetmgsi.gestionsupermarche.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void creerNotification(String message) {
        // Vérifie si une notification non lue avec le même message existe déjà
        boolean existe = repository.existsByMessageAndLueFalse(message);
        if (!existe) {
            Notification n = new Notification();
            n.setMessage(message);
            repository.save(n);
        }
    }

    public List<Notification> getAlertesNonLues() {
        return repository.findByLueFalseOrderByDateCreationDesc();
    }

    public void marquerCommeLue(Long notificationId) {
        Notification n = repository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        n.setLue(true);
        repository.save(n);
    }

    public void supprimerNotification(Long notificationId) {
        repository.deleteById(notificationId);
    }


}
