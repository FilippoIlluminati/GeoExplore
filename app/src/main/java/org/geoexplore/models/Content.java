// src/main/java/org/geoexplore/models/Content.java
package org.geoexplore.models;

public class Content {
    private String tipo; // es. "testo", "immagine", "video"
    private String descrizione;
    private boolean approvato;

    public Content(String tipo, String descrizione) {
        this.tipo = tipo;
        this.descrizione = descrizione;
        this.approvato = false; // di default il contenuto non è approvato
    }

    public void visualizzaContenuto() {
        System.out.println("Tipo: " + tipo + ", Descrizione: " + descrizione + ", Approvato: " + approvato);
    }

    public void approvaContenuto() {
        this.approvato = true;
    }

    // Getter e Setter
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public boolean isApprovato() {
        return approvato;
    }

    public void setApprovato(boolean approvato) {
        this.approvato = approvato;
    }
}
