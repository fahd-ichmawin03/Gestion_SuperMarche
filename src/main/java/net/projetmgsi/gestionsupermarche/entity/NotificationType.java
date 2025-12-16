package net.projetmgsi.gestionsupermarche.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

public enum NotificationType {
    STOCK_FAIBLE,
    RUPTURE,
    EXPIRATION
}
