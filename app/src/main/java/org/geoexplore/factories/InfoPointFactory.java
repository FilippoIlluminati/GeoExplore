// src/main/java/org/geoexplore/factories/InfoPointFactory.java
package org.geoexplore.factories;

import org.geoexplore.models.poi.InfoPoint;
import org.geoexplore.models.poi.PhysicalPOI;
import org.geoexplore.models.poi.LogicalPOI;

public class InfoPointFactory {

    public static InfoPoint createInfoPoint(String tipo, String nome, String descrizione, double latitudine, double longitudine) {
        switch (tipo) {
            case "Physical":
                return new PhysicalPOI(nome, descrizione, latitudine, longitudine);
            case "Logical":
                return new LogicalPOI(nome, descrizione, latitudine, longitudine);
            default:
                throw new IllegalArgumentException("Tipo sconosciuto: " + tipo);
        }
    }
}
