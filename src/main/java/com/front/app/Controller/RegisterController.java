package com.front.app.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

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
            showAlert("Error", "Tous les champs doivent être remplis. Le mot de passe et sa confirmation doivent être identique.", Alert.AlertType.ERROR);
        } else {
            // Simulate adding the project to the database
            System.out.println("Registration added: Username - " + username + ", Email - " + email);

            // Clear the fields after adding
            inputUsername.clear();
            inputPassword.clear();
            inputConfirmPassword.clear();
            inputEmail.clear();

            showAlert("Success", "Utilisateur crée avec succès!", Alert.AlertType.INFORMATION);
            goToLoginPage(event);
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