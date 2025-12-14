package net.projetmgsi.gestionsupermarche.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

public enum NotificationType {
    STOCK,
    VENTE,
    STOCK_FAIBLE,
    RUPTURE,
    SYSTEME
}
