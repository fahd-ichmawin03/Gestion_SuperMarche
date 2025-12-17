package net.projetmgsi.gestionsupermarche.repository;

import net.projetmgsi.gestionsupermarche.entity.LigneVente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LigneVenteRepository extends JpaRepository<LigneVente, Long> {

    // Top 5 produits les plus vendus
    @Query("SELECT lv.produit.nom, SUM(lv.quantite) AS qte " +
            "FROM LigneVente lv GROUP BY lv.produit.id ORDER BY qte DESC")
    List<Object[]> getTopProduits();
}
