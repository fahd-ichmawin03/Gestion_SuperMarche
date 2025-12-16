package net.projetmgsi.gestionsupermarche.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AdminController {

    @GetMapping("/")
    public String home() {
        return "Bienvenue dans Gestion SuperMarché - Application démarrée avec succès !";
    }

    @GetMapping("/admin/users")
    public String manageUsers() {
        return "Gestion des utilisateurs";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard"; }}