package net.projetmgsi.gestionsupermarche.repository;

import net.projetmgsi.gestionsupermarche.entity.Notification;
import net.projetmgsi.gestionsupermarche.entity.NotificationType;
import net.projetmgsi.gestionsupermarche.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {


    List<Notification> findByLueFalseOrderByDateCreationDesc();
    long countByLueFalse();

    boolean existsByProduitAndTypeAndLueFalse(
            Produit produit,
            NotificationType type
    );



    boolean existsByMessageAndLueFalse(String message);

}