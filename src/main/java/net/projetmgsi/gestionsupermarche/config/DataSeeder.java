package net.projetmgsi.gestionsupermarche.config;

import net.projetmgsi.gestionsupermarche.entity.Categorie;
import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.entity.Rayon;
import net.projetmgsi.gestionsupermarche.repository.CategorieRepository;
import net.projetmgsi.gestionsupermarche.repository.ProduitRepository;
import net.projetmgsi.gestionsupermarche.repository.RayonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(RayonRepository rayonRepo,
                                      CategorieRepository categorieRepo,
                                      ProduitRepository produitRepo) {
        return args -> {
            // Vérifie si la table produits est vide
            if (produitRepo.count() == 0)
            {
                System.out.println(" DÉMARRAGE DE L'INITIALISATION DES DONNÉES");
                // 1. Rayons
                Rayon r1 = new Rayon(null, "Boissons", "Liquides", "A1", null, true);
                Rayon r2 = new Rayon(null, "Snacks", "Gâteaux", "B2", null, true);
                rayonRepo.saveAll(Arrays.asList(r1, r2));

                // 2. Catégories
                Categorie c1 = new Categorie(null, "Sodas", "Gazéifié", r1, null, true);
                Categorie c2 = new Categorie(null, "Eaux", "Naturelle", r1, null, true);
                Categorie c3 = new Categorie(null, "Biscuits", "Secs", r2, null, true);
                categorieRepo.saveAll(Arrays.asList(c1, c2, c3));

                // 3. Produits
                Produit p1 = new Produit(null, "1111", "Coca Cola", "1.5L", new BigDecimal("12.50"), 100, c1, true);
                Produit p2 = new Produit(null, "2222", "Sidi Ali", "1.5L", new BigDecimal("5.00"), 200, c2, true);
                Produit p3 = new Produit(null, "3333", "Oreo", "Paquet", new BigDecimal("8.00"), 50, c3, true);

                produitRepo.saveAll(Arrays.asList(p1, p2, p3));

                System.out.println("✅ 3 PRODUITS AJOUTÉS : CODES 1111, 2222, 3333");
            } else {
                System.out.println("ℹ️ La base de données contient déjà des produits.");
            }
        };
    }
}