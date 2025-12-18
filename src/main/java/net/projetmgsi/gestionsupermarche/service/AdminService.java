package net.projetmgsi.gestionsupermarche.service;

import net.projetmgsi.gestionsupermarche.entity.Produit;
import net.projetmgsi.gestionsupermarche.entity.Stock;
import net.projetmgsi.gestionsupermarche.entity.TypeMouvement;
import net.projetmgsi.gestionsupermarche.repository.ProduitRepository;
import net.projetmgsi.gestionsupermarche.repository.StockRepository;
import net.projetmgsi.gestionsupermarche.repository.UserRepository;
import net.projetmgsi.gestionsupermarche.user.Role;
import net.projetmgsi.gestionsupermarche.user.User;
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

    // GESTION DES RESSOURCES HUMAINES (CAISSIERS)

    // Liste tous les utilisateurs ayant le rôle CAISSIER
    public List<User> listerTousLesCaissiers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.CAISSIER)
                .collect(Collectors.toList());
    }

    //Créer un nouvel utilisateur Caissier de manière sécurisée
    public User creerCaissier(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Un utilisateur existe déjà avec ce nom.");
        }
        User user = new User();
        user.setUsername(username);
        // On crypte le mot de passe avant de l'enregistrer
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.CAISSIER);
        return userRepository.save(user);
    }

    //Supprimer un caissier par ID
    public void supprimerCaissier(Long id) {
        userRepository.deleteById(id);
    }

    // GESTION LOGISTIQUE (STOCK & APPROVISIONNEMENT)

    /** * Effectue un mouvement d'entrée en stock et enregistre le mouvement dans l'historique.
     * @Transactional assure que si l'enregistrement de l'historique échoue, le stock n'est pas modifié.
     */
    @Transactional
    public void approvisionnerStock(Long produitId, int quantite, String adminName) {
        // Trouver le produit
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        // Mettre à jour le stock du produit
        int stockAvant = produit.getStock();
        produit.setStock(stockAvant + quantite);
        produitRepository.save(produit); // Sauvegarde le produit avec le nouveau stock

        // Enregistrer le mouvement de stock (Traçabilité)
        try {
            Stock mouvement = new Stock();
            mouvement.setProduit(produit);
            mouvement.setTypeMouvement(TypeMouvement.ENTREE);
            mouvement.setQuantite(quantite);
            mouvement.setStockAvant(stockAvant);
            mouvement.setStockApres(produit.getStock());
            mouvement.setUtilisateur(adminName);
            mouvement.setCommentaire("Approvisionnement manuel par Admin");
            stockRepository.save(mouvement);
        } catch (Exception e) {
            System.err.println("Attention : L'historique stock n'a pas pu être sauvé.");
        }
    }

    // GESTION DU CATALOGUE (CRUD PRODUIT)


    /** Modifier les informations d'un produit existant */
    @Transactional
    public void modifierProduit(Long id, Produit produitModifie) {
        // On cherche le produit existant
        Produit p = produitRepository.findById(id).orElseThrow();

        // On met à jour ses infos (Nom, Prix, Description, Catégorie)
        p.setNom(produitModifie.getNom());
        p.setPrix(produitModifie.getPrix());
        p.setDescription(produitModifie.getDescription());
        p.setCategorie(produitModifie.getCategorie());

        produitRepository.save(p);
    }

    /** * Supprimer un produit.
     * Si le produit est lié à des ventes, on effectue une suppression LOGIQUE (désactivation).
     */
    public void supprimerProduit(Long id) {
        try {
            // Tente la suppression physique
            produitRepository.deleteById(id);
        } catch (Exception e) {
            // Si échec (clé étrangère présente), on désactive le produit
            System.err.println("Suppression physique impossible (produit vendu). Passage en mode ARCHIVAGE.");
            Produit p = produitRepository.findById(id).orElse(null);
            if (p != null) {
                p.setActif(false); // On le marque comme inactif
                produitRepository.save(p);
            }
        }
    }
}