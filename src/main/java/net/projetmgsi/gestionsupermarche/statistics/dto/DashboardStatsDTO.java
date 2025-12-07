package net.projetmgsi.gestionsupermarche.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.projetmgsi.gestionsupermarche.repository.ProduitRepository;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Double  caJour;
    private Double  caMois;
    private Double  caAnnee;
    private Double  stockFaible;
    private Integer nbVentes;
    private Integer nbClients;
    private Double panierMoyen;
    private Integer nbRupture;

}


