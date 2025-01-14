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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.front.app.SessionManager;

import java.io.IOException;

public class TaskController {



    @FXML
    private VBox taskList;

    @FXML
    private Label statusLabel;

    @FXML
    private ScrollPane taskScrollPane;
    private String projectId;

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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
            String name = extractValue(task, "name");
            String description = extractValue(task, "description");

            Label taskLabel = new Label("Nom : " + name + " - Description : " + description);
            taskLabel.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-background-color: #f9f9f9;");
            taskList.getChildren().add(taskLabel);
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
}