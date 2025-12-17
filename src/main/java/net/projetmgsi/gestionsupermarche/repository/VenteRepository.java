package net.projetmgsi.gestionsupermarche.repository;

import net.projetmgsi.gestionsupermarche.entity.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
}
