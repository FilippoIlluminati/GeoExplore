// src/main/java/org/geoexplore/models/poi/PhysicalPOI.java
package org.geoexplore.models.poi;

public class PhysicalPOI extends InfoPoint {

    public PhysicalPOI(String nome, String descrizione, double latitudine, double longitudine) {
        super(nome, descrizione, latitudine, longitudine);
    }

    @Override
    public void mostraDettagli() {
        System.out.println("Punto Fisico: " + nome + " - " + descrizione +
                " (Latitudine: " + latitudine + ", Longitudine: " + longitudine + ")");
    }
}
