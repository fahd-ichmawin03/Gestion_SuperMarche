package net.projetmgsi.gestionsupermarche.alerts;

import lombok.RequiredArgsConstructor;
import net.projetmgsi.gestionsupermarche.alerts.dto.StockAlertDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public List<StockAlertDTO> getAllAlerts() {
        return alertService.getAlerts();
    }
}
