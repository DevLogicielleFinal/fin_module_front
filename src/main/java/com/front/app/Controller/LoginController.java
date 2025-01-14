package com.front.app.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import com.front.app.SessionManager;

public class LoginController{

    @FXML
    private TextField inputPassword;

    @FXML
    private TextField inputEmail;

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
    private void validFormClick(ActionEvent event) {
        String password = inputPassword.getText().trim();
        String email = inputEmail.getText().trim();

        if (password.isEmpty() || email.isEmpty()) {
            showAlert("Error", "L'email et le mot de passe sont requis.", Alert.AlertType.ERROR);
        } else {
            try {
                // Préparation des données JSON
                String jsonInput = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);

                // URL de l'API
                URL url = new URL("http://localhost:8080/auth/login");

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

                // Lecture de la réponse
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Récupérer le token JWT depuis la réponse
                    StringBuilder response = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                    }

                    // Extraire le token du JSON
                    String jwtToken = response.toString();


                    // Stocker le token dans le SessionManager
                    SessionManager.setJwtToken(jwtToken);
                    System.out.println("Token:" + jwtToken);

                    showAlert("Success", "Connexion réussie !", Alert.AlertType.INFORMATION);

                    // Effacer les champs après connexion
                    inputPassword.clear();
                    inputEmail.clear();

                    // Rediriger vers la page des projets
                    goToProjectPage(event);
                } else {
                    showAlert("Error", "Échec de la connexion. Code : " + responseCode, Alert.AlertType.ERROR);
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Une erreur s'est produite lors de la connexion.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void goToForgotPage(ActionEvent event){
        try {
            // Charge le fichier forgotView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/forgotView.fxml"));
            Parent root = loader.load();

            // Obtient la scène actuelle et remplace son contenu
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Forgot password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToRegisterPage(ActionEvent event){
        try {
            // Charge le fichier registerView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/registerView.fxml"));
            Parent root = loader.load();

            // Obtient la scène actuelle et remplace son contenu
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}