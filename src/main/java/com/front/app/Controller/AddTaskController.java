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

public class AddTaskController{

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private void goToTaskPage(ActionEvent event) {
        try {
            // Charge le fichier forgotView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/taskView.fxml"));
            Parent root = loader.load();

            // Obtient la scène actuelle et remplace son contenu
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Task");
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
            // Simulate adding the project to the database
            System.out.println("Task added: Title - " + title + ", Description - " + description);

            // Clear the fields after adding
            titleField.clear();
            descriptionField.clear();

            showAlert("Success", "Tâche ajouté avec succès!", AlertType.INFORMATION);
            goToTaskPage(event);
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