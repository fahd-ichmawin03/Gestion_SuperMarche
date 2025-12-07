package net.projetmgsi.gestionsupermarche.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(length = 500)
    private String description;

    // --- MODIFICATION ICI : On ignore le Rayon pour éviter les erreurs Lazy ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rayon_id")
    @JsonIgnore
    private Rayon rayon;

    // --- MODIFICATION ICI : On ignore la liste pour éviter la boucle ---
    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Produit> produits = new ArrayList<>();

    @Column(nullable = false)
    private Boolean actif = true;
}