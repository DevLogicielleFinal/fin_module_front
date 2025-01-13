package com.front.app.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;

public class ProjectController {

    @FXML
    private Button addButton;

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
    private void handleAddButton() {
        // Logic to navigate to the "Add Project" form
        System.out.println("Add button clicked. Redirecting to Add Project page.");
        statusLabel.setText("Redirecting to Add Project page...");
    }

    private void loadProjects() {
        // Dummy data for testing. Replace with actual data from the database.
        for (int i = 1; i <= 10; i++) {
            Label projectLabel = new Label("Project " + i + " - Description: Example project.");
            projectLabel.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-background-color: #f9f9f9;");
            projectList.getChildren().add(projectLabel);
        }
        statusLabel.setText("Projects loaded successfully.");
    }
}