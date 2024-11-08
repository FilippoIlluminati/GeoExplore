// src/main/java/org/geoexplore/models/poi/LogicalPOI.java
package org.geoexplore.models.poi;

public class LogicalPOI extends InfoPoint {

    public LogicalPOI(String nome, String descrizione, double latitudine, double longitudine) {
        super(nome, descrizione, latitudine, longitudine);
    }

    @Override
    public void mostraDettagli() {
        System.out.println("Punto Logico: " + nome + " - " + descrizione +
                " (Latitudine: " + latitudine + ", Longitudine: " + longitudine + ")");
    }
}
