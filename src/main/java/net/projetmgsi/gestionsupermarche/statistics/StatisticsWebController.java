package net.projetmgsi.gestionsupermarche.statistics;

import lombok.RequiredArgsConstructor;
import net.projetmgsi.gestionsupermarche.alerts.AlertService;
import net.projetmgsi.gestionsupermarche.alerts.NotificationService;
import net.projetmgsi.gestionsupermarche.alerts.dto.StockAlertDTO;
import net.projetmgsi.gestionsupermarche.entity.Notification;
import net.projetmgsi.gestionsupermarche.repository.LigneVenteRepository;
import net.projetmgsi.gestionsupermarche.repository.ProduitRepository;
import net.projetmgsi.gestionsupermarche.repository.VenteRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final VenteRepository venteRepository;
    private final LigneVenteRepository ligneventeRepository;
    private final ProduitRepository produitRepository;

    @GetMapping("/statistics/dashboard")
    public String dashboard(Model model,
                            @RequestParam(required = false)
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                            LocalDate date) {

        LocalDateTime dateTime =
                (date != null) ? date.atStartOfDay() : LocalDateTime.now();

        // uniquement les données NON dynamiques
        model.addAttribute("stats",
                statisticsService.getDashboardStats(dateTime));

        model.addAttribute("topProduits",
                statisticsService.getTop5Produits(dateTime));

        model.addAttribute("nbNotifications",
                notificationService.countNonLues());
        return "statistics/dashboard";
    }


    @GetMapping("/alerts")
    public String alerts(Model model) {

        List<StockAlertDTO> alertes = alertService.getAlerts();
        List<Notification> notifications = notificationService.getAlertesNonLues();

        model.addAttribute("alertes", alertes);
        model.addAttribute("notifications", notifications);

        model.addAttribute("nbNotifications",
                notificationService.countNonLues());
        return "statistics/alerts";
    }

    @PostMapping("/notifications/jaiRecu/{id}")
    public String jaiRecu(@PathVariable Long id) {
        notificationService.marquerCommeLue(id);
        return "redirect:/alerts";
    }





}

