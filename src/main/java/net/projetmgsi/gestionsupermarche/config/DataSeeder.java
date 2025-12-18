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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepo,
            RayonRepository rayonRepo,
            CategorieRepository categorieRepo,
            ProduitRepository produitRepo,
            VenteRepository venteRepo,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // INITIALISATION DES UTILISATEURS (Admin & Caissier)
            if (userRepo.count() == 0) {
                System.out.println("👤 CRÉATION DES UTILISATEURS PAR DÉFAUT...");

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("1234")); // Mdp: 1234
                admin.setRole(Role.ADMIN);

                User caissier = new User();
                caissier.setUsername("caissier1");
                caissier.setPassword(passwordEncoder.encode("1234")); // Mdp: 1234
                caissier.setRole(Role.CAISSIER);

                userRepo.saveAll(Arrays.asList(admin, caissier));
            }

            // INITIALISATION DU MAGASIN (Si pas de produits)
            if (produitRepo.count() > 0) {
                System.out.println("ℹ️ La base de données contient déjà des produits. Seeding annulé.");
                return;
            }

            System.out.println("🚀 INITIALISATION COMPLETE DU MAGASIN...");

            // Rayons
            Rayon r_frais = new Rayon(null, "Produits Frais", "Zone réfrigérée", "A", null, true);
            Rayon r_epicerie = new Rayon(null, "Épicerie", "Produits secs", "B", null, true);
            Rayon r_boissons = new Rayon(null, "Boissons", "Liquides", "C", null, true);
            Rayon r_hygiene = new Rayon(null, "Hygiène & Beauté", "Soins", "D", null, true);
            Rayon r_maison = new Rayon(null, "Maison & Entretien", "Nettoyage", "E", null, true);

            rayonRepo.saveAll(Arrays.asList(r_frais, r_epicerie, r_boissons, r_hygiene, r_maison));

            // Catégories
            // On garde les références pour lier les produits après
            Categorie c_legumes = new Categorie(null, "Fruits & Légumes", "Bio et conventionnel", r_frais, null, true);
            Categorie c_laitier = new Categorie(null, "Produits Laitiers", "Yaourts, laits", r_frais, null, true);
            Categorie c_pates = new Categorie(null, "Épicerie Salée", "Pâtes, riz", r_epicerie, null, true);
            Categorie c_biscuit = new Categorie(null, "Épicerie Sucrée", "Biscuits, chocolats", r_epicerie, null, true);
            Categorie c_sodas = new Categorie(null, "Jus & Sodas", "Boissons sucrées", r_boissons, null, true);
            Categorie c_eau = new Categorie(null, "Eaux", "Plates et gazeuses", r_boissons, null, true);
            Categorie c_shampoing = new Categorie(null, "Soins Capillaires", "Shampoings", r_hygiene, null, true);
            Categorie c_nettoyage = new Categorie(null, "Nettoyage Maison", "Sols, vitres", r_maison, null, true);

            List<Categorie> categories = Arrays.asList(
                    c_legumes, c_laitier, c_pates, c_biscuit,
                    c_sodas, c_eau, c_shampoing, c_nettoyage,
                    // Ajout d'autres catégories vides pour faire joli dans la liste
                    new Categorie(null, "Boucherie", "Viandes", r_frais, null, true),
                    new Categorie(null, "Poissonnerie", "Poissons", r_frais, null, true),
                    new Categorie(null, "Boulangerie", "Pains", r_epicerie, null, true),
                    new Categorie(null, "Bébé", "Couches", r_hygiene, null, true),
                    new Categorie(null, "Animalerie", "Croquettes", r_maison, null, true)
            );
            categorieRepo.saveAll(categories);

            //  Produits
            Produit p1 = createProduit("FR-001", "Pommes Golden (kg)", "Pommes locales", new BigDecimal("12.50"), 50, 10, LocalDate.now().plusDays(10), c_legumes);
            Produit p2 = createProduit("LT-001", "Lait UHT 1L", "Demi-écrémé", new BigDecimal("9.00"), 100, 20, LocalDate.now().plusMonths(3), c_laitier);
            Produit p3 = createProduit("EP-001", "Spaghetti 500g", "Pâtes blé dur", new BigDecimal("11.00"), 40, 5, LocalDate.now().plusMonths(12), c_pates);
            Produit p4 = createProduit("BS-001", "Coca-Cola 1.5L", "Soda gazeux", new BigDecimal("8.50"), 60, 10, LocalDate.now().plusMonths(6), c_sodas);
            Produit p5 = createProduit("HY-001", "Shampoing Doux", "Aux œufs", new BigDecimal("25.00"), 15, 3, LocalDate.now().plusYears(1), c_shampoing);
            Produit p6 = createProduit("MS-001", "Eau de Javel 1L", "Désinfectant", new BigDecimal("5.00"), 30, 5, LocalDate.now().plusYears(1), c_nettoyage);
            Produit p7 = createProduit("EP-002", "Chocolat Noir", "Tablette 100g", new BigDecimal("15.00"), 20, 5, LocalDate.now().plusMonths(8), c_biscuit);

            produitRepo.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7));

            // Ventes (Historique pour le Dashboard)
            System.out.println("📊 GÉNÉRATION DE VENTES FICTIVES...");

            // Vente 1 : Aujourd'hui (Paiement Espèces)
            Vente v1 = new Vente();
            v1.setCaissier("caissier1");
            v1.setMoyenPaiement(MoyenPaiement.ESPECES);
            v1.setDateVente(LocalDateTime.now()); // Aujourd'hui

            LigneVente lv1 = new LigneVente(null, v1, p2, 2, p2.getPrix(), null); // 2 Lait
            LigneVente lv2 = new LigneVente(null, v1, p4, 1, p4.getPrix(), null); // 1 Coca
            lv1.calculateSousTotal(); lv2.calculateSousTotal();

            v1.getLignes().addAll(Arrays.asList(lv1, lv2));
            v1.setTotal(lv1.getSousTotal().add(lv2.getSousTotal())); // Total calculé

            // Vente 2 :Hier (Paiement Carte)
            Vente v2 = new Vente();
            v2.setCaissier("caissier1");
            v2.setMoyenPaiement(MoyenPaiement.CARTE_BANCAIRE);
            v2.setDateVente(LocalDateTime.now().minusDays(1)); // Hier

            LigneVente lv3 = new LigneVente(null, v2, p5, 1, p5.getPrix(), null); // 1 Shampoing
            LigneVente lv4 = new LigneVente(null, v2, p7, 3, p7.getPrix(), null); // 3 Chocolats
            lv3.calculateSousTotal(); lv4.calculateSousTotal();

            v2.getLignes().addAll(Arrays.asList(lv3, lv4));
            v2.setTotal(lv3.getSousTotal().add(lv4.getSousTotal()));

            venteRepo.saveAll(Arrays.asList(v1, v2));

            System.out.println("✅ INITIALISATION TERMINÉE AVEC SUCCÈS !");
        };
    }

    // Méthode utilitaire pour créer un produit proprement
    private Produit createProduit(String code, String nom, String desc, BigDecimal prix, int stock, int min, LocalDate dateExp, Categorie cat) {
        Produit p = new Produit();
        p.setCode(code);
        p.setNom(nom);
        p.setDescription(desc);
        p.setPrix(prix);
        p.setStock(stock);
        p.setStockMinimal(min);
        p.setDateExpiration(dateExp);
        p.setCategorie(cat);
        p.setActif(true); //POUR LA SUPPRESSION LOGIQUE
        return p;
    }
}