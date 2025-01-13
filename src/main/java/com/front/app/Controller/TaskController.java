package com.front.app.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

public class TaskController {



    @FXML
    private VBox taskList;

    @FXML
    private Label statusLabel;

    @FXML
    private ScrollPane taskScrollPane;

    @FXML
    private void initialize() {
        // Initialize the task list
        loadTasks();
    }

    @FXML
    private void AddTaskButton(ActionEvent event) {
            try {
                // Charge le fichier forgotView.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/addTaskView.fxml"));
                Parent root = loader.load();

                // Obtient la scène actuelle et remplace son contenu
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Add task");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void loadTasks() {
        // Dummy data for testing. Replace with actual data from the database.
        for (int i = 1; i <= 10; i++) {
            Label taskLabel = new Label("Task " + i + " - Description: Example Task.");
            taskLabel.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-background-color: #f9f9f9;");
            taskList.getChildren().add(taskLabel);
        }
        statusLabel.setText("Tasks loaded successfully.");
    }
}