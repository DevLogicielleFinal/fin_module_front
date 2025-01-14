package com.front.app.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField inputUsername;

    @FXML
    private TextField inputPassword;

    @FXML
    private TextField inputConfirmPassword;

    @FXML
    private TextField inputEmail;


    public void goToLoginPage(ActionEvent event) {
        try {
            // Charge le fichier loginView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/loginView.fxml"));
            Parent root = loader.load();

            // Obtient la scène actuelle et remplace son contenu
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void registerClick(ActionEvent event) {
        String username = inputUsername.getText().trim();
        String password = inputPassword.getText().trim();
        String confirmPassword = inputConfirmPassword.getText().trim();
        String email = inputEmail.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            showAlert("Error", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
        } else if(!password.equals(confirmPassword)) {
            showAlert("Error", "Le mot de passe et sa confirmation doivent être identique.", Alert.AlertType.ERROR);
        } else {
            try {
                // Préparation des données JSON
                String jsonInput = String.format("{\"username\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}", username, email, password);

                // URL de l'API
                URL url = new URL("http://localhost:8080/users/register");

                // Connexion HTTP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                // Envoi des données
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                // Récupération de la réponse
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    showAlert("Success", "Utilisateur créé avec succès!", Alert.AlertType.INFORMATION);

                    // Efface les champs après le succès
                    inputUsername.clear();
                    inputPassword.clear();
                    inputConfirmPassword.clear();
                    inputEmail.clear();

                    goToLoginPage(event);
                } else {
                    showAlert("Error", "Échec de la création de l'utilisateur. Code : " + responseCode, Alert.AlertType.ERROR);
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Une erreur s'est produite lors de l'enregistrement.", Alert.AlertType.ERROR);
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