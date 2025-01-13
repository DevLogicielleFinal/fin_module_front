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
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotController {

    @FXML
    private Button backButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button sendButton;

    @FXML
    private TextField emailField;

    @FXML
    private void handleBackButton() {
        // Logic to navigate to the Sign-Up page
        System.out.println("Back button clicked. Navigating to Sign-Up page.");
    }

    @FXML
    private void handleHomeButton(ActionEvent event) {
        try {
            // Charge le fichier forgotView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/registerView.fxml"));
            Parent root = loader.load();

            // Obtient la scène actuelle et remplace son contenu
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Regisster");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSendButton() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showAlert("Error", "Please enter an email address.", AlertType.ERROR);
        } else {
            // Simulate sending the reset email
            System.out.println("Sending reset email to: " + email);
            emailField.clear();
            showAlert("Success", "The email to reset your password has been sent!", AlertType.INFORMATION);
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