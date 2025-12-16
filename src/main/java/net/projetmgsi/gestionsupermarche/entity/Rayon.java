package net.projetmgsi.gestionsupermarche.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rayons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rayon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String emplacement;  // Emplacement physique dans le magasin

    @OneToMany(mappedBy = "rayon", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Categorie> categories = new ArrayList<>();

    @Column(nullable = false)
    private Boolean actif = true;
}