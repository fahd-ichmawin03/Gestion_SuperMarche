package net.projetmgsi.gestionsupermarche.alerts;

import net.projetmgsi.gestionsupermarche.alerts.dto.StockAlertDTO;

import java.util.List;

public interface AlertService {
    List<StockAlertDTO> getAlerts();
}
