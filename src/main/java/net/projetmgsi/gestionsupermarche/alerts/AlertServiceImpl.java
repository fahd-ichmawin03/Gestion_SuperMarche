package net.projetmgsi.gestionsupermarche.alerts;

import lombok.RequiredArgsConstructor;
import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.repository.ProduitRepository;
import net.projetmgsi.gestionsupermarche.alerts.dto.StockAlertDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final ProduitRepository produitRepository;
    private final NotificationService notificationService; // <-- ajout

    @Override
    public List<StockAlertDTO> getAlerts() {
        List<StockAlertDTO> alerts = new ArrayList<>();

        for (Produit p : produitRepository.findAll()) {

            if (p.getStock() == 0) {
                alerts.add(new StockAlertDTO(p.getNom(), "Rupture", 0));

                // ✅ créer une notification
                notificationService.creerNotification(
                        "Rupture de stock : " + p.getNom()
                );

            } else if (p.getStock() < p.getStockMinimal()) {
                alerts.add(new StockAlertDTO(p.getNom(), "Stock faible", p.getStock()));

                // ✅ créer une notification
                notificationService.creerNotification(
                        "Stock faible pour : " + p.getNom() + " (reste " + p.getStock() + ")"
                );
            }
        }

        return alerts;
    }
}
