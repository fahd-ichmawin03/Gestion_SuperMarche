package net.projetmgsi.gestionsupermarche.repository;

import net.projetmgsi.gestionsupermarche.entity.Rayon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RayonRepository extends JpaRepository<Rayon, Long> {

    Optional<Rayon> findByNom(String nom);

    List<Rayon> findByActifTrue();

    List<Rayon> findByEmplacement(String emplacement);
}