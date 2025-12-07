package net.projetmgsi.gestionsupermarche.controller;

import net.projetmgsi.gestionsupermarche.entity.MoyenPaiement;
import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.entity.Vente;
import net.projetmgsi.gestionsupermarche.service.CaissierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/caissier")
@CrossOrigin(origins = "*")
public class CaissierController {

    @Autowired
    private CaissierService caissierService;

    /**
     * API - Données du dashboard caissier
     */
    @GetMapping("/api/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(Authentication auth) {
        String username = auth.getName();
        List<Vente> ventesDuJour = caissierService.obtenirVentesDuJour(username);

        BigDecimal totalDuJour = ventesDuJour.stream()
                .map(Vente::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> response = new HashMap<>();
        response.put("caissier", username);
        response.put("nombreVentes", ventesDuJour.size());
        response.put("totalDuJour", totalDuJour);
        response.put("message", "Bienvenue " + username);

        return ResponseEntity.ok(response);
    }

    /**
     * Rechercher un produit par code-barres
     */
    @GetMapping("/produits/code/{code}")
    public ResponseEntity<Produit> rechercherParCode(@PathVariable String code) {
        try {
            Produit produit = caissierService.rechercherProduitParCode(code);
            return ResponseEntity.ok(produit);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Rechercher des produits par nom
     */
    @GetMapping("/produits/recherche")
    public ResponseEntity<List<Produit>> rechercherParNom(@RequestParam String nom) {
        List<Produit> produits = caissierService.rechercherProduitsParNom(nom);
        return ResponseEntity.ok(produits);
    }

    /**
     * Obtenir tous les produits actifs
     */
    @GetMapping("/produits")
    public ResponseEntity<List<Produit>> obtenirProduits() {
        List<Produit> produits = caissierService.obtenirProduitsActifs();
        return ResponseEntity.ok(produits);
    }

    /**
     * Calculer le total d'un panier
     */
    @PostMapping("/panier/calculer")
    public ResponseEntity<Map<String, Object>> calculerTotal(@RequestBody List<CaissierService.ItemVente> items) {
        try {
            BigDecimal total = caissierService.calculerTotal(items);
            Map<String, Object> response = new HashMap<>();
            response.put("total", total);
            response.put("nombreArticles", items.stream().mapToInt(CaissierService.ItemVente::getQuantite).sum());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Créer une vente
     */
    @PostMapping("/ventes/creer")
    public ResponseEntity<?> creerVente(
            @RequestBody VenteRequest request,
            Authentication auth) {
        try {
            String caissier = auth.getName();
            Vente vente = caissierService.creerVente(
                    caissier,
                    request.getMoyenPaiement(),
                    request.getItems()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("venteId", vente.getId());
            response.put("total", vente.getTotal());
            response.put("message", "Vente enregistrée avec succès");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtenir l'historique des ventes du jour
     */
    @GetMapping("/ventes/aujourd-hui")
    public ResponseEntity<List<Vente>> obtenirVentesDuJour(Authentication auth) {
        String caissier = auth.getName();
        List<Vente> ventes = caissierService.obtenirVentesDuJour(caissier);
        return ResponseEntity.ok(ventes);
    }

    /**
     * Obtenir le détail d'une vente
     */
    @GetMapping("/ventes/{id}")
    public ResponseEntity<Vente> obtenirVente(@PathVariable Long id) {
        try {
            Vente vente = caissierService.obtenirVente(id);
            return ResponseEntity.ok(vente);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DTO pour créer une vente
    public static class VenteRequest {
        private MoyenPaiement moyenPaiement;
        private List<CaissierService.ItemVente> items;

        public MoyenPaiement getMoyenPaiement() { return moyenPaiement; }
        public void setMoyenPaiement(MoyenPaiement moyenPaiement) { this.moyenPaiement = moyenPaiement; }
        public List<CaissierService.ItemVente> getItems() { return items; }
        public void setItems(List<CaissierService.ItemVente> items) { this.items = items; }
    }
}