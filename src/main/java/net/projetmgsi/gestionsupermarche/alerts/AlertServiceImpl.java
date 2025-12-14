package net.projetmgsi.gestionsupermarche.alerts;

import lombok.RequiredArgsConstructor;
import net.projetmgsi.gestionsupermarche.entity.NotificationType;
import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.repository.NotificationRepository;
import net.projetmgsi.gestionsupermarche.repository.ProduitRepository;
import net.projetmgsi.gestionsupermarche.alerts.dto.StockAlertDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final ProduitRepository produitRepository;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @Override
    public List<StockAlertDTO> getAlerts() {

        List<StockAlertDTO> alerts = new ArrayList<>();

        for (Produit p : produitRepository.findAll()) {

            // 🚨 RUPTURE
            if (p.getStock() == 0) {

                String msg = "Rupture de stock : " + p.getNom();
                alerts.add(new StockAlertDTO(p.getNom(), "Rupture", 0));

                if (!notificationRepository.existsByMessageAndLueFalse(msg)) {
                    notificationService.creerNotification(
                            msg,
                            NotificationType.RUPTURE
                    );
                }
            }
            // ⚠️ STOCK FAIBLE
            else if (p.getStock() < p.getStockMinimal()) {

                String msg = "Stock faible pour : " + p.getNom() +
                        " (reste " + p.getStock() + ")";

                alerts.add(new StockAlertDTO(
                        p.getNom(),
                        "Stock faible",
                        p.getStock()
                ));

                if (!notificationRepository.existsByMessageAndLueFalse(msg)) {
                    notificationService.creerNotification(
                            msg,
                            NotificationType.STOCK_FAIBLE
                    );
                }
            }
        }

        return alerts;
    }
}
