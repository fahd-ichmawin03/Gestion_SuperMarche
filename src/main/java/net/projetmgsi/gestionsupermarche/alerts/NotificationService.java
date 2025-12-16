package net.projetmgsi.gestionsupermarche.alerts;
import lombok.RequiredArgsConstructor;
import net.projetmgsi.gestionsupermarche.entity.Notification;
import net.projetmgsi.gestionsupermarche.entity.NotificationType;
import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    public void creerNotification(String message, NotificationType type, Produit produit) {

        boolean existeDeja = repository
                .existsByProduitAndTypeAndLueFalse(produit, type);

        if (existeDeja || produit == null || produit.getActif()==false) {
            return;
        }

        Notification n = new Notification();
        n.setMessage(message);
        n.setType(type);
        n.setProduit(produit);
        n.setLue(false);
        n.setDateCreation(LocalDateTime.now());

        repository.save(n);
    }

    public List<Notification> getAlertesNonLues() {
        return repository.findByLueFalseOrderByDateCreationDesc();
    }

    public long countNonLues() {
        return repository.countByLueFalse();
    }

    @Transactional
    public void marquerCommeLue(Long id) {

        Notification n = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification introuvable"));

        n.setLue(true);
        repository.save(n);

    }


    public Notification findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Notification introuvable")
        );
    }

}
