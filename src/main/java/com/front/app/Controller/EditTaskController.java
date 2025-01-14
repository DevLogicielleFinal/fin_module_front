package com.front.app.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;

public class EditTaskController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private ComboBox<String> stateComboBox;

    private String taskId;

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @FXML
    private void initialize() {
        stateComboBox.getItems().addAll("TO_DO", "IN_PROGRESS", "DONE");
    }

    @FXML
    private void goToTaskPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/taskView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Task");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        String state = stateComboBox.getValue();

        if (state == null) {
            showAlert("Erreur", "Vous devez sélectionner un état pour la tâche.", AlertType.ERROR);
            return;
        }
        System.out.println("Task id: " + taskId);
        try {
            URL url = new URL("http://localhost:8080/api/tasks/" + taskId + "/state");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            String jwtToken = com.front.app.SessionManager.getJwtToken();
            if (jwtToken != null) {
                connection.setRequestProperty("Authorization", "Bearer " + jwtToken);
            }

            String jsonPayload = String.format("{\"newState\":\"%s\"}", state);

            System.out.println(jsonPayload);
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                showAlert("Succès", "L'état de la tâche a été modifié avec succès.", AlertType.INFORMATION);
                goToTaskPage(event);
            } else {
                showAlert("Erreur", "Erreur lors de la modification de l'état. Code : " + responseCode, AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la modification de la tâche.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
