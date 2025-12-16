package net.projetmgsi.gestionsupermarche.service;

import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.entity.Stock;
import net.projetmgsi.gestionsupermarche.entity.TypeMouvement;
import net.projetmgsi.gestionsupermarche.repository.ProduitRepository;
import net.projetmgsi.gestionsupermarche.repository.StockRepository;
import net.projetmgsi.gestionsupermarche.repository.UserRepository;
import net.projetmgsi.gestionsupermarche.user.Role;
import net.projetmgsi.gestionsupermarche.user.User; // Utilise l'entité User pour les caissiers
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired private UserRepository userRepository;
    @Autowired private ProduitRepository produitRepository;
    @Autowired private StockRepository stockRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // --- GESTION CAISSIERS ---

    /** Liste tous les utilisateurs ayant le rôle CAISSIER */
    public List<User> listerTousLesCaissiers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.CAISSIER)
                .collect(Collectors.toList());
    }

    /** Créer un nouvel utilisateur Caissier */
    public User creerCaissier(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Un utilisateur existe déjà avec ce nom.");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.CAISSIER); // IMPORTANT : Lui donner le bon rôle
        return userRepository.save(user);
    }

    /** Supprimer un caissier par ID */
    public void supprimerCaissier(Long id) {
        userRepository.deleteById(id);
    }

    // --- GESTION STOCK (APPROVISIONNEMENT) ---

    /** Effectue un mouvement d'entrée en stock et enregistre le mouvement. */
    @Transactional
    public void approvisionnerStock(Long produitId, int quantite, String adminName) {
        // 1. Trouver le produit
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        // 2. Mettre à jour le stock du produit
        int stockAvant = produit.getStock();
        produit.setStock(stockAvant + quantite);
        produitRepository.save(produit); // Sauvegarde le produit avec le nouveau stock

        // 3. Enregistrer le mouvement de stock
        Stock mouvement = new Stock();
        mouvement.setProduit(produit);
        mouvement.setTypeMouvement(TypeMouvement.ENTREE);
        mouvement.setQuantite(quantite);
        mouvement.setStockAvant(stockAvant);
        mouvement.setStockApres(produit.getStock());
        mouvement.setUtilisateur(adminName);
        mouvement.setCommentaire("Approvisionnement manuel par Admin");
        stockRepository.save(mouvement);
    }
}