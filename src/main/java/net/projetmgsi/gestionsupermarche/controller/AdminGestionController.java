package net.projetmgsi.gestionsupermarche.controller;

import net.projetmgsi.gestionsupermarche.repository.ProduitRepository;
import net.projetmgsi.gestionsupermarche.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminGestionController {

    @Autowired private AdminService adminService;
    @Autowired private ProduitRepository produitRepository;

    @GetMapping("/gestion-caissiers")
    public String pageCaissiers(Model model) {
        model.addAttribute("caissiers", adminService.listerTousLesCaissiers());
        return "admin/gestion_caissiers";
    }

    @PostMapping("/gestion-caissiers/ajouter")
    public String addCaissier(@RequestParam String username, @RequestParam String password) {
        try {
            adminService.creerCaissier(username, password);
        } catch(Exception e) {
            // Tu pourrais ajouter un message d'erreur ici si tu veux aller plus loin
        }
        return "redirect:/admin/gestion-caissiers";
    }

    @GetMapping("/gestion-caissiers/supprimer/{id}")
    public String delCaissier(@PathVariable Long id) {
        adminService.supprimerCaissier(id);
        return "redirect:/admin/gestion-caissiers";
    }

    @GetMapping("/gestion-stocks")
    public String pageStocks(Model model) {
        model.addAttribute("produits", produitRepository.findAll());
        return "admin/gestion_stocks";
    }

    @PostMapping("/gestion-stocks/approvisionner")
    public String addStock(@RequestParam Long produitId, @RequestParam int quantite, Authentication auth) {
        adminService.approvisionnerStock(produitId, quantite, auth.getName());
        return "redirect:/admin/gestion-stocks";
    }
}