package net.projetmgsi.gestionsupermarche.statistics;

import lombok.RequiredArgsConstructor;
import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.repository.LigneVenteRepository;
import net.projetmgsi.gestionsupermarche.repository.ProduitRepository;
import net.projetmgsi.gestionsupermarche.repository.StockRepository;
import net.projetmgsi.gestionsupermarche.repository.VenteRepository;
import net.projetmgsi.gestionsupermarche.statistics.dto.DashboardStatsDTO;
import net.projetmgsi.gestionsupermarche.statistics.dto.TopProduitDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final VenteRepository venteRepository;
    private final LigneVenteRepository ligneVenteRepository;
    private final ProduitRepository produitRepository;
    private final StockRepository stockRepository;

    @Override
    public DashboardStatsDTO getDashboardStats(LocalDateTime date) {

        LocalDate d = (date != null) ? date.toLocalDate() : LocalDate.now();

        // 🔹 Chiffre d'affaires
        Double caJour = venteRepository.getChiffreAffaireParJour(d);
        Double caMois = venteRepository.getChiffreAffaireParMois(d.getMonthValue(), d.getYear());
        Double caAnnee = venteRepository.getChiffreAffaireParAnnee(d.getYear());

        if (caJour == null) caJour = 0.0;
        if (caMois == null) caMois = 0.0;
        if (caAnnee == null) caAnnee = 0.0;

        // 🔹 Nombre de ventes
        Integer nbVentes = Math.toIntExact(venteRepository.count());

        // 🔹 Clients servis (via lignes de vente)
        Integer nbClients = Math.toIntExact(ligneVenteRepository.countClientsServis());

        // 🔹 Panier moyen
        Double totalCA = venteRepository.getTotalCA();
        if (totalCA == null) totalCA = 0.0;

        Double panierMoyen = (nbVentes == 0) ? 0.0 : totalCA / nbVentes;

        // 🔹 Produits en rupture
        Integer nbRupture = Math.toIntExact(
                produitRepository.countByStockLessThanEqual(0)
        );

        // 🔹 Stock faible (optionnel, déjà OK chez toi)
        Double stockFaible = (double) produitRepository.getStockFaible().size();

        return new DashboardStatsDTO(
                caJour,
                caMois,
                caAnnee,
                stockFaible,
                nbVentes,
                nbClients,
                panierMoyen,
                nbRupture
        );
    }


    @Override
    public List<TopProduitDTO> getTop5Produits(LocalDateTime date) {
        // Si tu veux filtrer par date, il faut adapter ta requête dans LigneVenteRepository
        // Pour l'instant, on prend les ventes globales
        return ligneVenteRepository.getTopProduits()
                .stream()
                .limit(5)
                .map(obj -> new TopProduitDTO((String) obj[0], ((Long) obj[1])))
                .toList();
    }

    @Override
    public List<Produit> getProduitsStockFaible() {
        return produitRepository.getStockFaible();
    }

    @Override
    public List<Map<String, Object>> getVentesGraphData(
            LocalDateTime from,
            LocalDateTime to
    ) {
        return venteRepository.getVentesEntre(from, to);
    }







    @Override
    public List<Map<String, Object>> getPaiementStats(LocalDateTime date) {

        LocalDate targetDate = (date != null) ? date.toLocalDate() : null;

        List<Object[]> raw = venteRepository.getStatsParPaiement(targetDate);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] obj : raw) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", obj[0]); // ex: ESPECE, CARTE
            map.put("total", ((Number) obj[1]).doubleValue());
            result.add(map);
        }
        return result;
    }
}
