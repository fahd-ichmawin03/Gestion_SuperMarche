package net.projetmgsi.gestionsupermarche.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private boolean lue = false;

    private LocalDateTime dateCreation = LocalDateTime.now();
}
