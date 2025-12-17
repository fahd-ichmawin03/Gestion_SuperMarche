package net.projetmgsi.gestionsupermarche.statistics;

import lombok.RequiredArgsConstructor;
import net.projetmgsi.gestionsupermarche.alerts.AlertService;
import net.projetmgsi.gestionsupermarche.alerts.NotificationService;
import net.projetmgsi.gestionsupermarche.alerts.dto.StockAlertDTO;
import net.projetmgsi.gestionsupermarche.entity.Notification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StatisticsWebController {

    private final StatisticsService statisticsService;
    private final AlertService alertService;
    private final NotificationService notificationService;

    @GetMapping("/statistics/dashboard")
    public String dashboard(Model model,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDateTime dateTime = (date != null) ? date.atStartOfDay() : LocalDateTime.now();

        model.addAttribute("stats", statisticsService.getDashboardStats(dateTime));
        model.addAttribute("topProduits", statisticsService.getTop5Produits(dateTime));

        List<Map<String, Object>> graph = statisticsService.getVentesGraphData(dateTime);
        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (Map<String, Object> obj : graph) {
            labels.add(obj.get("date").toString());
            values.add(((Number) obj.get("quantite")).intValue());
        }

        model.addAttribute("chartLabels", labels);
        model.addAttribute("chartValues", values);

        return "statistics/dashboard";
    }


    @GetMapping("/alerts")
    public String alerts(Model model) {

        List<StockAlertDTO> alertes = alertService.getAlerts();
        List<Notification> notifications = notificationService.getAlertesNonLues();

        model.addAttribute("alertes", alertes);
        model.addAttribute("notifications", notifications);
        return "statistics/alerts";
    }




}

