// src/main/java/org/geoexplore/models/Valutazione.java
package org.geoexplore.models;

import java.util.Date;

public class Valutazione {
    private int punteggio; // Esempio: punteggio da 1 a 5
    private String autore; // L'autore della valutazione
    private Date data;

    public Valutazione(int punteggio, String autore) {
        this.punteggio = punteggio;
        this.autore = autore;
        this.data = new Date(); // Imposta la data corrente
    }

    public void visualizzaValutazione() {
        System.out.println("Punteggio: " + punteggio + ", Autore: " + autore + ", Data: " + data);
    }

    // Getter e Setter
    public int getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
