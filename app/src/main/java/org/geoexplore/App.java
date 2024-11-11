package org.geoexplore;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class App extends Application {

    private Stage primaryStage;
    private String userType = "";

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("GeoExplore - Benvenuti");

        showUserSelectionScreen();
    }

    private void showUserSelectionScreen() {
        // Layout per la selezione dell'utente
        BorderPane selectionLayout = new BorderPane();
        VBox centerLayout = new VBox(10);
        centerLayout.setAlignment(Pos.CENTER); // Allinea al centro verticalmente
        centerLayout.setStyle("-fx-padding: 20;");

        Label label = new Label("Seleziona il tipo di utente:");

        ToggleButton adminButton = new ToggleButton("Admin");
        ToggleButton userButton = new ToggleButton("Utente");
        ToggleButton visitorButton = new ToggleButton("Visitatore");

        ToggleGroup toggleGroup = new ToggleGroup();
        adminButton.setToggleGroup(toggleGroup);
        userButton.setToggleGroup(toggleGroup);
        visitorButton.setToggleGroup(toggleGroup);

        centerLayout.getChildren().addAll(label, adminButton, userButton, visitorButton);

        Button proceedButton = new Button("Procedi");
        proceedButton.setOnAction(e -> {
            if (adminButton.isSelected()) {
                userType = "Admin";
            } else if (userButton.isSelected()) {
                userType = "Utente";
            } else if (visitorButton.isSelected()) {
                userType = "Visitatore";
            }

            if (!userType.isEmpty()) {
                showMapScreen();
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Attenzione");
                alert.setHeaderText(null);
                alert.setContentText("Seleziona un tipo di utente per procedere.");
                alert.showAndWait();
            }
        });

        selectionLayout.setCenter(centerLayout); // Imposta layout centrato
        selectionLayout.setBottom(proceedButton); // Posiziona "Procedi" in basso

        BorderPane.setAlignment(proceedButton, Pos.CENTER); // Allinea "Procedi" al centro
        Scene scene = new Scene(selectionLayout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMapScreen() {
        // Layout principale
        BorderPane mapLayout = new BorderPane();

        // Inizializza WebView per la mappa
        WebView mapView = new WebView();
        WebEngine webEngine = mapView.getEngine();
        webEngine.load("https://www.openstreetmap.org/export/embed.html?bbox=13.5046%2C43.2480%2C13.5120%2C43.2520&layer=mapnik");


        mapLayout.setCenter(mapView);

        // Barra di navigazione inferiore
        HBox navbar = new HBox(10);
        navbar.setStyle("-fx-padding: 10; -fx-background-color: #dddddd;");
        navbar.setAlignment(Pos.CENTER); // Allinea la navbar al centro

        Button homeButton = new Button("Torna alla Home");
        homeButton.setOnAction(e -> showUserSelectionScreen());

        navbar.getChildren().add(homeButton);

        // Pulsante per aggiungere POI (solo per Admin)
        if ("Admin".equals(userType)) {
            Button addPOIButton = new Button("Aggiungi Punto di Interesse");
            addPOIButton.setOnAction(e -> addPointOfInterest());
            navbar.getChildren().add(addPOIButton);
        }

        mapLayout.setBottom(navbar);

        // Imposta la scena
        Scene mapScene = new Scene(mapLayout, 800, 600);
        primaryStage.setScene(mapScene);
        primaryStage.show();
    }

    private void addPointOfInterest() {
        // Logica per aggiungere un nuovo POI
        System.out.println("Aggiunta di un nuovo Punto di Interesse...");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Aggiungi POI");
        alert.setHeaderText(null);
        alert.setContentText("Funzionalità di aggiunta POI non ancora implementata.");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
