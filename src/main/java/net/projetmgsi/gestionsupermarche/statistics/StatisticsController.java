package net.projetmgsi.gestionsupermarche.statistics;

import lombok.RequiredArgsConstructor;
import net.projetmgsi.gestionsupermarche.alerts.NotificationService;
import net.projetmgsi.gestionsupermarche.statistics.dto.DashboardStatsDTO;
import net.projetmgsi.gestionsupermarche.statistics.dto.TopProduitDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final NotificationService notificationService;

    @GetMapping("/dashboard")
    public DashboardStatsDTO getDashboard(@RequestParam(required = false) String date) {
        LocalDateTime filterDate = null;
        if (date != null) {
            filterDate = LocalDate.parse(date).atStartOfDay();
        }
        return statisticsService.getDashboardStats(filterDate);
    }

    @GetMapping("/top-produits")
    public List<TopProduitDTO> getTopProduits(@RequestParam(required = false) String date) {
        LocalDateTime filterDate = null;
        if (date != null) filterDate = LocalDate.parse(date).atStartOfDay();
        return statisticsService.getTop5Produits(filterDate);
    }

    @GetMapping("/ventes-graph")
    public List<Map<String, Object>> getVentesGraph(
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "7") int jours
    ) {
        LocalDate ref = (date != null)
                ? LocalDate.parse(date)
                : LocalDate.now();

        LocalDateTime from = ref.minusDays(jours).atStartOfDay();
        LocalDateTime to = ref.atTime(23, 59, 59);

        return statisticsService.getVentesGraphData(from, to);
    }


    // ✅ Graphique par moyen de paiement
    @GetMapping("/paiements")
    public List<Map<String, Object>> getPaiementsStats(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return statisticsService.getPaiementStats(
                date != null ? date.atStartOfDay() : null
        );
    }




}
