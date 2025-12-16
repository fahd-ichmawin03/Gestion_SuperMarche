package net.projetmgsi.gestionsupermarche.alerts.scheduler;

import lombok.RequiredArgsConstructor;
import net.projetmgsi.gestionsupermarche.alerts.NotificationService;
import net.projetmgsi.gestionsupermarche.entity.NotificationType;
import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.repository.NotificationRepository;
import net.projetmgsi.gestionsupermarche.repository.ProduitRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class ExpirationScheduler {

    private final ProduitRepository produitRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 8 * * *") //  tous les jours à 08h
    public void verifierProduitsExpires() {

        LocalDate today = LocalDate.now();

        List<Produit> expires =
                produitRepository.findByDateExpirationBeforeAndActifTrue(today);

        for (Produit p : expires) {

            notificationService.creerNotification(
                    "Produit expiré : " + p.getNom(),
                    NotificationType.EXPIRATION,
                    p
            );
        }
    }
}

