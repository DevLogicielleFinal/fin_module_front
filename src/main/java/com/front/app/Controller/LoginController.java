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
            // Simulate adding the project to the database
            System.out.println("Connexion confirmer: Email - " + email);

            // Clear the fields after adding
            inputPassword.clear();
            inputEmail.clear();

            showAlert("Success", "Connexion établie!", Alert.AlertType.INFORMATION);
            goToProjectPage(event);
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