package net.projetmgsi.gestionsupermarche.service;

import net.projetmgsi.gestionsupermarche.entity.*;
import net.projetmgsi.gestionsupermarche.repository.*;
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

    public List<User> listerTousLesCaissiers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.CAISSIER).collect(Collectors.toList());
    }

    public User creerCaissier(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) throw new RuntimeException("Existe déjà");
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.CAISSIER);
        return userRepository.save(user);
    }

    public void supprimerCaissier(Long id) { userRepository.deleteById(id); }

    @Transactional
    public void approvisionnerStock(Long produitId, int quantite, String adminName) {
        Produit p = produitRepository.findById(produitId).orElseThrow();
        int avant = p.getStock();
        p.setStock(avant + quantite);
        produitRepository.save(p);

        Stock s = new Stock();
        s.setProduit(p);
        s.setTypeMouvement(TypeMouvement.ENTREE);
        s.setQuantite(quantite);
        s.setStockAvant(avant);
        s.setStockApres(p.getStock());
        s.setUtilisateur(adminName);
        s.setCommentaire("Approvisionnement Admin");
        stockRepository.save(s);
    }
}