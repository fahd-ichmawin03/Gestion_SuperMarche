package net.projetmgsi.gestionsupermarche.repository;

import net.projetmgsi.gestionsupermarche.entity.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {

    List<Vente> findByCaissier(String caissier);

    List<Vente> findByDateVenteBetween(LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT v FROM Vente v WHERE v.caissier = ?1 AND v.dateVente BETWEEN ?2 AND ?3")
    List<Vente> findVentesCaissierDuJour(String caissier, LocalDateTime debut, LocalDateTime fin);

    // Chiffre d'affaires pour une date précise
    @Query("SELECT SUM(v.total) FROM Vente v WHERE DATE(v.dateVente) = :date")
    Double  getChiffreAffaireParJour(@Param("date") LocalDate date);

    @Query("SELECT SUM(v.total) FROM Vente v WHERE MONTH(v.dateVente) = :mois AND YEAR(v.dateVente) = :annee")
    Double  getChiffreAffaireParMois(@Param("mois") int mois, @Param("annee") int annee);

    @Query("SELECT SUM(v.total) FROM Vente v WHERE YEAR(v.dateVente) = :annee")
    Double  getChiffreAffaireParAnnee(@Param("annee") int annee);


    @Query("""
        SELECT v.moyenPaiement, SUM(v.total)
        FROM Vente v
        WHERE (:date IS NULL OR DATE(v.dateVente) = :date)
        GROUP BY v.moyenPaiement
    """)
    List<Object[]> getStatsParPaiement(@Param("date") LocalDate date);

    @Query("SELECT SUM(v.total) FROM Vente v")
    Double getTotalCA();

    @Query("""
    SELECT DATE(v.dateVente) AS date,
           COUNT(v.id) AS quantite
    FROM Vente v
    WHERE v.dateVente BETWEEN :from AND :to
    GROUP BY DATE(v.dateVente)
    ORDER BY DATE(v.dateVente)
""")
    List<Map<String, Object>> getVentesEntre(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );


}
