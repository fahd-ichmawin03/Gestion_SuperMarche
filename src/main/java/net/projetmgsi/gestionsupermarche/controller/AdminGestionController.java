package net.projetmgsi.gestionsupermarche.controller;

import net.projetmgsi.gestionsupermarche.entity.Categorie;
import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.repository.CategorieRepository;
import net.projetmgsi.gestionsupermarche.repository.ProduitRepository;
import net.projetmgsi.gestionsupermarche.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminGestionController {

    @Autowired private AdminService adminService;
    @Autowired private ProduitRepository produitRepository;
    @Autowired private CategorieRepository categorieRepository;

    // PAGE CAISSIERS
    @GetMapping("/gestion-caissiers")
    public String pageCaissiers(Model model) {
        model.addAttribute("caissiers", adminService.listerTousLesCaissiers());
        return "admin/gestion_caissiers";
    }

    @PostMapping("/gestion-caissiers/ajouter")
    public String addCaissier(@RequestParam String username, @RequestParam String password) {
        try { adminService.creerCaissier(username, password); } catch(Exception e) {}
        return "redirect:/admin/gestion-caissiers";
    }

    @GetMapping("/gestion-caissiers/supprimer/{id}")
    public String delCaissier(@PathVariable Long id) {
        adminService.supprimerCaissier(id);
        return "redirect:/admin/gestion-caissiers";
    }

    // PAGE STOCKS & PRODUITS
    @GetMapping("/gestion-stocks")
    public String pageStocks(Model model) {
        // IMPORTANT : On ne charge QUE les produits actifs (pas ceux supprimés logiquement)
        List<Produit> produits = produitRepository.findByActifTrue();

        List<Categorie> categories = categorieRepository.findAll();

        model.addAttribute("produits", produits);
        model.addAttribute("categories", categories);

        return "admin/gestion_stocks";
    }

    // Action: Ajouter du stock
    @PostMapping("/gestion-stocks/approvisionner")
    public String addStock(@RequestParam Long produitId, @RequestParam int quantite, Authentication auth) {
        adminService.approvisionnerStock(produitId, quantite, auth.getName());
        return "redirect:/admin/gestion-stocks";
    }

    // SUPPRESSION (Conforme à "opt Supprimer")
    @GetMapping("/gestion-stocks/supprimer/{id}")
    public String supprimerProduit(@PathVariable Long id) {
        adminService.supprimerProduit(id);
        return "redirect:/admin/gestion-stocks";
    }

    // MODIFICATION - Afficher le formulaire (Conforme à "opt Modifier")
    @GetMapping("/gestion-stocks/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable Long id, Model model) {
        Produit p = produitRepository.findById(id).orElseThrow();
        model.addAttribute("produitAModifier", p);
        model.addAttribute("categories", categorieRepository.findAll());
        return "admin/modifier_produit";
    }

    // MODIFICATION :Enregistrer les changements
    @PostMapping("/gestion-stocks/modifier")
    public String validerModification(@ModelAttribute Produit produit) {
        adminService.modifierProduit(produit.getId(), produit);
        return "redirect:/admin/gestion-stocks";
    }

    // Action: Créer un NOUVEAU produit
    @PostMapping("/gestion-stocks/nouveau")
    public String nouveauProduit(
            // On récupère "reference" du formulaire HTML
            @RequestParam String reference,
            @RequestParam String nom,
            @RequestParam String description,
            @RequestParam BigDecimal prix,
            @RequestParam int stock,
            @RequestParam int stockMinimal,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateExpiration,
            @RequestParam Long categorieId) {

        Categorie cat = categorieRepository.findById(categorieId).orElseThrow();

        Produit p = new Produit();
        p.setCode(reference);
        p.setNom(nom);
        p.setDescription(description);
        p.setPrix(prix);
        p.setStock(stock);
        p.setStockMinimal(stockMinimal);
        p.setDateExpiration(dateExpiration);
        p.setCategorie(cat);
        p.setActif(true); // Actif par défaut

        produitRepository.save(p);

        return "redirect:/admin/gestion-stocks";
    }
}