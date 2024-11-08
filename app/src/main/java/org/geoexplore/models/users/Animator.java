// src/main/java/org/geoexplore/models/users/Animator.java
package org.geoexplore.models.users;

import org.geoexplore.models.Content;

public class Animator extends User {

    public Animator(String nomeUtente, String email) {
        super(nomeUtente, email);
    }

    @Override
    public void interagisci() {
        System.out.println("L'animatore " + nomeUtente + " sta proponendo un nuovo contest.");
    }

    public void proponiContest(String tema) {
        System.out.println(nomeUtente + " ha proposto un nuovo contest con tema: " + tema);
    }

    public void validaContest(Content content) {
        System.out.println("Il contenuto del contest è stato validato da " + nomeUtente);
    }
}
