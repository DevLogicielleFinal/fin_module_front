package com.front.app.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML
    private Label welcomeText;

    @FXML
    private void handleHomeButton(ActionEvent event) {
        try {
            // Charge le fichier forgotView.fxml
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

    @FXML
    public void onButtonClick(ActionEvent event){
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

    public void goToForgotPage(ActionEvent event) {
        try {
            // Charge le fichier forgotView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/forgotView.fxml"));
            Parent root = loader.load();

            // Obtient la scène actuelle et remplace son contenu
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Mot de passe oublié");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}