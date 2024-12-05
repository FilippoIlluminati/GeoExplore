package org.geoexplore;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

public class App extends Application {

    private Stage primaryStage;
    private String userType = "";
    private WebEngine webEngine;
    private static final String POI_FILE = "data/pois.json";
    private static final String TOURIST_REGISTRATION_FILE = "data/tourist_emails.json";

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("GeoExplore - Benvenuti");

        // Inizializza il file POI se non esiste
        initializePoiFile();
        initializeTouristRegistrationFile();

        showUserSelectionScreen();
    }

    private void initializePoiFile() {
        File file = new File(POI_FILE);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs(); // Crea la cartella se non esiste
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]"); // Inizializza con un array JSON vuoto
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeTouristRegistrationFile() {
        File file = new File(TOURIST_REGISTRATION_FILE);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs(); // Crea la cartella se non esiste
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]"); // Inizializza con un array JSON vuoto
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showUserSelectionScreen() {
        BorderPane selectionLayout = new BorderPane();
        VBox centerLayout = new VBox(10);
        centerLayout.setAlignment(Pos.CENTER);
        centerLayout.setStyle("-fx-padding: 20;");

        Label label = new Label("Seleziona il tipo di utente:");

        ToggleButton curatorButton = new ToggleButton("Curatore");
        ToggleButton animatorButton = new ToggleButton("Animatore");
        ToggleButton contributorButton = new ToggleButton("Contributor");
        ToggleButton touristButton = new ToggleButton("Turista");

        ToggleGroup toggleGroup = new ToggleGroup();
        curatorButton.setToggleGroup(toggleGroup);
        animatorButton.setToggleGroup(toggleGroup);
        contributorButton.setToggleGroup(toggleGroup);
        touristButton.setToggleGroup(toggleGroup);

        centerLayout.getChildren().addAll(label, curatorButton, animatorButton, contributorButton, touristButton);

        Button proceedButton = new Button("Procedi");
        proceedButton.setOnAction(e -> {
            if (curatorButton.isSelected()) {
                userType = "Curatore";
            } else if (animatorButton.isSelected()) {
                userType = "Animatore";
            } else if (contributorButton.isSelected()) {
                userType = "Contributor";
            } else if (touristButton.isSelected()) {
                userType = "Turista";
            }

            if (!userType.isEmpty()) {
                if ("Turista".equals(userType)) {
                    showTouristAccessScreen();
                } else {
                    showMapScreen();
                }
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Attenzione");
                alert.setHeaderText(null);
                alert.setContentText("Seleziona un tipo di utente per procedere.");
                alert.showAndWait();
            }
        });

        selectionLayout.setCenter(centerLayout);
        selectionLayout.setBottom(proceedButton);
        BorderPane.setAlignment(proceedButton, Pos.CENTER);

        Scene scene = new Scene(selectionLayout, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showTouristAccessScreen() {
        VBox touristLayout = new VBox(10);
        touristLayout.setAlignment(Pos.CENTER);
        touristLayout.setStyle("-fx-padding: 20;");

        Label emailLabel = new Label("Inserisci la tua email per accedere:");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        Button accessButton = new Button("Accedi");
        accessButton.setOnAction(e -> {
            String email = emailField.getText();
            if (email != null && !email.isEmpty()) {
                if (isRegisteredEmail(email)) {
                    showMapScreen();
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Attenzione");
                    alert.setHeaderText(null);
                    alert.setContentText("Email non registrata. Per favore registrati prima di accedere.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Attenzione");
                alert.setHeaderText(null);
                alert.setContentText("Inserisci un'email valida per accedere.");
                alert.showAndWait();
            }
        });

        Label continueWithoutLoginLabel = new Label("Continua senza accedere");
        continueWithoutLoginLabel.setStyle("-fx-underline: true; -fx-text-fill: blue;");
        continueWithoutLoginLabel.setOnMouseClicked(e -> showMapScreen());

        Label registerLabel = new Label("Registrati");
        registerLabel.setStyle("-fx-underline: true; -fx-text-fill: blue;");
        registerLabel.setOnMouseClicked(e -> registerTourist(emailField.getText()));

        touristLayout.getChildren().addAll(emailLabel, emailField, accessButton, continueWithoutLoginLabel, registerLabel);

        Scene touristScene = new Scene(touristLayout, 400, 300);
        primaryStage.setScene(touristScene);
        primaryStage.show();
    }

    private boolean isRegisteredEmail(String email) {
        try {
            File touristFile = new File(TOURIST_REGISTRATION_FILE);
            if (touristFile.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(TOURIST_REGISTRATION_FILE)));
                JSONArray tourists = new JSONArray(content);
                for (int i = 0; i < tourists.length(); i++) {
                    if (tourists.getString(i).equals(email)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void registerTourist(String email) {
        if (email == null || email.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Attenzione");
            alert.setHeaderText(null);
            alert.setContentText("Inserisci un'email valida per registrarti.");
            alert.showAndWait();
            return;
        }

        try {
            JSONArray tourists;
            File touristFile = new File(TOURIST_REGISTRATION_FILE);
            if (touristFile.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(TOURIST_REGISTRATION_FILE)));
                tourists = new JSONArray(content);
            } else {
                tourists = new JSONArray();
            }

            boolean exists = false;
            for (int i = 0; i < tourists.length(); i++) {
                if (tourists.getString(i).equals(email)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                tourists.put(email);
                try (FileWriter file = new FileWriter(TOURIST_REGISTRATION_FILE)) {
                    file.write(tourists.toString(4)); // Salva il file con indentazione
                }
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Registrazione Completata");
                alert.setHeaderText(null);
                alert.setContentText("Registrazione avvenuta con successo.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Attenzione");
                alert.setHeaderText(null);
                alert.setContentText("Email già registrata.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMapScreen() {
        BorderPane mapLayout = new BorderPane();

        WebView mapView = new WebView();
        webEngine = mapView.getEngine();
        webEngine.load(getClass().getResource("/map.html").toExternalForm());

        webEngine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                String script = """
                    if (typeof map === 'undefined') {
                        var map = L.map('map').setView([43.2595, 13.4945], 14);
                        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                            attribution: 'Map data &copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors'
                        }).addTo(map);
                        var bounds = [[43.243757, 13.504804], [43.253252, 13.519481]];
                        map.setMaxBounds(bounds);
                        map.on('drag', function() {
                            map.panInsideBounds(bounds, { animate: true });
                        });
                    }
                """;
                webEngine.executeScript(script);
                loadPOIs();
            }
        });

        mapLayout.setCenter(mapView);

        HBox navbar = new HBox(10);
        navbar.setStyle("-fx-padding: 10; -fx-background-color: #dddddd;");
        navbar.setAlignment(Pos.CENTER);

        Button homeButton = new Button("Torna alla Home");
        homeButton.setOnAction(e -> showUserSelectionScreen());

        if ("Curatore".equals(userType)) {
            Button addPOIButton = new Button("Aggiungi POI");
            addPOIButton.setOnAction(e -> addPointOfInterest());
            navbar.getChildren().add(addPOIButton);
        }

        navbar.getChildren().add(homeButton);
        mapLayout.setBottom(navbar);

        Scene mapScene = new Scene(mapLayout, 800, 600);
        primaryStage.setScene(mapScene);
        primaryStage.show();
    }

    private void addPointOfInterest() {
        if (!"Curatore".equals(userType)) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Permesso Negato");
            alert.setHeaderText(null);
            alert.setContentText("Solo il curatore può aggiungere punti di interesse.");
            alert.showAndWait();
            return;
        }

        TextInputDialog poiDialog = new TextInputDialog();
        poiDialog.setTitle("Aggiungi POI");
        poiDialog.setHeaderText("Inserisci le informazioni del Punto di Interesse");
        poiDialog.setContentText("Nome:");

        Optional<String> result = poiDialog.showAndWait();
        result.ifPresent(nome -> {
            String script = """
                map.once('click', function(e) {
                    var lat = e.latlng.lat;
                    var lng = e.latlng.lng;
                    window.poi = { nome: '%s', latitudine: lat, longitudine: lng };
                    L.marker([lat, lng]).addTo(map).bindPopup('<b>' + '%s' + '</b>').openPopup();
                    alert(lat + ',' + lng);
                });
            """.formatted(nome, nome);
            webEngine.executeScript(script);

            webEngine.setOnAlert(event -> {
                String[] coordinates = event.getData().split(",");
                if (coordinates.length == 2) {
                    double lat = Double.parseDouble(coordinates[0]);
                    double lng = Double.parseDouble(coordinates[1]);
                    savePOI(nome, lat, lng);
                }
            });

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("POI Aggiunto");
            alert.setHeaderText(null);
            alert.setContentText("Clicca sulla mappa per selezionare la posizione del POI.");
            alert.showAndWait();
        });
    }

    private void savePOI(String nome, double lat, double lng) {
        try {
            JSONArray pois;
            File poiFile = new File(POI_FILE);
            if (poiFile.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(POI_FILE)));
                pois = new JSONArray(content);
            } else {
                pois = new JSONArray();
            }

            boolean exists = false;
            for (int i = 0; i < pois.length(); i++) {
                JSONObject poi = pois.getJSONObject(i);
                if (poi.getString("nome").equals(nome) &&
                        poi.getDouble("latitudine") == lat &&
                        poi.getDouble("longitudine") == lng) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                JSONObject poi = new JSONObject();
                poi.put("nome", nome);
                poi.put("latitudine", lat);
                poi.put("longitudine", lng);
                pois.put(poi);
            }

            try (FileWriter file = new FileWriter(POI_FILE)) {
                file.write(pois.toString(4)); // Salva il file con indentazione
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPOIs() {
        try {
            File poiFile = new File(POI_FILE);
            if (poiFile.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(POI_FILE)));
                JSONArray pois = new JSONArray(content);

                for (int i = 0; i < pois.length(); i++) {
                    JSONObject poi = pois.getJSONObject(i);
                    String nome = poi.getString("nome");
                    double lat = poi.getDouble("latitudine");
                    double lng = poi.getDouble("longitudine");
                    String script = "L.marker([" + lat + ", " + lng + "]).addTo(map).bindPopup('<b>' + '" + nome + "' + '</b>').openPopup();";
                    webEngine.executeScript(script);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}