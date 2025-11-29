package net.projetmgsi.gestionsupermarche.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CaissierController {

    @GetMapping("/caissier/ventes")
    public String listVentes() {
        return "Liste des ventes du caissier";
    }

    @GetMapping("/caissier/creer-vente")
    public String creerVente() {
        return "Création d'une vente";
    }
}
