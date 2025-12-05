package net.projetmgsi.gestionsupermarche.repository;

import net.projetmgsi.gestionsupermarche.entity.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {

    List<Vente> findByCaissier(String caissier);

    List<Vente> findByDateVenteBetween(LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT v FROM Vente v WHERE v.caissier = ?1 AND v.dateVente BETWEEN ?2 AND ?3")
    List<Vente> findVentesCaissierDuJour(String caissier, LocalDateTime debut, LocalDateTime fin);
}