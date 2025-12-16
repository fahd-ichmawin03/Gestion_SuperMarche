package net.projetmgsi.gestionsupermarche.service;

import net.projetmgsi.gestionsupermarche.entity.*;
import net.projetmgsi.gestionsupermarche.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CaissierService {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private StockRepository stockRepository;

    /**
     * Rechercher un produit par code-barres
     */
    public Produit rechercherProduitParCode(String code) {
        return produitRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec le code : " + code));
    }

    /**
     * Rechercher des produits par nom (recherche partielle)
     */
    public List<Produit> rechercherProduitsParNom(String nom) {
        return produitRepository.findByNomContainingIgnoreCase(nom);
    }

    /**
     * Obtenir tous les produits actifs
     */
    public List<Produit> obtenirProduitsActifs() {
        return produitRepository.findByActifTrue();
    }

    /**
     * Créer et valider une vente
     */
    @Transactional
    public Vente creerVente(String caissier, MoyenPaiement moyenPaiement, List<ItemVente> items) {
        // Vérifier le stock avant de créer la vente
        for (ItemVente item : items) {
            Produit produit = produitRepository.findById(item.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé : " + item.getProduitId()));

            if (produit.getStock() < item.getQuantite()) {
                throw new RuntimeException("Stock insuffisant pour " + produit.getNom() +
                        ". Disponible : " + produit.getStock());
            }
        }

        // Créer la vente
        Vente vente = new Vente();
        vente.setCaissier(caissier);
        vente.setMoyenPaiement(moyenPaiement);
        vente.setDateVente(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        // Créer les lignes de vente
        for (ItemVente item : items) {
            Produit produit = produitRepository.findById(item.getProduitId()).get();

            LigneVente ligne = new LigneVente();
            ligne.setVente(vente);
            ligne.setProduit(produit);
            ligne.setQuantite(item.getQuantite());
            ligne.setPrixUnitaire(produit.getPrix());
            ligne.calculateSousTotal();

            vente.getLignes().add(ligne);
            total = total.add(ligne.getSousTotal());

            // Mettre à jour le stock
            int stockAvant = produit.getStock();
            produit.setStock(stockAvant - item.getQuantite());
            produitRepository.save(produit);

            // Enregistrer le mouvement de stock
            Stock mouvement = new Stock();
            mouvement.setProduit(produit);
            mouvement.setTypeMouvement(TypeMouvement.VENTE);
            mouvement.setQuantite(item.getQuantite());
            mouvement.setStockAvant(stockAvant);
            mouvement.setStockApres(produit.getStock());
            mouvement.setUtilisateur(caissier);
            mouvement.setCommentaire("Vente caisse");
            stockRepository.save(mouvement);
        }

        vente.setTotal(total);
        return venteRepository.save(vente);
    }

    /**
     * Obtenir l'historique des ventes du jour pour un caissier
     */
    public List<Vente> obtenirVentesDuJour(String caissier) {
        LocalDateTime debutJour = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime finJour = LocalDateTime.now().with(LocalTime.MAX);
        return venteRepository.findVentesCaissierDuJour(caissier, debutJour, finJour);
    }

    /**
     * Obtenir le détail d'une vente
     */
    public Vente obtenirVente(Long id) {
        return venteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vente non trouvée : " + id));
    }

    /**
     * Calculer le total du panier avant validation
     */
    public BigDecimal calculerTotal(List<ItemVente> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemVente item : items) {
            Produit produit = produitRepository.findById(item.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
            BigDecimal sousTotal = produit.getPrix().multiply(BigDecimal.valueOf(item.getQuantite()));
            total = total.add(sousTotal);
        }
        return total;
    }

    // Classe interne pour représenter un item du panier
    public static class ItemVente {
        private Long produitId;
        private Integer quantite;

        public ItemVente() {}

        public ItemVente(Long produitId, Integer quantite) {
            this.produitId = produitId;
            this.quantite = quantite;
        }

        public Long getProduitId() { return produitId; }
        public void setProduitId(Long produitId) { this.produitId = produitId; }
        public Integer getQuantite() { return quantite; }
        public void setQuantite(Integer quantite) { this.quantite = quantite; }
    }
}