package com.front.app.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javafx.scene.control.Button;

import com.front.app.SessionManager;

import java.io.IOException;

public class TaskController {



    @FXML
    private VBox taskList;

    @FXML
    private Label statusLabel;

    @FXML
    private ScrollPane taskScrollPane;
    private boolean isInitialized = false;
    private String projectId;

    public void setProjectId(String projectId) {
        this.projectId = projectId;
        System.out.println("Set Project Id: " + projectId);

        if (!isInitialized) {
            // Appeler la méthode de chargement après l'initialisation
            loadTasks();
            isInitialized = true;
        }
    }

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
    private void AddTaskButton(ActionEvent event) {
            try {
                // Charge le fichier forgotView.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/addTaskView.fxml"));
                Parent root = loader.load();

                AddTaskController addTaskController = loader.getController();
                addTaskController.setProjectId(projectId);

                // Obtient la scène actuelle et remplace son contenu
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Add task");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void modifyTask(String taskId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/editTaskView.fxml"));
            Parent root = loader.load();

            // Passer l'ID de la tâche au contrôleur de la vue d'édition
            EditTaskController editTaskController = loader.getController();
            editTaskController.setTaskId(taskId);

            Stage stage = (Stage) taskList.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier la tâche");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue de modification.", Alert.AlertType.ERROR);
        }
    }

    private void loadTasks() {
        if (projectId == null || projectId.isEmpty()) {
            statusLabel.setText("Erreur : Aucun ID de projet fourni.");
            return;
        }

        try {
            URL url = new URL("http://localhost:8080/api/projects/" + projectId + "/tasks");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            String jwtToken = SessionManager.getJwtToken();
            if (jwtToken == null || jwtToken.isEmpty()) {
                statusLabel.setText("Erreur : Token JWT manquant. Veuillez vous reconnecter.");
                return;
            }
            connection.setRequestProperty("Authorization", "Bearer " + jwtToken);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                displayTasks(response.toString());
                statusLabel.setText("Tâches chargées avec succès !");
            } else {
                statusLabel.setText("Erreur : Impossible de charger les tâches. Code : " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erreur : Une erreur s'est produite lors du chargement des tâches.");
        }
    }

    private void displayTasks(String jsonResponse) {
        taskList.getChildren().clear();
        String[] tasks = jsonResponse.split("\\},\\{");

        for (String task : tasks) {
            String taskId = extractValue(task, "id");
            String name = extractValue(task, "name");
            String description = extractValue(task, "description");
            String state = extractValue(task, "state");

            // Conteneur pour la tâche (Label + Bouton)
            VBox taskContainer = new VBox();
            taskContainer.setSpacing(5);
            taskContainer.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-background-color: #f9f9f9;");

            // Affichage des détails de la tâche
            Label taskLabel = new Label("Nom : " + name + "\nDescription : " + description + "\nÉtat : " + state);

            // Bouton pour modifier la tâche
            Button modifyButton = new Button("Modifier");
            modifyButton.setOnAction(event -> modifyTask(taskId));

            // Ajouter les éléments au conteneur
            taskContainer.getChildren().addAll(taskLabel, modifyButton);

            // Ajouter le conteneur au VBox principal
            taskList.getChildren().add(taskContainer);
        }
    }

    private String extractValue(String json, String key) {
        String keyPattern = "\"" + key + "\":";
        int startIndex = json.indexOf(keyPattern) + keyPattern.length();
        char firstChar = json.charAt(startIndex);

        if (firstChar == '\"') {
            startIndex++;
            int endIndex = json.indexOf("\"", startIndex);
            return json.substring(startIndex, endIndex);
        } else {
            int endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = json.indexOf("}", startIndex);
            }
            return json.substring(startIndex, endIndex).trim();
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