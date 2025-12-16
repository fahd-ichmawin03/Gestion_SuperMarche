package net.projetmgsi.gestionsupermarche.statistics;

import lombok.RequiredArgsConstructor;
import net.projetmgsi.gestionsupermarche.alerts.AlertService;
import net.projetmgsi.gestionsupermarche.alerts.NotificationService;
import net.projetmgsi.gestionsupermarche.alerts.dto.StockAlertDTO;
import net.projetmgsi.gestionsupermarche.entity.Notification;
import net.projetmgsi.gestionsupermarche.entity.NotificationType;
import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.repository.LigneVenteRepository;
import net.projetmgsi.gestionsupermarche.repository.ProduitRepository;
import net.projetmgsi.gestionsupermarche.repository.StockRepository;
import net.projetmgsi.gestionsupermarche.repository.VenteRepository;
import net.projetmgsi.gestionsupermarche.service.PdfExportService;
import net.projetmgsi.gestionsupermarche.service.ProduitService;
import net.projetmgsi.gestionsupermarche.statistics.dto.DashboardStatsDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final PdfExportService pdfExportService;
    private final ProduitService produitService;

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
    @ResponseBody
    public ResponseEntity<?> jaiRecu(@PathVariable Long id) {

        Notification notification = notificationService.findById(id);

        if (notification.getType() == NotificationType.EXPIRATION) {
            Produit produit = notification.getProduit();

            if (produit != null && produit.getActif()==true) {
                produitService.supprimerProduit(produit.getId());
            }
        }

        notificationService.marquerCommeLue(id);

        return ResponseEntity.ok().build();
    }




    @GetMapping("/statistics/export-pdf")
    public ResponseEntity<byte[]> exportDashboardPdf(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {

        DashboardStatsDTO stats = statisticsService.getDashboardStats(
                date != null ? date.atStartOfDay() : null
        );

        byte[] pdf = pdfExportService.exportDashboard(stats);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dashboard.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }




}

