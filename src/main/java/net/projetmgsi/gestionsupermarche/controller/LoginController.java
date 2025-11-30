package net.projetmgsi.gestionsupermarche.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login.html";  // Retourne la vue login.html
    }
}