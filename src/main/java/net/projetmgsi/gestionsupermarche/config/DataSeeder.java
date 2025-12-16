package net.projetmgsi.gestionsupermarche.config;

import net.projetmgsi.gestionsupermarche.entity.*;
import net.projetmgsi.gestionsupermarche.repository.*;
import net.projetmgsi.gestionsupermarche.user.Role;
import net.projetmgsi.gestionsupermarche.user.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(RayonRepository rayonRepo,
                                      CategorieRepository categorieRepo,
                                      ProduitRepository produitRepo,
                                      VenteRepository venteRepo,
                                      UserRepository userRepo,
                                      PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepo.count() == 0) {
                System.out.println("👤 CRÉATION DES UTILISATEURS PAR DÉFAUT");

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("1234"));
                admin.setRole(Role.ADMIN);

                User caissier = new User();
                caissier.setUsername("caissier1");
                caissier.setPassword(passwordEncoder.encode("1234"));
                caissier.setRole(Role.CAISSIER);

                userRepo.saveAll(Arrays.asList(admin, caissier));
            }

            if (produitRepo.count() > 0) {
                System.out.println("ℹ️ Données produits déjà existantes.");
                return;
            }

            System.out.println("🚀 INITIALISATION DES DONNÉES PRODUITS");

            // --- Rayons ---
            Rayon boissons = new Rayon(null, "Boissons", "Liquides", "A1", null, true);
            Rayon snacks = new Rayon(null, "Snacks", "Gâteaux", "B2", null, true);
            rayonRepo.saveAll(Arrays.asList(boissons, snacks));

            // --- Catégories ---
            Categorie sodas = new Categorie(null, "Sodas", "Gazéifié", boissons, null, true);
            Categorie eaux = new Categorie(null, "Eaux", "Minérales", boissons, null, true);
            Categorie biscuits = new Categorie(null, "Biscuits", "Secs", snacks, null, true);
            categorieRepo.saveAll(Arrays.asList(sodas, eaux, biscuits));

            Produit lait = new Produit();
            lait.setCode("PRD001");
            lait.setNom("Lait 1L");
            lait.setDescription("Lait demi-écrémé");
            lait.setPrix(new BigDecimal("7.50"));
            lait.setStock(20);
            lait.setStockMinimal(5);
            lait.setDateExpiration(LocalDate.now().minusDays(2));
            lait.setCategorie(sodas);
            lait.setActif(true);

            Produit yaourt = new Produit();
            yaourt.setCode("PRD002");
            yaourt.setNom("Yaourt Danone");
            yaourt.setDescription("Yaourt nature");
            yaourt.setPrix(new BigDecimal("2.00"));
            yaourt.setStock(50);
            yaourt.setStockMinimal(10);
            yaourt.setDateExpiration(LocalDate.now().plusDays(3));
            yaourt.setCategorie(sodas);
            yaourt.setActif(true);

            Produit eau = new Produit();
            eau.setCode("PRD003");
            eau.setNom("Eau Sidi Ali 1.5L");
            eau.setDescription("Eau minérale");
            eau.setPrix(new BigDecimal("3.00"));
            eau.setStock(100);
            eau.setStockMinimal(20);
            eau.setDateExpiration(LocalDate.now().plusMonths(12));
            eau.setCategorie(eaux);
            eau.setActif(true);

            Produit biscuit = new Produit();
            biscuit.setCode("PRD004");
            biscuit.setNom("Biscuits Chocolat");
            biscuit.setDescription("Fourrés");
            biscuit.setPrix(new BigDecimal("5.50"));
            biscuit.setStock(15);
            biscuit.setStockMinimal(5);
            biscuit.setDateExpiration(LocalDate.now().plusMonths(6));
            biscuit.setCategorie(biscuits);
            biscuit.setActif(true);

            produitRepo.saveAll(Arrays.asList(lait, yaourt, eau, biscuit));

            // --- Ventes ---
            Vente venteToday = new Vente();
            venteToday.setCaissier("caissier1");
            venteToday.setMoyenPaiement(MoyenPaiement.ESPECES);
            venteToday.setDateVente(LocalDateTime.now());

            LigneVente lv1 = new LigneVente(null, venteToday, lait, 2, lait.getPrix(), null);
            LigneVente lv2 = new LigneVente(null, venteToday, yaourt, 3, yaourt.getPrix(), null);
            lv1.calculateSousTotal(); lv2.calculateSousTotal();
            venteToday.getLignes().addAll(Arrays.asList(lv1, lv2));
            venteToday.setTotal(lv1.getSousTotal().add(lv2.getSousTotal()));

            Vente venteYesterday = new Vente();
            venteYesterday.setCaissier("caissier2");
            venteYesterday.setMoyenPaiement(MoyenPaiement.CARTE_BANCAIRE);
            venteYesterday.setDateVente(LocalDateTime.now().minusDays(1));

            LigneVente lv3 = new LigneVente(null, venteYesterday, yaourt, 2, yaourt.getPrix(), null);
            LigneVente lv4 = new LigneVente(null, venteYesterday, eau, 4, eau.getPrix(), null);
            lv3.calculateSousTotal(); lv4.calculateSousTotal();
            venteYesterday.getLignes().addAll(Arrays.asList(lv3, lv4));
            venteYesterday.setTotal(lv3.getSousTotal().add(lv4.getSousTotal()));

            venteRepo.saveAll(Arrays.asList(venteToday, venteYesterday));

            System.out.println("✅ DONNÉES DE TEST AJOUTÉES AVEC SUCCÈS !");
        };
    }
}