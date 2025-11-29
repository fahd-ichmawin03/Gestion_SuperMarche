package net.projetmgsi.gestionsupermarche.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "Bienvenue dans le dashboard Admin";
    }

    @GetMapping("/admin/users")
    public String manageUsers() {
        return "Gestion des utilisateurs";
    }
}
