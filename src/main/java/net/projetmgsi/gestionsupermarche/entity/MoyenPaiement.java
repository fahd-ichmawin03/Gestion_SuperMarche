package net.projetmgsi.gestionsupermarche.entity;

public enum MoyenPaiement {
    ESPECES("Espèces"),
    CARTE_BANCAIRE("Carte bancaire"),
    CHEQUE("Chèque"),
    MOBILE_MONEY("Mobile Money");

    private final String label;

    MoyenPaiement(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}