package org.geoexplore;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("GeoExplore");

        // Mostra la schermata iniziale di selezione utente
        showUserSelectionScreen();
    }

    private void showUserSelectionScreen() {
        // Layout per la schermata di selezione utente
        VBox layout = new VBox(20); // 20px di spaziatura tra i pulsanti

        // Messaggio di benvenuto
        Label welcomeMessage = new Label("Benvenuti nella città di Corridonia!");

        // Pulsanti per selezione del tipo di utente
        Button adminButton = new Button("Admin");
        Button userButton = new Button("Utente");
        Button visitorButton = new Button("Visitatore");

        // Eventi per ciascun tipo di utente
        adminButton.setOnAction(e -> showMainScreen("Admin"));
        userButton.setOnAction(e -> showMainScreen("Utente"));
        visitorButton.setOnAction(e -> showMainScreen("Visitatore"));

        layout.getChildren().addAll(welcomeMessage, adminButton, userButton, visitorButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMainScreen(String userType) {
        // Layout principale
        BorderPane layout = new BorderPane();

        // Messaggio di benvenuto
        Label welcomeLabel = new Label("Benvenuti nella città di Corridonia!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");
        layout.setTop(welcomeLabel);
        BorderPane.setAlignment(welcomeLabel, Pos.CENTER);

        // Contenuto principale
        VBox mainContent = new VBox(10);
        mainContent.setAlignment(Pos.CENTER);

        // Pulsante per mostrare i POI
        Button showPoiButton = new Button("Mostra Punti di Interesse");
        showPoiButton.setOnAction(e -> System.out.println("Punti di Interesse visualizzati per " + userType));
        mainContent.getChildren().add(showPoiButton);

        // Solo l'admin ha accesso al pulsante per aggiungere POI
        if ("Admin".equals(userType)) {
            Button addPoiButton = new Button("Aggiungi Punto di Interesse");
            addPoiButton.setOnAction(e -> System.out.println("Punto di Interesse aggiunto da Admin"));
            mainContent.getChildren().add(addPoiButton);
        }

        // Barra di navigazione inferiore sempre presente
        HBox navigationBar = new HBox();
        navigationBar.setAlignment(Pos.CENTER);
        navigationBar.setStyle("-fx-background-color: #CCCCCC; -fx-padding: 10px;");

        Button homeButton = new Button("Torna alla Home");
        homeButton.setOnAction(e -> showUserSelectionScreen());

        navigationBar.getChildren().add(homeButton);

        // Imposta il layout della scena
        layout.setCenter(mainContent);
        layout.setBottom(navigationBar);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
