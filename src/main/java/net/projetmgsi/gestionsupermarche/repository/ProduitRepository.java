package net.projetmgsi.gestionsupermarche.repository;

import net.projetmgsi.gestionsupermarche.entity.Categorie;
import net.projetmgsi.gestionsupermarche.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    Optional<Produit> findByCode(String code);

    List<Produit> findByNomContainingIgnoreCase(String nom);

    List<Produit> findByActifTrue();

    List<Produit> findByCategorie(Categorie categorie);

    List<Produit> findByStockLessThan(Integer seuil);  // Produits en rupture
}