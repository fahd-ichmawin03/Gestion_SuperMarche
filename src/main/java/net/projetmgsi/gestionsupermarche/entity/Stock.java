package net.projetmgsi.gestionsupermarche.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mouvements_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeMouvement typeMouvement;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false)
    private Integer stockAvant;

    @Column(nullable = false)
    private Integer stockApres;

    @Column(nullable = false)
    private LocalDateTime dateMouvement;

    @Column(nullable = false)
    private String utilisateur;  // Qui a fait le mouvement

    @Column(length = 500)
    private String commentaire;

    @PrePersist
    protected void onCreate() {
        dateMouvement = LocalDateTime.now();

        // Calcul automatique du stock après
        if (typeMouvement == TypeMouvement.ENTREE || typeMouvement == TypeMouvement.RETOUR) {
            stockApres = stockAvant + quantite;
        } else if (typeMouvement == TypeMouvement.SORTIE || typeMouvement == TypeMouvement.VENTE) {
            stockApres = stockAvant - quantite;
        } else {
            stockApres = quantite; // CORRECTION ou INVENTAIRE
        }
    }
}