package com.front.app.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ProjectController{



    @FXML
    private VBox projectList;

    @FXML
    private ScrollPane projectScrollPane;

    @FXML
    private Label statusLabel;


    @FXML
    private void initialize() {
        // Initialize the project list
        loadProjects();
    }

    @FXML
    private void AddProjectButton(ActionEvent event) {
        try {
            // Charge le fichier addProjectView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/addProjectView.fxml"));
            Parent root = loader.load();

            // Obtient la scène actuelle et remplace son contenu
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add project");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProjects() {
        // Dummy data for testing. Replace with actual data from the database.
        for (int i = 1; i <= 10; i++) {
            Label projectLabel = new Label("Project " + i + " - Description: Example project.");
            projectLabel.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-background-color: #f9f9f9;");
            projectList.getChildren().add(projectLabel);
        }
    }
}