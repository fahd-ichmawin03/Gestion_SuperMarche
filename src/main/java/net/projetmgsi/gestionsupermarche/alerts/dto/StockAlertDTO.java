package net.projetmgsi.gestionsupermarche.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockAlertDTO {
    private String produit;
    private String typeAlerte;
    private Integer stockRestant;
}
