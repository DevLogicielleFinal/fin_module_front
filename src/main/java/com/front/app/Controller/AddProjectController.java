package com.front.app.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.front.app.SessionManager;

import java.io.IOException;

public class AddProjectController{

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

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

    @FXML
    private void handleAddButton(ActionEvent event) {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();

        if (title.isEmpty() || description.isEmpty()) {
            showAlert("Error", "Le titre et la description sont requis.", AlertType.ERROR);
        } else {
            try {
                // Préparation des données JSON
                String jsonInput = String.format("{\"name\":\"%s\",\"description\":\"%s\"}", title, description);

                // URL de l'API
                URL url = new URL("http://localhost:8080/api/projects");

                // Connexion HTTP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");

                // Ajout du token JWT dans l'en-tête Authorization
                String jwtToken = SessionManager.getJwtToken();
                if (jwtToken == null || jwtToken.isEmpty()) {
                    showAlert("Error", "Token JWT manquant. Veuillez vous reconnecter.", AlertType.ERROR);
                    return;
                }
                connection.setRequestProperty("Authorization", "Bearer " + jwtToken);
                connection.setDoOutput(true);

                // Envoi des données
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                // Lecture de la réponse
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    showAlert("Success", "Projet ajouté avec succès!", AlertType.INFORMATION);

                    // Efface les champs après ajout
                    titleField.clear();
                    descriptionField.clear();

                    // Redirige vers la page des projets
                    goToProjectPage(event);
                } else {
                    showAlert("Error", "Échec de l'ajout du projet. Code : " + responseCode, AlertType.ERROR);
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Une erreur s'est produite lors de l'ajout du projet.", AlertType.ERROR);
            }
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