package Geoexplore.Report;

// Stato di avanzamento della segnalazione
public enum ReportStatus {
    IN_ATTESA,  // Segnalazione ancora da gestire
    IGNORATO,   // Segnalazione ignorata dal gestore
    RISOLTO     // Segnalazione presa in carico e conclusa
}
