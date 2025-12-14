package net.projetmgsi.gestionsupermarche.alerts;
import lombok.RequiredArgsConstructor;
import net.projetmgsi.gestionsupermarche.entity.Notification;
import net.projetmgsi.gestionsupermarche.entity.NotificationType;
import net.projetmgsi.gestionsupermarche.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public void creerNotification(String message, NotificationType type) {
        Notification n = new Notification();
        n.setMessage(message);
        n.setType(type);
        repository.save(n);
    }

    public List<Notification> getAlertesNonLues() {
        return repository.findByLueFalseOrderByDateCreationDesc();
    }

    public long countNonLues() {
        return repository.countByLueFalse();
    }

    public void marquerCommeLue(Long id) {
        Notification n = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification introuvable"));
        n.setLue(true);
        repository.save(n);
    }
}
