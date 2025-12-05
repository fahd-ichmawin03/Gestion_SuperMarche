package net.projetmgsi.gestionsupermarche.entity;

public enum TypeMouvement {
    ENTREE("Entrée en stock"),          // Réception de marchandise
    SORTIE("Sortie de stock"),          // Sortie manuelle
    VENTE("Vente"),                     // Sortie automatique lors d'une vente
    RETOUR("Retour client"),            // Retour de produit
    CORRECTION("Correction"),           // Correction d'inventaire
    INVENTAIRE("Inventaire"),           // Mise à jour lors d'un inventaire
    PERTE("Perte/Casse");              // Produit perdu ou cassé

    private final String label;

    TypeMouvement(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}