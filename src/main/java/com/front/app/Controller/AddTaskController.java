package com.front.app.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AddTaskController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private ComboBox<String> usersComboBox;

    private String projectId;
    private boolean isInitialized = false;

    public void setProjectId(String projectId) {
        this.projectId = projectId;
        System.out.println("Set Project Id: " + projectId);

        if (!isInitialized) {
            // Appeler la méthode de chargement après l'initialisation
            loadUsersForProject();
            isInitialized = true;
        }
    }

    // Map pour associer le nom d'utilisateur à son ID
    private final Map<String, String> userIdMap = new HashMap<>();



    @FXML
    private void initialize() {
        initializeStatusComboBox();
    }

    private void initializeStatusComboBox() {
        ObservableList<String> statuses = FXCollections.observableArrayList("TO_DO", "IN_PROGRESS", "DONE");
        statusComboBox.setItems(statuses);
    }

    private void loadUsersForProject() {
        try {
            // URL de l'API pour récupérer les utilisateurs du projet
            URL url = new URL("http://localhost:8080/api/projects/" + projectId + "/users");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            System.out.println("Project Id: " + projectId);

            // Ajouter le token JWT si nécessaire
            String jwtToken = com.front.app.SessionManager.getJwtToken();
            if (jwtToken != null) {
                connection.setRequestProperty("Authorization", "Bearer " + jwtToken);
            }

            // Vérifier la réponse de l'API
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line.trim());
                }

                // Parser et ajouter les utilisateurs à la ComboBox
                parseUsersFromJson(response.toString());
                usersComboBox.setItems(FXCollections.observableArrayList(userIdMap.keySet()));
            } else {
                showAlert("Erreur", "Impossible de charger les utilisateurs. Code : " + responseCode, AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement des utilisateurs.", AlertType.ERROR);
        }
    }

    private void parseUsersFromJson(String jsonResponse) {
        jsonResponse = jsonResponse.replace("[", "").replace("]", "");
        String[] userItems = jsonResponse.split("\\},\\{");

        userIdMap.clear(); // Nettoyer les anciennes données
        for (String userItem : userItems) {
            String userId = extractValue(userItem, "id").replaceAll("[^0-9]", ""); // Supprimer les caractères non numériques
            String userName = extractValue(userItem, "username");
            if (!userId.isEmpty() && !userName.isEmpty()) {
                userIdMap.put(userName, userId);
            }
        }
    }

    private String extractValue(String json, String key) {
        String keyPattern = "\"" + key + "\":\"";
        int startIndex = json.indexOf(keyPattern) + keyPattern.length();
        int endIndex = json.indexOf("\"", startIndex);
        return startIndex > -1 && endIndex > -1 ? json.substring(startIndex, endIndex) : "";
    }

    @FXML
    private void goToTaskPage(ActionEvent event) {
        try {
            // Charge le fichier taskView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/taskView.fxml"));
            Parent root = loader.load();

            // Obtient la scène actuelle et remplace son contenu
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Task");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddButton(ActionEvent event) {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String state = statusComboBox.getValue();
        String selectedUser = usersComboBox.getValue();

        if (title.isEmpty() || description.isEmpty() || state == null || selectedUser == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis.", AlertType.ERROR);
            return;
        }

        // Récupérer l'ID utilisateur depuis `usersComboBox`
        String userIdString = userIdMap.get(selectedUser);
        if (userIdString == null || userIdString.isEmpty()) {
            showAlert("Erreur", "Utilisateur invalide ou non sélectionné.", AlertType.ERROR);
            return;
        }
        System.out.println("userIdString: " + userIdString);
        System.out.println("userIdMap: " + userIdMap);
        int userId;
        try {
            userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID utilisateur est invalide.", AlertType.ERROR);
            return;
        }

        try {
            // Construire l'URL de l'API
            String apiUrl = "http://localhost:8080/api/projects/" + projectId + "/tasks";

            // Préparer la requête JSON
            String jsonPayload = String.format(
                    "{\"description\":\"%s\", \"state\":\"%s\", \"userId\":%s}",
                    description, state, userId
            );

            System.out.println(jsonPayload);

            // Connexion HTTP
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Ajouter le token JWT
            String jwtToken = com.front.app.SessionManager.getJwtToken();
            if (jwtToken != null) {
                connection.setRequestProperty("Authorization", "Bearer " + jwtToken);
            }

            // Envoyer les données JSON
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Lire la réponse
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                showAlert("Succès", "Tâche ajoutée avec succès.", AlertType.INFORMATION);
                goToTaskPage(event);
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line.trim());
                }
                showAlert("Erreur", "Erreur lors de l'ajout de la tâche : " + response, AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de la tâche.", AlertType.ERROR);
        }
    }



    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
