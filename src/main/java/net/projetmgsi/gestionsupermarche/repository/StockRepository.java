package net.projetmgsi.gestionsupermarche.repository;

import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.entity.Stock;
import net.projetmgsi.gestionsupermarche.entity.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findByProduit(Produit produit);

    List<Stock> findByProduitOrderByDateMouvementDesc(Produit produit);

    List<Stock> findByTypeMouvement(TypeMouvement typeMouvement);

    List<Stock> findByDateMouvementBetween(LocalDateTime debut, LocalDateTime fin);

    List<Stock> findByUtilisateur(String utilisateur);
}