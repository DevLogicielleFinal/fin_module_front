package com.front.app.Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotController{

    @FXML
    private TextField emailField;

    @FXML
    private void goToMainPage(ActionEvent event) {
        try {
            // Charge le fichier mainView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/mainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Projet");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSendButton(ActionEvent event) {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showAlert("Error", "Veuillez entrez une adresse mail.", AlertType.ERROR);
        } else {
            // Simulate sending the reset email
            System.out.println("Sending reset email to: " + email);
            emailField.clear();
            showAlert("Success", "L'email pour mettre à jour votre mot de passe a été envoyer.", AlertType.INFORMATION);
            goToMainPage(event);

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