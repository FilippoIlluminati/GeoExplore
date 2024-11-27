package org.geoexplore;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.Optional;

public class App extends Application {

    private Stage primaryStage;
    private String userType = "";
    private WebEngine webEngine;

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
        webEngine = mapView.getEngine();
        // Carica il file HTML che contiene la mappa con il GeoJSON
        webEngine.load(getClass().getResource("/map.html").toExternalForm());

        // Aggiungi JavaScript per limitare la visualizzazione della mappa a Corridonia
        webEngine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                String script = """
                    var map = L.map('map').setView([43.2595, 13.4945], 14);
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    }).addTo(map);
                    var bounds = [[43.243757, 13.504804], [43.253252, 13.519481]];
                    map.setMaxBounds(bounds);
                    map.on('drag', function() {
                        map.panInsideBounds(bounds, { animate: true });
                    });
                """;
                webEngine.executeScript(script);
            }
        });

        mapLayout.setCenter(mapView);

        // Barra di navigazione inferiore
        HBox navbar = new HBox(10);
        navbar.setStyle("-fx-padding: 10; -fx-background-color: #dddddd;");
        navbar.setAlignment(Pos.CENTER); // Allinea la navbar al centro

        Button homeButton = new Button("Torna alla Home");
        homeButton.setOnAction(e -> showUserSelectionScreen());

        Button addPOIButton = new Button("Aggiungi POI");
        addPOIButton.setOnAction(e -> addPointOfInterest());

        navbar.getChildren().addAll(homeButton, addPOIButton);

        mapLayout.setBottom(navbar);

        // Imposta la scena
        Scene mapScene = new Scene(mapLayout, 800, 600);
        primaryStage.setScene(mapScene);
        primaryStage.show();
    }

    private void addPointOfInterest() {
        // Richiedi il nome del POI all'utente tramite una finestra di dialogo
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Aggiungi POI");
        dialog.setHeaderText("Inserisci il nome del Punto di Interesse");
        dialog.setContentText("Nome:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            // Esegui JavaScript per aggiungere un marker sulla mappa
            String script = "map.off('click');" +
                    "map.on('click', function(e) {" +
                    "    var lat = e.latlng.lat;" +
                    "    var lng = e.latlng.lng;" +
                    "    if (!window.poiAdded) {" +
                    "        L.marker([lat, lng]).addTo(map).bindPopup('" + name + "').openPopup();" +
                    "        window.poiAdded = true;" +
                    "    }" +
                    "});";
            webEngine.executeScript(script);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
