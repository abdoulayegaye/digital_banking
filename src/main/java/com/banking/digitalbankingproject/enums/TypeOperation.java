package com.banking.digitalbankingproject.enums;

/**
 * Énumération représentant les types d'opérations bancaires.
 */
public enum TypeOperation {
    DEPOT("Dépôt"),       // Opération de dépôt
    RETRAIT("Retrait");   // Opération de retrait

    private final String label; // Libellé de l'opération

    /**
     * Constructeur de l'énumération.
     *
     * @param label Le libellé de l'opération.
     */
    TypeOperation(String label) {
        this.label = label;
    }

    /**
     * Retourne le libellé de l'opération.
     *
     * @return Le libellé de l'opération.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Retourne l'énumération correspondant au libellé donné.
     *
     * @param label Le libellé de l'opération.
     * @return L'énumération correspondante, ou null si non trouvée.
     */
    public static TypeOperation fromLabel(String label) {
        for (TypeOperation type : TypeOperation.values()) {
            if (type.getLabel().equals(label)) {
                return type;
            }
        }
        return null; // Retourne null si le libellé n'est pas trouvé
    }
}