package net.projetmgsi.gestionsupermarche.service;

import lombok.RequiredArgsConstructor;
import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.repository.ProduitRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProduitService {

    private final ProduitRepository produitRepository;

    public void supprimerProduit(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        produit.setActif(false); // Soft delete
        produitRepository.save(produit);
    }
}
