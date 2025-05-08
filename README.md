GeoExplore

GeoExplore è un progetto che fornisce un sistema per la gestione di mappe interattive e l’inserimento di diversi contenuti geografici.

Funzionalità principali:
* Visualizzazione di una mappa con Point of Interest (POI)
* Creazione e gestione di itinerari (journeys)
* Inserimento e moderazione di contenuti (content) legati a POI e itinerari
* Gestione degli utenti con ruoli specifici

Ruoli utente:
1. Turista: utente non autenticato, utilizzo generico e sporadico dell’applicazione
2. Turista autenticato: utente registrato, può salvare preferiti e partecipare attivamente
3. Contributor: utente autenticato che può proporre nuovi POI e contenuti
4. Contributor autorizzato: contributor con permessi di pubblicazione immediata dei contenuti
5. Animatore: utente che organizza eventi e contest legati alla mappa
6. Curatore: utente con permessi per approvare o rifiutare itinerari e contenuti
7. Gestore della piattaforma: amministratore con accesso completo e supervisione di tutte le risorse

Architettura del progetto:
* Controller: espone le API REST per le operazioni su mappe, POI, itinerari, contenuti e utenti
* Service: contiene la logica di business e coordina le operazioni tra i repository
* Repository: interfaccia di accesso ai dati tramite JPA/Hibernate
* Model/Entity: classi che rappresentano le tabelle del database
* Sicurezza: configurazione di Spring Security per autenticazione e autorizzazione

Tecnologie:
* Java 17
* Spring Boot
* Spring MVC
* Spring Data JPA
* Spring Security

Design Patterns Utilizzati
* Singleton
* Factory Method / Strategy
* Facade
* Chain of Responsibility

