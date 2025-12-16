package net.projetmgsi.gestionsupermarche.statistics;

import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.statistics.dto.DashboardStatsDTO;
import net.projetmgsi.gestionsupermarche.statistics.dto.TopProduitDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatisticsService {

    DashboardStatsDTO getDashboardStats(LocalDateTime date);

    List<TopProduitDTO> getTop5Produits(LocalDateTime date);
    List<Produit> getProduitsStockFaible();

    List<Map<String, Object>>getVentesGraphData(LocalDateTime from,LocalDateTime to);

    List<Map<String, Object>> getPaiementStats(LocalDateTime date);






    }

