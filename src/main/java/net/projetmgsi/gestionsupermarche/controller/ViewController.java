package net.projetmgsi.gestionsupermarche.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/caissier/dashboard")
    public String caissierDashboard() {
        return "caissier/dashboard";
    }

    @GetMapping("/caissier/vente")
    public String caissierVente() {
        return "caissier/vente";
    }

    @GetMapping("/caissier/historique")
    public String caissierHistorique() {
        return "caissier/historique";
    }
}
