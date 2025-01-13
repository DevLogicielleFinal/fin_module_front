package com.front.app.Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {



    @FXML
    private void goToLoginPage(ActionEvent event) {
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
    private void goToRegisterPage(ActionEvent event) {
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
}
