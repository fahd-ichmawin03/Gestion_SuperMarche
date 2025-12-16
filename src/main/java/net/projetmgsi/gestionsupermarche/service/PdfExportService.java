package net.projetmgsi.gestionsupermarche.service;


import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import net.projetmgsi.gestionsupermarche.statistics.dto.DashboardStatsDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfExportService {

    public byte[] exportDashboard(DashboardStatsDTO stats) {

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 12);

            document.add(new Paragraph("📊 Rapport Statistiques Supermarché", titleFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Chiffre d'affaires du jour : " + stats.getCaJour() + " DH", normalFont));
            document.add(new Paragraph("Chiffre d'affaires du mois : " + stats.getCaMois() + " DH", normalFont));
            document.add(new Paragraph("Chiffre d'affaires de l'année : " + stats.getCaAnnee() + " DH", normalFont));
            document.add(new Paragraph(" ", normalFont));

            document.add(new Paragraph("Nombre de ventes : " + stats.getNbVentes(), normalFont));
            document.add(new Paragraph("Clients servis : " + stats.getNbClients(), normalFont));
            document.add(new Paragraph("Panier moyen : " + stats.getPanierMoyen() + " DH", normalFont));
            document.add(new Paragraph("Produits en rupture : " + stats.getNbRupture(), normalFont));

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Erreur génération PDF", e);
        }

        return out.toByteArray();
    }
}
