package net.projetmgsi.gestionsupermarche.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // <--- N'oubliez pas cet import
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "lignes_vente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- MODIFICATION ICI ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vente_id", nullable = false)
    @JsonIgnore  // <--- AJOUTEZ CECI POUR CASSER LA BOUCLE
    private Vente vente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sousTotal;

    @PrePersist
    @PreUpdate
    public void calculateSousTotal() {
        if (prixUnitaire != null && quantite != null) {
            sousTotal = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
        }
    }
}