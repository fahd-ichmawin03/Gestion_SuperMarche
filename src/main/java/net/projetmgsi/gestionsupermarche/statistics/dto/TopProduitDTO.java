package net.projetmgsi.gestionsupermarche.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopProduitDTO {
    private String nom;
    private Long quantiteVendue;
}
