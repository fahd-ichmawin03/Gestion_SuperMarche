package net.projetmgsi.gestionsupermarche.config;

import net.projetmgsi.gestionsupermarche.entity.*;
import net.projetmgsi.gestionsupermarche.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(RayonRepository rayonRepo,
                                      CategorieRepository categorieRepo,
                                      ProduitRepository produitRepo,
                                      VenteRepository venteRepo,
                                      LigneVenteRepository ligneVenteRepo) {

        return args -> {

            if (produitRepo.count() > 0) {
                System.out.println("ℹ️ Données déjà existantes.");
                return;
            }

            System.out.println("🚀 INITIALISATION DES DONNÉES");

            // ==== Rayons ====
            Rayon boissons = new Rayon(null, "Boissons", "Liquides", "A1", null, true);
            Rayon snacks = new Rayon(null, "Snacks", "Gâteaux", "B2", null, true);
            rayonRepo.saveAll(List.of(boissons, snacks));

            // ==== Catégories ====
            Categorie sodas = new Categorie(null, "Sodas", "Gazéifié", boissons, null, true);
            Categorie eaux = new Categorie(null, "Eaux", "Minérales", boissons, null, true);
            Categorie biscuits = new Categorie(null, "Biscuits", "Secs", snacks, null, true);
            categorieRepo.saveAll(List.of(sodas, eaux, biscuits));

            // ==== Produits ====
            Produit lait = new Produit(null, "PRD001", "Lait 1L", "Lait demi-écrémé",
                    new BigDecimal("7.50"), 20, 5, LocalDate.now().minusDays(2), sodas, true); // expiré
            Produit yaourt = new Produit(null, "PRD002", "Yaourt Danone", "Yaourt nature",
                    new BigDecimal("2.00"), 50, 10, LocalDate.now().plusDays(3), sodas, true); // bientôt expiré
            Produit eau = new Produit(null, "PRD003", "Eau Sidi Ali 1.5L", "Eau minérale",
                    new BigDecimal("3.00"), 100, 20, LocalDate.now().plusMonths(12), eaux, true); // OK
            Produit biscuit = new Produit(null, "PRD004", "Biscuits Chocolat", "Fourrés",
                    new BigDecimal("5.50"), 15, 5, LocalDate.now().minusDays(5), biscuits, true); // expiré
            produitRepo.saveAll(List.of(lait, yaourt, eau, biscuit));

            // ==== Ventes + LignesVente (dates proches pour graphe) ====

            // Vente aujourd'hui
            Vente venteToday = new Vente();
            venteToday.setCaissier("caissier1");
            venteToday.setMoyenPaiement(MoyenPaiement.ESPECES);
            venteToday.setDateVente(LocalDateTime.now());

            LigneVente lv1 = new LigneVente(null, venteToday, lait, 2, lait.getPrix(), null);
            LigneVente lv2 = new LigneVente(null, venteToday, yaourt, 3, yaourt.getPrix(), null);
            lv1.calculateSousTotal();
            lv2.calculateSousTotal();
            venteToday.getLignes().addAll(List.of(lv1, lv2));
            venteToday.setTotal(venteToday.getLignes().stream()
                    .map(LigneVente::getSousTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            // Vente hier
            Vente venteYesterday = new Vente();
            venteYesterday.setCaissier("caissier2");
            venteYesterday.setMoyenPaiement(MoyenPaiement.CARTE_BANCAIRE);
            venteYesterday.setDateVente(LocalDateTime.now().minusDays(1));

            LigneVente lv3 = new LigneVente(null, venteYesterday, yaourt, 2, yaourt.getPrix(), null);
            LigneVente lv4 = new LigneVente(null, venteYesterday, eau, 4, eau.getPrix(), null);
            lv3.calculateSousTotal();
            lv4.calculateSousTotal();
            venteYesterday.getLignes().addAll(List.of(lv3, lv4));
            venteYesterday.setTotal(venteYesterday.getLignes().stream()
                    .map(LigneVente::getSousTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            // Vente il y a 2 jours
            Vente vente2DaysAgo = new Vente();
            vente2DaysAgo.setCaissier("caissier1");
            vente2DaysAgo.setMoyenPaiement(MoyenPaiement.CARTE_BANCAIRE);
            vente2DaysAgo.setDateVente(LocalDateTime.now().minusDays(2));

            LigneVente lv5 = new LigneVente(null, vente2DaysAgo, biscuit, 1, biscuit.getPrix(), null);
            LigneVente lv6 = new LigneVente(null, vente2DaysAgo, lait, 1, lait.getPrix(), null);
            lv5.calculateSousTotal();
            lv6.calculateSousTotal();
            vente2DaysAgo.getLignes().addAll(List.of(lv5, lv6));
            vente2DaysAgo.setTotal(vente2DaysAgo.getLignes().stream()
                    .map(LigneVente::getSousTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            // Vente il y a 3 jours
            Vente vente3DaysAgo = new Vente();
            vente3DaysAgo.setCaissier("caissier2");
            vente3DaysAgo.setMoyenPaiement(MoyenPaiement.ESPECES);
            vente3DaysAgo.setDateVente(LocalDateTime.now().minusDays(3));

            LigneVente lv7 = new LigneVente(null, vente3DaysAgo, eau, 3, eau.getPrix(), null);
            LigneVente lv8 = new LigneVente(null, vente3DaysAgo, biscuit, 2, biscuit.getPrix(), null);
            lv7.calculateSousTotal();
            lv8.calculateSousTotal();
            vente3DaysAgo.getLignes().addAll(List.of(lv7, lv8));
            vente3DaysAgo.setTotal(vente3DaysAgo.getLignes().stream()
                    .map(LigneVente::getSousTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            // Enregistrement
            venteRepo.saveAll(List.of(venteToday, venteYesterday, vente2DaysAgo, vente3DaysAgo));

            System.out.println("✅ PRODUITS + VENTES AVEC LIGNES CRÉÉS");
        };
    }
}