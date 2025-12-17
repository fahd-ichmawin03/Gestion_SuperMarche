package net.projetmgsi.gestionsupermarche.repository;

import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.entity.Stock;
import net.projetmgsi.gestionsupermarche.entity.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findByProduit(Produit produit);

    List<Stock> findByProduitOrderByDateMouvementDesc(Produit produit);

    List<Stock> findByTypeMouvement(TypeMouvement typeMouvement);

    List<Stock> findByDateMouvementBetween(LocalDateTime debut, LocalDateTime fin);

    List<Stock> findByUtilisateur(String utilisateur);

    @Query(value = """
    SELECT DATE(date_mouvement) AS jour, SUM(quantite) AS total
    FROM mouvements_stock
    WHERE type_mouvement = 'SORTIE'
      AND (:date IS NULL OR DATE(date_mouvement) = :date)
    GROUP BY DATE(date_mouvement)
    ORDER BY jour
""", nativeQuery = true)
    List<Object[]> getVentesParJour(@Param("date") LocalDate date);


}