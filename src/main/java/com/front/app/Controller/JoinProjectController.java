package com.front.app.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JoinProjectController {

    @FXML
    private TextField codeProjectField;

    @FXML
    private void handleJointButton(ActionEvent event) {
        String projectId = codeProjectField.getText().trim();

        if (projectId.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un ID de projet.", AlertType.ERROR);
            return;
        }

        try {
            // Construire l'URL pour l'API
            String apiUrl = "http://localhost:8080/api/projects/" + projectId + "/assign";
            URL url = new URL(apiUrl);

            // Ouvrir une connexion HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Ajouter le token JWT à l'en-tête
            String jwtToken = com.front.app.SessionManager.getJwtToken();
            if (jwtToken != null) {
                connection.setRequestProperty("Authorization", "Bearer " + jwtToken);
            }

            // Envoyer une requête vide (sans payload spécifique)
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = "{}".getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Lire la réponse
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                showAlert("Succès", "Vous avez rejoint le projet avec succès.", AlertType.INFORMATION);
                goToProjectPage(event);
            } else {
                showAlert("Erreur", "Impossible de rejoindre le projet. Code : " + responseCode, AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la tentative de rejoindre le projet.", AlertType.ERROR);
        }
    }

    @FXML
    private void goToProjectPage(ActionEvent event) {
        try {
            // Charge le fichier forgotView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/projectView.fxml"));
            Parent root = loader.load();

            // Obtient la scène actuelle et remplace son contenu
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Projet");
        } catch (IOException e) {
            e.printStackTrace();
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
